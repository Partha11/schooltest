package com.example.schoolteacher.parents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolteacher.Adapter.NotificationAdapter;
import com.example.schoolteacher.Model.ClassModel;
import com.example.schoolteacher.Model.Contact;
import com.example.schoolteacher.Model.Member;
import com.example.schoolteacher.Model.NotificationModel;
import com.example.schoolteacher.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestParentActivity extends AppCompatActivity implements NotificationAdapter.InvitationActionListener {

    private DatabaseReference classRequestRef, userRef, contactsClassRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    FirebaseDatabase firebaseDatabase;
    String userID;

    String noteId;

    //RecyclerView

    private FirebaseUser user;
    private DatabaseReference reference;

    private List<NotificationModel> notifications;

    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_parent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        // bottom nav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_notifications);

        //bottom nav

        bottomNav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navigation_class:
                    startActivity(new Intent(getApplicationContext(), MainParentsActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.navigation_due:
                    startActivity(new Intent(getApplicationContext(), DueParentsActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.navigation_messages:
                    startActivity(new Intent(getApplicationContext(), MessageParentActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.navigation_explore:
                    startActivity(new Intent(getApplicationContext(), ExploreParentActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.navigation_notifications:
                    break;
            }

            return true;
        });


        // requests

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();

        noteId = getIntent().getStringExtra("id");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        classRequestRef = FirebaseDatabase.getInstance().getReference()
                .child("Notifications"); // Class Requests
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        contactsClassRef = FirebaseDatabase.getInstance().getReference()
                .child("Class Contacts"); // Class Contacts
         // class request list

        //       linearLayoutManager = new LinearLayoutManager(this);
//        classRequestsList.setLayoutManager(new LinearLayoutManager(this));

        initialize();
    }

    private void initialize() {

        notifications = new ArrayList<>();
        adapter = new NotificationAdapter();
        user = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView classRequestsList = findViewById(R.id.class_requests_list);

        if (user != null) {

            reference = FirebaseDatabase.getInstance().getReference();

        } else {

            reference = null;
        }

        adapter.setListener(this);
        classRequestsList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        classRequestsList.setItemAnimator(new DefaultItemAnimator());
        classRequestsList.setAdapter(adapter);

        loadClassRequests();
    }

    private void loadClassRequests() {

        if (reference == null) {

            Toast.makeText(this, "Session expired", Toast.LENGTH_SHORT).show();

        } else {

            reference.child("Notifications").child(user.getUid()).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    notifications.clear();

                    for (DataSnapshot d : dataSnapshot.getChildren()) {

                        NotificationModel notification = d.getValue(NotificationModel.class);
                        notifications.add(notification);
                    }

                    getContactsFromNotification(notifications);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.d("Error", databaseError.getMessage());
                }
            });
        }
    }

    private void getContactsFromNotification(List<NotificationModel> notifications) {

        adapter.clearList();

        for (NotificationModel notification: notifications) {

            reference.child("Users").child(notification.getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Contact contact = dataSnapshot.getValue(Contact.class);

                    if (contact != null) {

                        contact.setClassId(notification.getClassId());
                        adapter.addContact(contact);

                        Log.d("SIze", adapter.getItemCount() + "");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.d("Error", databaseError.getMessage());
                }
            });
        }
    }

    private void removeNotification(String classId) {

        if (reference != null) {

            reference.child("Notes").child(classId).child("Invites").child(user.getUid()).removeValue();
            reference.child("Notifications").child(user.getUid()).child(classId).removeValue();
        }
    }

    @Override
    public void onAccepted(String classId) {

        reference.child("Notes").child(classId)
                .addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ClassModel mClass = dataSnapshot.getValue(ClassModel.class);

                if (mClass != null) {

                    Member member = new Member();
                    String key = reference.push().getKey();

                    if (key == null) {

                        return;
                    }

                    member.setJoinedAt(String.valueOf(System.currentTimeMillis()));
                    member.setMemberId(user.getUid());

                    mClass.setClassFollowers(mClass.getClassFollowers() + 1);
                    reference.child("Notes").child(classId).child("classFollowers").setValue(mClass.getClassFollowers());
                    reference.child("Notes").child(classId).child("memberList").child(key).setValue(member);
                    reference.child("Users").child(user.getUid()).child("Classes").child(classId).setValue(member);

                    removeNotification(classId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Error", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onRejected(String classId) {

        removeNotification(classId);
    }



/*    @Override
    public void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(classRequestRef.child(currentUserId), Contacts.class)
                        .build();

        final FirebaseRecyclerAdapter<Contacts, RequestParentActivity.RequestsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, RequestParentActivity.RequestsViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull final RequestParentActivity.RequestsViewHolder holder, int position, @NonNull Contacts model) {
                        holder.itemView.findViewById(R.id.requests_accept_btn).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.requests_cancel_btn).setVisibility(View.VISIBLE);

                        final String listUserId = getRef(position).getKey();
                        DatabaseReference getTypeRef = getRef(position).child("requestType").getRef();
                        getTypeRef.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    String type = dataSnapshot.getValue().toString();

                                    if (type.equals("received")) {

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
                                                holder.userStatus.setText(R.string.wanna_add_class);

                                                //test onClick Button accept or decline
                                                holder.itemView.findViewById(R.id.requests_accept_btn)
                                                        .setOnClickListener(v -> contactsClassRef.child(currentUserId).child(listUserId).child("Class Contact")
                                                                .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            contactsClassRef.child(listUserId).child(currentUserId).child("Class Contact")
                                                                                    .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        classRequestRef.child(currentUserId).child(listUserId)
                                                                                                .removeValue()
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if(task.isSuccessful()){
                                                                                                            classRequestRef.child(listUserId).child(currentUserId)
                                                                                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    Toast.makeText(RequestParentActivity.this, "Class Access Granted",Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });

                                                                        }
                                                                    }
                                                                }));

                                                holder.itemView.findViewById(R.id.requests_cancel_btn).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        classRequestRef.child(currentUserId).child(listUserId)
                                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    classRequestRef.child(listUserId).child(currentUserId)
                                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                Toast.makeText(RequestParentActivity.this, "Contact Deleted",Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                    }
                                                });


                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        CharSequence options[] = new CharSequence[]{
                                                                "Accept",
                                                                "Cancel"
                                                        };
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(RequestParentActivity.this);
                                                        builder.setTitle(requestUserName + " Class Request"); // Class Request
                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if(which == 0){
                                                                    contactsClassRef.child(currentUserId).child(listUserId).child("Class Contact") // Class Contact
                                                                            .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                contactsClassRef.child(listUserId).child(currentUserId).child("Class Contact") // Class Contact
                                                                                        .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            classRequestRef.child(currentUserId).child(listUserId)
                                                                                                    .removeValue()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if(task.isSuccessful()){
                                                                                                                classRequestRef.child(listUserId).child(currentUserId)
                                                                                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                                        Toast.makeText(RequestParentActivity.this, "New Contact Saved",Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    }
                                                                                });

                                                                            }
                                                                        }
                                                                    });

                                                                }
                                                                else if(which == 1){
                                                                    classRequestRef.child(currentUserId).child(listUserId) // ClassRequestRef
                                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                classRequestRef.child(listUserId).child(currentUserId)
                                                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            Toast.makeText(RequestParentActivity.this, "Contact Deleted",Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                        builder.show();
                                                    }
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

                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        CharSequence options[] = new CharSequence[]{
                                                                "Cancel Class Request"
                                                        };
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(RequestParentActivity.this);
                                                        builder.setTitle("Already sent request");
                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if(which == 0){
                                                                    classRequestRef.child(currentUserId).child(listUserId)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        classRequestRef.child(listUserId)
                                                                                                .child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if(task.isSuccessful()){
                                                                                                    Toast.makeText(RequestParentActivity.this, "You have cancelled  the class request",Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                    }

                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                                        builder.show();
                                                    }
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
                    public RequestParentActivity.RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        RequestParentActivity.RequestsViewHolder holder = new RequestParentActivity.RequestsViewHolder(view);
                        return holder;
                    }
                };

        classRequestsList.setAdapter(adapter);
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
            acceptButton = (Button)itemView.findViewById(R.id.requests_accept_btn);
            cancelButton = (Button)itemView.findViewById(R.id.requests_cancel_btn);

        }
    }*/


}
