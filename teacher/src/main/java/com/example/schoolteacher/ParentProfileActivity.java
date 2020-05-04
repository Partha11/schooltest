package com.example.schoolteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.schoolteacher.parents.RequestParentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParentProfileActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private String receiverClassUserId, senderClassUserId;
    private CircleImageView userProfileImage;
    private TextView userProfileName, userProfileStatus;
    private Button sendClassRequestButton;
    private DatabaseReference userRef, classRequestRef, contactsClassRef, notificationRef;
    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;

    String userID;
    String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Profile");

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        noteId = getIntent().getStringExtra("id");
        Intent noteIntent = new Intent(ParentProfileActivity.this, RequestParentActivity.class);
        noteIntent.putExtra("id",noteId);


        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        classRequestRef = FirebaseDatabase.getInstance().getReference()
                .child("Class Requests");
        contactsClassRef = FirebaseDatabase.getInstance().getReference().child("Class Contacts");

        notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");

        mAuth = FirebaseAuth.getInstance();
        senderClassUserId = mAuth.getCurrentUser().getUid();

        receiverClassUserId = getIntent().getExtras().get("visitClassUserId").toString();



        userProfileImage = (CircleImageView) findViewById(R.id.visit_profile_image);
        userProfileName = (TextView) findViewById(R.id.visit_user_name);
        userProfileStatus = (TextView) findViewById(R.id.visit_profile_status);
        sendClassRequestButton = (Button) findViewById(R.id.send_class_request_button);
        sendClassRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendClassRequest();
            }
        });

        RetrieveUserInfo();
    }


    private void RetrieveUserInfo() {

        if(senderClassUserId.equals(receiverClassUserId)){
            sendClassRequestButton.setEnabled(false);
            sendClassRequestButton.setVisibility(View.INVISIBLE);
        }

        userRef.child(receiverClassUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))) {
                    String userImage = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(userImage).placeholder(R.drawable.avatar).into(userProfileImage);
                }

                String userName = dataSnapshot.child("name").getValue().toString();
                String userStatus = dataSnapshot.child("status").getValue().toString();

                userProfileName.setText(userName);
                userProfileStatus.setText(userStatus);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void SendClassRequest() {
        if(sendClassRequestButton.getText().equals("Cancel Invite")){
            classRequestRef.child(senderClassUserId).child(receiverClassUserId)
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        classRequestRef.child(receiverClassUserId).child(senderClassUserId)
                                .removeValue();
                    }
                }
            });
            sendClassRequestButton.setText(R.string.add_to_class);
            return;
        }
        classRequestRef.child(senderClassUserId).child(receiverClassUserId)
                .child("requestType").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            classRequestRef.child(receiverClassUserId).child(senderClassUserId)
                                    .child("requestType").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                HashMap<String, String> classNotificationMap = new HashMap<>();
                                                classNotificationMap.put("from", senderClassUserId);
                                                classNotificationMap.put("type", "request");
                                                notificationRef.child(receiverClassUserId).push()
                                                        .setValue(classNotificationMap);
                                                sendClassRequestButton.setText(R.string.req_sent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


}
