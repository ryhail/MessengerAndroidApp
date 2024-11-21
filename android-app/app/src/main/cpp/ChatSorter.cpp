#include <jni.h>
#include <vector>
#include <algorithm>

jfieldID GetField(JNIEnv* env, jclass cls, const char* name, const char* sig) {
    jfieldID field = env->GetFieldID(cls, name, sig);
    if (!field) {
        env->ExceptionClear();
        throw std::runtime_error("Field not found");
    }
    return field;
}

jmethodID GetMethod(JNIEnv* env, jclass cls, const char* name, const char* sig) {
    jmethodID method = env->GetMethodID(cls, name, sig);
    if (!method) {
        env->ExceptionClear();
        throw std::runtime_error("Method not found");
    }
    return method;
}

bool compareChats(JNIEnv* env, jobject chat1, jobject chat2, jclass chatClass, jfieldID lastMessageField, jclass messageClass, jfieldID timestampField) {
    jobject lastMessage1 = env->GetObjectField(chat1, lastMessageField);
    jobject lastMessage2 = env->GetObjectField(chat2, lastMessageField);

    if (!lastMessage1 && !lastMessage2) return true;

    if (!lastMessage1) return false;
    if (!lastMessage2) return true;

    jobject timestamp1 = env->GetObjectField(lastMessage1, timestampField);
    jobject timestamp2 = env->GetObjectField(lastMessage2, timestampField);

    jlong time1 = timestamp1 ? env->CallLongMethod(timestamp1, env->GetMethodID(env->FindClass("java/util/Date"), "getTime", "()J")) : 0;
    jlong time2 = timestamp2 ? env->CallLongMethod(timestamp2, env->GetMethodID(env->FindClass("java/util/Date"), "getTime", "()J")) : 0;

    return time1 > time2;
}

extern "C"
jobject JNICALL Java_com_example_messanger_model_ChatSorter_sortChats(JNIEnv* env, jobject obj, jobject chatList) {
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID getSize = GetMethod(env, arrayListClass, "size", "()I");
    jmethodID getElement = GetMethod(env, arrayListClass, "get", "(I)Ljava/lang/Object;");
    jmethodID addElement = GetMethod(env, arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jint listSize = env->CallIntMethod(chatList, getSize);

    std::vector<jobject> chats;

    for (int i = 0; i < listSize; i++) {
        jobject chat = env->CallObjectMethod(chatList, getElement, i);
        chats.push_back(chat);
    }

    jclass chatClass = env->FindClass("com/example/messanger/model/Chat");
    jfieldID lastMessageField = GetField(env, chatClass, "lastMessage", "Lcom/example/messanger/model/Message;");

    jclass messageClass = env->FindClass("com/example/messanger/model/Message");
    jfieldID timestampField = GetField(env, messageClass, "timestamp", "Ljava/util/Date;");

    std::sort(chats.begin(), chats.end(), [&](jobject chat1, jobject chat2) {
        return compareChats(env, chat1, chat2, chatClass, lastMessageField, messageClass, timestampField);
    });

    jobject sortedList = env->NewObject(arrayListClass, env->GetMethodID(arrayListClass, "<init>", "()V"));

    for (const auto& chat : chats) {
        env->CallBooleanMethod(sortedList, addElement, chat);
    }

    return sortedList;
}
