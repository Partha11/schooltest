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

import com.example.schoolteacher.Model.NoteClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AddNoteActivity extends AppCompatActivity {

    EditText etAddNote;
    EditText etAddNoteTitle;
    TextView tvAddNoteCreatedDate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    String userID;

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

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Users").child(userID).child("Notes");


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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        tvAddNoteCreatedDate.setText(simpleDateFormat.format(createdDateNote));

        String note = etAddNote.getText().toString().trim();
        String noteTitle = etAddNoteTitle.getText().toString().trim();
        String createdDate = tvAddNoteCreatedDate.getText().toString();


        if (!TextUtils.isEmpty(note) && !TextUtils.isEmpty(noteTitle)) {

            String noteId = myRef.push().getKey();

            NoteClass noteClass = new NoteClass(noteId, noteTitle, note, createdDate);

            myRef.child(noteId).setValue(noteClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                     if (task.isSuccessful()){

                         Toast.makeText(AddNoteActivity.this, getString(R.string.class_added), Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
                         startActivity(intent);


                     }else {

                         Toast.makeText(AddNoteActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                     }
                }
            });


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
