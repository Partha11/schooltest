package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Model.ClassModel;
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
    ArrayAdapter adapter;
    ArrayList<String> subs;
    ArrayList<String> subx;
    ArrayList<String> times;

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

        subs = new ArrayList<>();
        times = new ArrayList<>();
        subx = new ArrayList<>();
        listView = findViewById(R.id.schedulerList);
//        loadSchedules();
//        listView.setOnItemLongClickListener(this);

        initialize();
    }

    private void initialize() {

        List<ClassModel> classes = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes");
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

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


/*    private void loadSchedules() {
        subs.clear();
        times.clear();
        String qu = "SELECT * FROM SCHEDULE ORDER BY subject";
//        Cursor cursor = AppBase.handler.execQuery(qu);
        if (adapter == null || adapter.getCount() == 0) {
            Toast.makeText(getBaseContext(), "No Schedules Available", Toast.LENGTH_LONG).show();
        } else {
//            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                subx.add(cursor.getString(1));
                subs.add(cursor.getString(1) + "\nfor " + cursor.getString(0) + "\nat " + cursor.getString(2) + " : " + cursor.getString(3));
                times.add(cursor.getString(2));
                cursor.moveToNext();
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, subs);
        listView.setAdapter(adapter);
    }

 */


    public void refresh(MenuItem item) {
 //       loadSchedules();
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
