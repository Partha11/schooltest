package com.example.schoolteacher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolteacher.Model.Contact;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    //    private LinearLayoutManager linearLayoutManager;
    private View requestsFragmentView;
    private RecyclerView mRequestsList;
    private DatabaseReference chatRequestsRef, userRef, contactsRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Requests");

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        chatRequestsRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests"); // Class Requests
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts"); // Class Contacts
        mRequestsList = findViewById(R.id.chat_requests_list); // class request list

        //       linearLayoutManager = new LinearLayoutManager(this);
        mRequestsList.setLayoutManager(new LinearLayoutManager(this));

    }



    @Override
    public void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Contact> options =
                new FirebaseRecyclerOptions.Builder<Contact>()
                        .setQuery(chatRequestsRef.child(currentUserId), Contact.class)
                        .build();

        final FirebaseRecyclerAdapter<Contact, RequestsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contact, RequestsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestsViewHolder holder, int position, @NonNull Contact model) {
                        holder.itemView.findViewById(R.id.requests_accept_btn).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.requests_cancel_btn).setVisibility(View.VISIBLE);

                        final String listUserId = getRef(position).getKey();
                        DatabaseReference getTypeRef = getRef(position).child("requestType").getRef();
                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String type = dataSnapshot.getValue().toString();
                                    if(type.equals("received")){
                                        userRef.child(listUserId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChild("image")){
                                                    final String requestProfileImage = dataSnapshot.child("image").getValue().toString();
                                                    Picasso.get().load(requestProfileImage).placeholder(R.drawable.avatar).into(holder.profileImage);
                                                }

                                                final String requestUserName = dataSnapshot.child("name").getValue().toString();
                                                final String requestUserStatus = dataSnapshot.child("status").getValue().toString();
                                                holder.userName.setText(requestUserName);
                                                holder.userStatus.setText(R.string.wanna_connect);

                                                //test onClick Button accept or decline
                                                holder.itemView.findViewById(R.id.requests_accept_btn)
                                                        .setOnClickListener(v -> contactsRef.child(currentUserId).child(listUserId).child("Contact")
                                                                .setValue("Saved").addOnCompleteListener(task -> {
                                                                    if(task.isSuccessful()){
                                                                        contactsRef.child(listUserId).child(currentUserId).child("Contact")
                                                                                .setValue("Saved").addOnCompleteListener(task1 -> {
                                                                            if(task1.isSuccessful()){
                                                                                chatRequestsRef.child(currentUserId).child(listUserId)
                                                                                        .removeValue()
                                                                                        .addOnCompleteListener(task11 -> {
                                                                                            if(task11.isSuccessful()){
                                                                                                chatRequestsRef.child(listUserId).child(currentUserId)
                                                                                                        .removeValue().addOnCompleteListener(task111 -> Toast.makeText(RequestsActivity.this, "New Contact Saved",Toast.LENGTH_SHORT).show());
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });

                                                                    }
                                                                }));

                                                holder.itemView.findViewById(R.id.requests_cancel_btn).setOnClickListener(v -> chatRequestsRef.child(currentUserId).child(listUserId)
                                                        .removeValue().addOnCompleteListener(task -> {
                                                            if(task.isSuccessful()){
                                                                chatRequestsRef.child(listUserId).child(currentUserId)
                                                                        .removeValue().addOnCompleteListener(task12 -> {
                                                                            if(task12.isSuccessful()){
                                                                                Toast.makeText(RequestsActivity.this, "Contact Deleted",Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        }));


                                                holder.itemView.setOnClickListener(v -> {
                                                    CharSequence[] options1 = new CharSequence[]{
                                                            "Accept",
                                                            "Cancel"
                                                    };
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestsActivity.this);
                                                    builder.setTitle(requestUserName + " Chat Request"); // Class Request
                                                    builder.setItems(options1, (dialog, which) -> {
                                                        if(which == 0){
                                                            contactsRef.child(currentUserId).child(listUserId).child("Contact") // Class Contact
                                                                    .setValue("Saved").addOnCompleteListener(task -> {
                                                                        if(task.isSuccessful()){
                                                                            contactsRef.child(listUserId).child(currentUserId).child("Contact") // Class Contact
                                                                                    .setValue("Saved").addOnCompleteListener(task13 -> {
                                                                                        if(task13.isSuccessful()){
                                                                                            chatRequestsRef.child(currentUserId).child(listUserId)
                                                                                                    .removeValue()
                                                                                                    .addOnCompleteListener(task131 -> {
                                                                                                        if(task131.isSuccessful()){
                                                                                                            chatRequestsRef.child(listUserId).child(currentUserId)
                                                                                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task131) {
                                                                                                                    Toast.makeText(RequestsActivity.this, "New Contact Saved",Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    });

                                                                        }
                                                                    });

                                                        }
                                                        else if(which == 1){
                                                            chatRequestsRef.child(currentUserId).child(listUserId) // ClassRequestRef
                                                                    .removeValue().addOnCompleteListener(task -> {
                                                                        if(task.isSuccessful()){
                                                                            chatRequestsRef.child(listUserId).child(currentUserId)
                                                                                    .removeValue().addOnCompleteListener(task14 -> {
                                                                                        if(task14.isSuccessful()){
                                                                                            Toast.makeText(RequestsActivity.this, "Contact Deleted",Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                        }
                                                                    });
                                                        }
                                                    });
                                                    builder.show();
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else if(type.equals("sent")){
                                        Button requestSendBtn = holder.itemView.findViewById(R.id.requests_accept_btn);
                                        requestSendBtn.setText(R.string.req_sent);
                                        holder.itemView.findViewById(R.id.requests_cancel_btn).setVisibility(View.INVISIBLE);
                                        userRef.child(listUserId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChild("image")){
                                                    final String requestProfileImage = dataSnapshot.child("image").getValue().toString();
                                                    Picasso.get().load(requestProfileImage).placeholder(R.drawable.avatar).into(holder.profileImage);
                                                }

                                                final String requestUserName = dataSnapshot.child("name").getValue().toString();
                                                final String requestUserStatus = dataSnapshot.child("status").getValue().toString();
                                                holder.userName.setText(requestUserName);
                                                holder.userStatus.setText(requestUserStatus);

                                                holder.itemView.setOnClickListener(v -> {
                                                    CharSequence[] options12 = new CharSequence[]{
                                                            "Cancel Chat Request"
                                                    };
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestsActivity.this);
                                                    builder.setTitle("Already sent request");
                                                    builder.setItems(options12, (dialog, which) -> {
                                                        if(which == 0){
                                                            chatRequestsRef.child(currentUserId).child(listUserId)
                                                                    .removeValue()
                                                                    .addOnCompleteListener(task -> {
                                                                        if(task.isSuccessful()){
                                                                            chatRequestsRef.child(listUserId)
                                                                                    .child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        Toast.makeText(RequestsActivity.this, "You have cancelled  the chat request",Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                        }

                                                                    });
                                                        }
                                                    });
                                                    builder.show();
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    @NonNull
                    @Override
                    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        RequestsViewHolder holder = new RequestsViewHolder(view);
                        return holder;
                    }
                };
        mRequestsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class RequestsViewHolder extends  RecyclerView.ViewHolder{
        TextView userName, userStatus;
        CircleImageView profileImage;
        Button acceptButton, cancelButton;
        public RequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.users_profile_name);
            userStatus = itemView.findViewById(R.id.users_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            acceptButton = itemView.findViewById(R.id.requests_accept_btn);
            cancelButton = itemView.findViewById(R.id.requests_cancel_btn);

        }
    }

}
