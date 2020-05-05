package com.example.schoolteacher.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolteacher.Model.Contacts;
import com.example.schoolteacher.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Contacts> contacts;
    private InvitationActionListener listener;

    public NotificationAdapter() {

        contacts = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.userName.setText(contacts.get(position).getName());
        holder.userStatus.setText(contacts.get(position).getStatus());

        Picasso.get().load(contacts.get(position).getImage()).placeholder(R.drawable.avatar).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {

        return contacts == null ? 0 : contacts.size();
    }

    public void clearList() {

        this.contacts.clear();
    }

    public void addContact(Contacts contact) {

        this.contacts.add(contact);
        notifyItemInserted(contacts.size() - 1);
    }

    public void setListener(InvitationActionListener listener) {

        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView userName;
        TextView userStatus;
        CircleImageView profileImage;
        Button acceptButton;
        Button cancelButton;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            userName = itemView.findViewById(R.id.users_profile_name);
            userStatus = itemView.findViewById(R.id.users_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            acceptButton = itemView.findViewById(R.id.requests_accept_btn);
            cancelButton = itemView.findViewById(R.id.requests_cancel_btn);

            acceptButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);

            acceptButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (listener != null) {

                switch (view.getId()) {

                    case R.id.requests_accept_btn:

                        listener.onAccepted(contacts.get(getAdapterPosition()).getClassId());
                        contacts.remove(getAdapterPosition());
                        notifyDataSetChanged();
                        break;

                    case R.id.requests_cancel_btn:

                        listener.onRejected(contacts.get(getAdapterPosition()).getClassId());
                        contacts.remove(getAdapterPosition());
                        notifyDataSetChanged();
                        break;
                }

            } else {

                Log.d("Adapter", "Listener null");
            }
        }
    }

    public interface InvitationActionListener {

        void onAccepted(String classId);
        void onRejected(String classId);
    }
}
