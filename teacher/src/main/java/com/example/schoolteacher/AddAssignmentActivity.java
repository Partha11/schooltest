package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Model.AssignClass;
import com.example.schoolteacher.parents.DueParentsActivity;
import com.example.schoolteacher.parents.UpcomingFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAssignmentActivity extends AppCompatActivity {

    TextInputEditText etAddAssignment;
    TextInputEditText etAddAssignmentTitle;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String noteId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //



        etAddAssignment = findViewById(R.id.etAddAssignment);
        etAddAssignmentTitle = findViewById(R.id.ettAddAssignmentTitle);

        noteId = getIntent().getStringExtra("asId");
//        Intent intent = getIntent();
//        noteId = intent.getStringExtra("noteId");

        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(noteId).child("Assignments");

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


    public void AddAssign(View view) {


        String assignment = etAddAssignment.getText().toString().trim();
        String asTitle = etAddAssignmentTitle.getText().toString().trim();

        if (!TextUtils.isEmpty(assignment) && !TextUtils.isEmpty(asTitle)) {

            String asId = databaseReference.push().getKey();

            AssignClass assignClass = new AssignClass(asId, asTitle, assignment);

            databaseReference.child(asId).setValue(assignClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        Toast.makeText(AddAssignmentActivity.this, getString(R.string.assignment_added), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), ClassworkActivity.class);
                        intent.putExtra("id",noteId);
                        startActivity(intent);



                    }else {

                        Toast.makeText(AddAssignmentActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(this, getString(R.string.enter_assignment), Toast.LENGTH_LONG).show();
        }


    }

}
