package com.example.messanger.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.messanger.MessagesActivity;
import com.example.messanger.R;
import com.example.messanger.SearchActivity;
import com.example.messanger.model.Profile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private final List<Profile> profileList;
    private final Context context;

    public ProfileAdapter(Context context, List<Profile> profileList) {
        this.context = context;
        this.profileList = profileList;
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView usernameTextView, registrationDateTextView, bioTextView;
        Button startChatButton;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            registrationDateTextView = itemView.findViewById(R.id.registrationDateTextView);
            bioTextView = itemView.findViewById(R.id.bioTextView);
            startChatButton = itemView.findViewById(R.id.startChatButton);
        }
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profileList.get(position);

        holder.usernameTextView.setText(profile.getNickname());
        holder.registrationDateTextView.setText(profile.getRegistrationDateString());
        holder.bioTextView.setText(profile.getBio());

        Glide.with(context)
                .load(((AppCompatActivity)context).getString(R.string.auth_base_url) +  profile.getProfilePicture())
                .circleCrop()
                .placeholder(R.drawable.load)
                .error(R.drawable.no_image)
                .into(holder.profileImageView);
        holder.startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessagesActivity.class);
                Intent intent_search = ((SearchActivity) context).getIntent();
                intent.putExtra("PROFILE_NAME", profile.getNickname());
                intent.putExtra("PROFILE_ID", profile.getId());
                intent.putExtra("USER_NAME", intent_search.getStringExtra("USER_NAME"));
                intent.putExtra("USER_ID", intent_search.getLongExtra("USER_ID", -1));
                ((SearchActivity) context).setResult(4321);
                ((SearchActivity) context).finish();

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }
}