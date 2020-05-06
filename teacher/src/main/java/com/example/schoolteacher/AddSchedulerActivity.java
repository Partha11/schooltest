package com.example.schoolteacher;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Model.ClassModel;
import com.example.schoolteacher.Model.Schedule;
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

public class AddSchedulerActivity extends AppCompatActivity {

    private List<ClassModel> classes;

    private DatabaseReference reference;
    private Spinner classSpinner;
    private Spinner daySpinner;
    private EditText subjectName;
    private TimePicker timePicker;

    private ArrayAdapter<String> classNameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scheduler);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initialize();
    }

    private void initialize() {

        classes = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();
        classSpinner = findViewById(R.id.classSelector);
        daySpinner = findViewById(R.id.daySelector);
        subjectName = findViewById(R.id.subjectName);
        timePicker = findViewById(R.id.timePicker);
        classNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        ArrayList<String> days = new ArrayList<>();

        classSpinner.setAdapter(classNameAdapter);

        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");
        days.add("Sunday");

        ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);
        daySpinner.setAdapter(daysAdapter);

        Button button = findViewById(R.id.saveBUTTON_SCHEDULE);
        button.setOnClickListener(v -> saveSchedule());

        populateSpinner();
    }

    private void populateSpinner() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {

            //Add code for loading login activity
            return;
        }

        reference.child("Notes").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    ClassModel mClass = d.getValue(ClassModel.class);

                    if (mClass != null) {

                        if (mClass.getCreatedBy() != null && mClass.getCreatedBy().equals(user.getEmail())) {

                            classes.add(mClass);
                            classNameAdapter.add(mClass.getClassName());
                        }
                    }
                }

                classNameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveSchedule() {

        String subject = subjectName.getText().toString();

        if (subject.length() < 2) {

            Toast.makeText(this, "Enter a valid subject name", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String key = reference.push().getKey();
        String classId = classes.get(classSpinner.getSelectedItemPosition()).getClassId();
        String classTime = "at " + hour + ":" + minute + " " + daySpinner.getSelectedItem().toString();

        if (key == null) {

            Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
            return;
        }

        Schedule schedule = new Schedule();

        schedule.setScheduleId(key);
        schedule.setClassName(classes.get(classSpinner.getSelectedItemPosition()).getClassName());
        schedule.setSubjectName(subject);
        schedule.setClassTime(classTime);

        reference.child("Notes").child(classId).child("Schedules").child(key)
                .setValue(schedule).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                finish();
            }
        });
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
