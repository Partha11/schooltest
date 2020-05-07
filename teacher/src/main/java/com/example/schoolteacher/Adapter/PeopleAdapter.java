package com.example.schoolteacher.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolteacher.Model.Contact;
import com.example.schoolteacher.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {

    private List<Contact> contacts;

    public PeopleAdapter() {

        contacts = new ArrayList<>();
    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_display_layout, parent, false);
        return new PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {

        holder.userName.setText(contacts.get(position).getName());
        holder.userStatus.setText(contacts.get(position).getStatus());

        Picasso.get()
                .load(contacts.get(position).getImage())
                .placeholder(R.drawable.avatar)
                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {

        return contacts == null ? 0 : contacts.size();
    }

    public void addContact(Contact contact) {

        this.contacts.add(contact);
        this.notifyItemInserted(contacts.size() - 1);
    }

    static class PeopleViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView userStatus, userName;

        PeopleViewHolder(@NonNull View itemView) {

            super(itemView);

            profileImage = itemView.findViewById(R.id.users_profile_image);
            userStatus = itemView.findViewById(R.id.users_status);
            userName = itemView.findViewById(R.id.users_profile_name);
        }
    }
}
