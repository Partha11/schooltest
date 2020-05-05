package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.schoolteacher.parents.RequestParentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParentProfileActivity extends AppCompatActivity {

    private String senderClassUserId;
    private CircleImageView userProfileImage;
    private TextView userProfileName, userProfileStatus;
    private Button sendClassRequestButton;
    private DatabaseReference userRef, classRequestRef, contactsClassRef, notificationRef, classReference;
    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;

    private String parentUid;
    private String classId;

    private boolean parentFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Profile");

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        classId = getIntent().getStringExtra("id");
        parentUid = getIntent().getStringExtra("visitClassUserId");

        Intent noteIntent = new Intent(ParentProfileActivity.this, RequestParentActivity.class);
        noteIntent.putExtra("id", classId);

        firebaseDatabase = FirebaseDatabase.getInstance();

        userRef = firebaseDatabase.getReference().child("Users");
        classRequestRef = firebaseDatabase.getReference().child("Class Requests");
        contactsClassRef = firebaseDatabase.getReference().child("Class Contacts");
        notificationRef = firebaseDatabase.getReference().child("Notifications");
        classReference = firebaseDatabase.getReference("Notes").child(classId);

        senderClassUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        validateClassRequest();

        userProfileImage = findViewById(R.id.visit_profile_image);
        userProfileName = findViewById(R.id.visit_user_name);
        userProfileStatus = findViewById(R.id.visit_profile_status);
        sendClassRequestButton = findViewById(R.id.send_class_request_button);

        sendClassRequestButton.setOnClickListener(v -> sendClassRequest());

        getUserInfo();
    }

    private void getUserInfo() {

        if (senderClassUserId.equals(parentUid)) {

            sendClassRequestButton.setEnabled(false);
            sendClassRequestButton.setVisibility(View.INVISIBLE);
        }

        userRef.child(parentUid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))) {

                    String userImage = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                    Picasso.get().load(userImage).placeholder(R.drawable.avatar).into(userProfileImage);
                }

                String userName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                String userStatus = Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString();

                userProfileName.setText(userName);
                userProfileStatus.setText(userStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendClassRequest() {

        if (!parentFound) {

            HashMap<String, Long> dataMap = new HashMap<>();

            dataMap.put(parentUid, System.currentTimeMillis());
            classReference.child("Invites").setValue(dataMap).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    HashMap<String, String> classNotificationMap = new HashMap<>();
                    classNotificationMap.put("from", senderClassUserId);
                    classNotificationMap.put("classId", classId);
                    classNotificationMap.put("type", "request");

                    notificationRef.child(parentUid).child(classId).setValue(classNotificationMap);
                    sendClassRequestButton.setText(R.string.req_sent);
                }
            });

        } else {

            classReference.child("Invites").child(parentUid).removeValue().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    notificationRef.child(parentUid).removeValue();

                    String message = "Invitation Canceled";
                    sendClassRequestButton.setText(message);
                }
            });
        }

        /*if (sendClassRequestButton.getText().equals("Cancel Invite")) {

            classRequestRef.child(senderClassUserId).child(receiverClassUserId)
                    .removeValue().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    classRequestRef.child(receiverClassUserId).child(senderClassUserId).removeValue();
                }
            });

            sendClassRequestButton.setText(R.string.add_to_class);
            return;
        }*/

/*        classRequestRef.child(senderClassUserId).child(parentUid)
                .child("requestType").setValue("sent").addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                classRequestRef.child(parentUid).child(senderClassUserId)
                        .child("requestType").setValue("received").addOnCompleteListener(task1 -> {

                    if (task1.isSuccessful()) {

                        HashMap<String, String> classNotificationMap = new HashMap<>();
                        classNotificationMap.put("from", senderClassUserId);
                        classNotificationMap.put("type", "request");

                        notificationRef.child(parentUid).push().setValue(classNotificationMap);
                        sendClassRequestButton.setText(R.string.req_sent);
                    }
                });
            }
        });*/
    }

    private void validateClassRequest() {

        classReference.child("Invites").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    if (d.getKey() != null && d.getKey().equals(parentUid)) {

                        sendClassRequestButton.setText(R.string.cancel_invite);
                        parentFound = true;
                        break;
                    }
                }

                if (!parentFound) {

                    sendClassRequestButton.setText(R.string.add_to_class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
