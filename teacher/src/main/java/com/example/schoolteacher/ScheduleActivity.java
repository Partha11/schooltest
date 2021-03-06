package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Adapter.ScheduleAdapter;
import com.example.schoolteacher.Model.ClassModel;
import com.example.schoolteacher.Model.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScheduleActivity extends AppCompatActivity  {

    ListView listView;

    private DatabaseReference reference;
    private ScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // fab
        FloatingActionButton fab = findViewById(R.id.fab_sch);

        fab.setOnClickListener(view -> {

            Intent launchIntent = new Intent(getBaseContext(), AddSchedulerActivity.class);
            startActivity(launchIntent);
        });

//        loadSchedules();
//        listView.setOnItemLongClickListener(this);

        initialize();
    }

    private void initialize() {

        List<ClassModel> classes = new ArrayList<>();
        listView = findViewById(R.id.schedulerList);

        reference = FirebaseDatabase.getInstance().getReference("Notes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {

            //Add code for loading login activity
            return;
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    ClassModel mClass = d.getValue(ClassModel.class);

                    if (mClass != null) {

                        if (mClass.getCreatedBy() != null && mClass.getCreatedBy().equals(user.getEmail())) {

                            classes.add(mClass);
                        }
                    }
                }

                fetchSchedules(classes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchSchedules(List<ClassModel> classes) {

        List<Schedule> schedules = new ArrayList<>();

        for (ClassModel c : classes) {

            reference.child(c.getClassId()).child("Schedules").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    schedules.clear();

                    for (DataSnapshot d : dataSnapshot.getChildren()) {

                        Schedule schedule = d.getValue(Schedule.class);

                        if (schedule != null) {

                            schedules.add(schedule);
                        }
                    }

                    adapter = new ScheduleAdapter(ScheduleActivity.this, schedules);
                    listView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void refresh(MenuItem item) {

//        fetchSchedules();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scheduler_menu, menu);
        return true;
    }


    // itemSelected toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

}
