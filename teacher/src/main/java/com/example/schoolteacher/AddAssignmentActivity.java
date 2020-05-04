package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Model.AssignClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddAssignmentActivity extends AppCompatActivity {

    private TextInputEditText etAddAssignment;
    private TextInputEditText etAddAssignmentTitle;

    private DatabaseReference reference;

    private String classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        etAddAssignment = findViewById(R.id.etAddAssignment);
        etAddAssignmentTitle = findViewById(R.id.ettAddAssignmentTitle);

        classId = getIntent().getStringExtra("classId");
//        Intent intent = getIntent();
//        noteId = intent.getStringExtra("noteId");

        reference = FirebaseDatabase.getInstance().getReference("Notes").child(classId).child("Assignments");

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

    public void addAssignment(View view) {

        String assignment = Objects.requireNonNull(etAddAssignment.getText()).toString().trim();
        String asTitle = Objects.requireNonNull(etAddAssignmentTitle.getText()).toString().trim();

        if (!TextUtils.isEmpty(assignment) && !TextUtils.isEmpty(asTitle)) {

            String assignmentId = reference.push().getKey();
            AssignClass assignClass = new AssignClass(assignmentId, asTitle, assignment);

            if (assignmentId == null) {

                Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();

            } else {

                reference.child(assignmentId).setValue(assignClass).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(AddAssignmentActivity.this, getString(R.string.assignment_added), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), ClassworkActivity.class);
                        intent.putExtra("id", classId);
                        startActivity(intent);

                    } else {

                        Toast.makeText(AddAssignmentActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else {

            Toast.makeText(this, getString(R.string.enter_assignment), Toast.LENGTH_LONG).show();
        }
    }

}
