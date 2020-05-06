package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Adapter.AssignAdapter;
import com.example.schoolteacher.Model.Assignment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClassworkActivity extends AppCompatActivity {

    private DatabaseReference reference;

    private List<Assignment> assignmentList;

    private ListView listView;
    private String classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classwork);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // bottom nav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.classwork);

        // Classwork

        listView = findViewById(R.id.listViewAssignment);
        assignmentList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Notes");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            classId = bundle.getString("id");

            if (classId != null && !classId.isEmpty()) {

                getDataForFirebase(classId);
            }
        }

        bottomNav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.streamClass:

                    Intent intent = new Intent(getApplicationContext(), StreamActivity.class);
                    intent.putExtra("id", classId);
                    startActivity(intent);
                    break;

                case R.id.classwork:

                    break;

                case R.id.people:

                    Intent intent1 = new Intent(getApplicationContext(), PeopleActivity.class);
                    intent1.putExtra("id", classId);
                    startActivity(intent1);
                    break;
            }

            return true;
        });

        // end bottom nav
        // fab

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), AddAssignmentActivity.class);
            intent.putExtra("classId", classId);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Assignment assignment = assignmentList.get(position);
            Intent intent = new Intent(getApplicationContext(), AssignmentScreenActivity.class);

            intent.putExtra("classId", classId);
            intent.putExtra("asId", assignment.getAssignmentId());
            intent.putExtra("assignmentTitle", assignment.getAssignmentTitle());
            intent.putExtra("assignment", assignment.getAssignmentDescription());

            startActivity(intent);
        });

        // end Classwork
    }

    public void getDataForFirebase(String classId) {

        reference.child(classId).child("Assignments").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                assignmentList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Assignment assignment = ds.getValue(Assignment.class);
                    assignmentList.add(assignment);
                }

                Collections.reverse(assignmentList);
                AssignAdapter assignAdapter = new AssignAdapter(ClassworkActivity.this, assignmentList);

                listView.setAdapter(assignAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Error", databaseError.getMessage());
            }
        });
    }

    // item toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
