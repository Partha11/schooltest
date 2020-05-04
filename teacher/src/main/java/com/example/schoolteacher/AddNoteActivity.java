package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Model.Attendance;
import com.example.schoolteacher.Model.ClassModel;
import com.example.schoolteacher.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    private EditText etAddNote;
    private EditText etAddNoteTitle;
    private TextView tvAddNoteCreatedDate;

    private FirebaseUser currentUser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

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


        etAddNote = findViewById(R.id.etAddNote);
        etAddNoteTitle = findViewById(R.id.ettAddNoteTitle);
        tvAddNoteCreatedDate = findViewById(R.id.tvAddNoteCreatedDate);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Notes");

        if (currentUser == null) {

            finishAffinity();
            startActivity(new Intent(this, LoginActivity.class));
        }
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


    public void add(View view) {

        Date createdDateNote = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);

        tvAddNoteCreatedDate.setText(sdf.format(createdDateNote));

        String classDescription = etAddNote.getText().toString().trim();
        String classTitle = etAddNoteTitle.getText().toString().trim();

        if (!TextUtils.isEmpty(classDescription) && !TextUtils.isEmpty(classTitle)) {

            String classId = reference.push().getKey();
            ClassModel mClass = new ClassModel();

            mClass.setClassId(classId);
            mClass.setClassName(classTitle);
            mClass.setDescription(classDescription);
            mClass.setCreatedBy(currentUser.getEmail());
            mClass.setCreatedAt(sdf.format(createdDateNote));
            mClass.setClassFollowers(0);

            if (classId == null) {

                Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();

            } else {

                reference.child(classId).setValue(mClass).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(AddNoteActivity.this, getString(R.string.class_added), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(AddNoteActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } else {

            Toast.makeText(this, getString(R.string.enter_class), Toast.LENGTH_LONG).show();
        }
    }

    /*private void writeNewNote(String noteId, String note, String createdDate) {

        NoteClass notes = new NoteClass(nonote,createdDate);


        notes.setNote(note);
        notes.setNote(createdDate);

        myRef.child("Notes").child(noteId).child("createdDate").setValue(createdDate);
        myRef.child("Notes").child(noteId).child("noteContent").setValue(note);

    }*/
}
