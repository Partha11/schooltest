package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Model.AssignClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AssignmentScreenActivity extends AppCompatActivity {

    private TextInputEditText etUpdateAssignmentTitle;
    private TextInputEditText etUpdateAssignment;

    private DatabaseReference reference = null;

    private String assignmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assign);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Intent intent = getIntent();

        etUpdateAssignmentTitle = findViewById(R.id.etUpdateAssignmentTitle);
        etUpdateAssignment = findViewById(R.id.etUpdateAssignment);

        Button btnUpdateAssignment = findViewById(R.id.btnUpdateAssingment);
        Button btnDeleteAssignment = findViewById(R.id.btnDeleteAssignment);

        etUpdateAssignmentTitle.setText(intent.getStringExtra("assignmentTitle"));
        etUpdateAssignment.setText(intent.getStringExtra("assignment"));

        assignmentId = intent.getStringExtra("asId");
        String classId = getIntent().getStringExtra("classId");

        if (classId != null) {

            reference = FirebaseDatabase.getInstance().getReference("Notes").child(classId).child("Assignments");

        } else {

            Toast.makeText(this, "No matching classes found", Toast.LENGTH_SHORT).show();
        }

        btnUpdateAssignment.setOnClickListener(v -> {

            String etUpdateAssignmentTitles = Objects.requireNonNull(etUpdateAssignmentTitle.getText()).toString();
            String etUpdateAssignments = Objects.requireNonNull(etUpdateAssignment.getText()).toString();

            if (!TextUtils.isEmpty(etUpdateAssignments) && !TextUtils.isEmpty(etUpdateAssignmentTitles)) {

                updateAssignment(assignmentId, etUpdateAssignmentTitles, etUpdateAssignments);
                finish();

            } else {

                Toast.makeText(AssignmentScreenActivity.this, getString(R.string.enter_informations), Toast.LENGTH_SHORT).show();
            }

        });

        btnDeleteAssignment.setOnClickListener(v -> {

            deleteAssignment(assignmentId);
            finish();
        });
    }

    private void updateAssignment(String assignmentId, String asTitle, String assignment) {

        if (reference != null) {

            AssignClass assignmentClass = new AssignClass(assignmentId, asTitle, assignment);
            reference.child(assignmentId).setValue(assignmentClass);
        }

        Toast.makeText(this, getString(R.string.assignment_updated), Toast.LENGTH_SHORT).show();
    }

    private void deleteAssignment(String assignmentId) {

        if (reference != null) {

            reference.child(assignmentId).removeValue();
            Toast.makeText(this, getString(R.string.assignment_deleted), Toast.LENGTH_SHORT).show();
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
