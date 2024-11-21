cmake_minimum_required(VERSION 3.4.1)
add_library(native-lib SHARED ChatSorter.cpp)

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)
find_library(log-lib log)
target_link_libraries(native-lib ${log-lib})
