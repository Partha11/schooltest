package com.example.schoolteacher;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolteacher.Model.AssignClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AssignmentScreenActivity extends AppCompatActivity {

    List<AssignClass> assignmentList;

    TextInputEditText etUpdateAssignmentTitle;
    TextInputEditText etUpdateAssignment;
    Button btnUpdateAssingment;
    Button btnDeleteAssignment;

    String asId;
    String id;
    String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assign);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        Intent intent = getIntent();

        etUpdateAssignmentTitle = findViewById(R.id.etUpdateAssignmentTitle);
        etUpdateAssignment = findViewById(R.id.etUpdateAssignment);
        btnUpdateAssingment = findViewById(R.id.btnUpdateAssingment);
        btnDeleteAssignment = findViewById(R.id.btnDeleteAssignment);


        etUpdateAssignmentTitle.setText(intent.getStringExtra("assignmentTitle"));
        etUpdateAssignment.setText(intent.getStringExtra("assignment"));

        asId = intent.getStringExtra("asId");

        btnUpdateAssingment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String etUpdateAssignmentTitles = etUpdateAssignmentTitle.getText().toString();
                String etUpdateAssignments = etUpdateAssignment.getText().toString();

                if (!TextUtils.isEmpty(etUpdateAssignments) && !TextUtils.isEmpty(etUpdateAssignmentTitles)) {
                    updateAssignment(asId, etUpdateAssignmentTitles, etUpdateAssignments);

                    Intent intent = new Intent(getApplicationContext(), ClassworkActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AssignmentScreenActivity.this, getString(R.string.enter_informations), Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnDeleteAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteAssignment(asId);
                Intent intent = new Intent(getApplicationContext(), ClassworkActivity.class);
                startActivity(intent);

            }
        });
    }

    private boolean updateAssignment(String assignmentid, String asTitle, String assignment) {

        String noteId = getIntent().getStringExtra("id");
//        Intent intent = getIntent();
//        noteId = intent.getStringExtra("noteId");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(noteId).child("Assignments").child(assignmentid);

        AssignClass assignmentClass = new AssignClass(assignmentid, asTitle, assignment);
        reference.setValue(assignmentClass);

        Toast.makeText(this, getString(R.string.assignment_updated), Toast.LENGTH_SHORT).show();

        return true;
    }

    private boolean deleteAssignment(String assignmentid) {

        //Intent intent = getIntent();
        String id = getIntent().getStringExtra("id");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(id).child("Assignments").child(assignmentid);
        reference.removeValue();

        Toast.makeText(this, getString(R.string.assignment_deleted), Toast.LENGTH_SHORT).show();

        return true;
    }


// toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
