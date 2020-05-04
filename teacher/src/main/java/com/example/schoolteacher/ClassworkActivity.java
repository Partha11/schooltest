package com.example.schoolteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.schoolteacher.Adapter.AssignAdapter;
import com.example.schoolteacher.Model.AssignClass;
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

public class ClassworkActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    List<AssignClass> assignmentList;
    ListView listView;

    String noteId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classwork);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // bottom nav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.classwork);

        // Classwork

        listView = findViewById(R.id.listViewAssignment);
        assignmentList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Notes");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){

            noteId = bundle.getString("id");
            getDataForFirebase(noteId);

        }


        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.streamClass:
                        Intent intent = new Intent(getApplicationContext(), StreamActivity.class);
                        intent.putExtra("id",noteId );
                        startActivity(intent);
                        break;

                    case R.id.classwork:

                        break;
                    case R.id.people:

                        Intent intent1 = new Intent(getApplicationContext(), PeopleActivity.class);
                        intent1.putExtra("id",noteId );
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });
        // end bottom nav

        // fab

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddAssignmentActivity.class);
                intent.putExtra("asId", noteId);
                startActivity(intent);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AssignClass assignClass = assignmentList.get(position);
                Intent intent = new Intent(getApplicationContext(), AssignmentScreenActivity.class);

                intent.putExtra("asId", assignClass.getAsId());
                intent.putExtra("asTitle", assignClass.getAsTitle());
                intent.putExtra("assignment", assignClass.getAssignment());

                startActivity(intent);

            }
        });

        // end Classwork

    }




    public void getDataForFirebase(String noteId) {

        myRef.child(noteId).child("Assignments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                assignmentList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    AssignClass assignments = ds.getValue(AssignClass.class);

                    assignmentList.add(assignments);


                }

                Collections.reverse(assignmentList);
                AssignAdapter assignAdapter = new AssignAdapter(ClassworkActivity.this, assignmentList);

                listView.setAdapter(assignAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
