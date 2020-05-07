package com.example.schoolteacher;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolteacher.Adapter.PeopleAdapter;
import com.example.schoolteacher.Model.Contact;
import com.example.schoolteacher.Model.Member;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PeopleActivity extends AppCompatActivity {

    private String classId;

    private PeopleAdapter adapter;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.people);


        bottomNav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.streamClass:
                    Intent streamIntent = new Intent(getApplicationContext(), StreamActivity.class);
                    streamIntent.putExtra("id", classId);
                    startActivity(streamIntent);
                    break;

                case R.id.classwork:
                    Intent intent = new Intent(getApplicationContext(), ClassworkActivity.class);
                    intent.putExtra("id", classId);
                    startActivity(intent);
                    break;

                case R.id.people:
                    break;
            }

            return true;
        });

        initialize();
    }

    private void initialize() {

        adapter = new PeopleAdapter();
        reference = FirebaseDatabase.getInstance().getReference();
        classId = getIntent().getStringExtra("id");
        List<String> members = new ArrayList<>();

        if (classId == null) {

            Toast.makeText(this, "Class id is null, please check if it has been passed via the intent", Toast.LENGTH_SHORT).show();
            return;
        }

        RecyclerView peopleList = findViewById(R.id.peoplerv);
        ImageButton addParentBtn = findViewById(R.id.studentBTN);

        addParentBtn.setOnClickListener(v -> {

            Intent parentIntent = new Intent(getApplicationContext(), FindParentActivity.class);
            parentIntent.putExtra("id", classId);
            startActivity(parentIntent);
            overridePendingTransition(0, 0);
        });

        peopleList.setItemAnimator(new DefaultItemAnimator());
        peopleList.setLayoutManager(new LinearLayoutManager(PeopleActivity.this));
        peopleList.setAdapter(adapter);

        reference.child("Notes").child(classId).child("memberList").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    Member member = d.getValue(Member.class);

                    if (member != null) {

                        members.add(member.getMemberId());
                    }
                }

                fetchMemberInfo(members);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchMemberInfo(List<String> members) {

        List<Contact> contacts = new ArrayList<>();

        for (String m : members) {

            reference.child("Users").child(m).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Contact contact = dataSnapshot.getValue(Contact.class);

                    if (contact != null && !contacts.contains(contact)) {

                        contacts.add(contact);
                        adapter.addContact(contact);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
