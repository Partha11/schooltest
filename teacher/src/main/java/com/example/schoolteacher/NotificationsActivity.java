package com.example.schoolteacher;

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
import com.example.schoolteacher.Model.NotificationModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity implements NotificationAdapter.InvitationActionListener {

    private FirebaseUser user;
    private DatabaseReference reference;

    private List<NotificationModel> notifications;

    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportActionBar();
        setSupportActionBar(toolbar);
        setTitle("Notifications");

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // bottom nav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.bottomBarItemFourth);

        initialize();

        bottomNav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.bottomBarItemFirst:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    break;
                case R.id.bottomBarItemSecond:
                    startActivity(new Intent(getApplicationContext(), ClassActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.bottomBarItemThird:
                    startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                    overridePendingTransition(0,0);
                    break;
                case R.id.bottomBarItemFourth:
                    break;
            }

            return true;
        });

        loadClassRequests();
    }

    private void initialize() {

        notifications = new ArrayList<>();
        adapter = new NotificationAdapter();
        user = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView recyclerView = findViewById(R.id.notification_recycler_view);

        if (user != null) {

            reference = FirebaseDatabase.getInstance().getReference();

        } else {

            reference = null;
        }

        adapter.setListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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
                    adapter.addContact(contact);
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

        reference.child("Notes").child(classId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ClassModel mClass = dataSnapshot.getValue(ClassModel.class);

                if (mClass != null) {

                    HashMap<String, Long> data = new HashMap<>();
                    data.put("joinedAt", System.currentTimeMillis());

                    mClass.setClassFollowers(mClass.getClassFollowers() + 1);
                    reference.child("Notes").child(classId).child("classFollowers").setValue(mClass.getClassFollowers());
                    reference.child("Notes").child(classId).child("memberList").setValue(data);
                    reference.child("Users").child("Classes").child(classId).setValue(data);

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
}
