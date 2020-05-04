package com.example.schoolteacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Model.NoteClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoteScreenActivity extends AppCompatActivity {

    List<NoteClass> noteList;

    TextInputEditText etUpdateNoteTitle;
    TextInputEditText etUpdateNote;
    TextView tvUpdateDate;
    Button btnUpdate;
    Button btnDelete;

    String noteId;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_delete_note_);

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

        Intent intent = getIntent();

        etUpdateNoteTitle = findViewById(R.id.etUpdateNoteTitle);
        etUpdateNote = findViewById(R.id.etUpdateNote);
        tvUpdateDate = findViewById(R.id.updatedDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);


        etUpdateNoteTitle.setText(intent.getStringExtra("noteTitle"));
        etUpdateNote.setText(intent.getStringExtra("note"));

        noteId = intent.getStringExtra("noteId");


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date updateDateNote = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                tvUpdateDate.setText(simpleDateFormat.format(updateDateNote));

                String etUpdateNoteTitles = etUpdateNoteTitle.getText().toString();
                String etUpdateNotes = etUpdateNote.getText().toString();
                String tvUpdateDates = getString(R.string.date_of_update) + tvUpdateDate.getText().toString();

                if (!TextUtils.isEmpty(etUpdateNotes) && !TextUtils.isEmpty(etUpdateNoteTitles)) {
                    updateNote(noteId, etUpdateNoteTitles, etUpdateNotes, tvUpdateDates);

                    Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                } else {
                    Toast.makeText(NoteScreenActivity.this, getString(R.string.enter_informations), Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteNote(noteId);
                Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });

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


    private boolean updateNote(String noteId, String noteTitle, String note, String createdDate) {

        Intent i = new Intent(NoteScreenActivity.this, AssignmentScreenActivity.class);
        i.putExtra("id", noteId);

        Intent intent = new Intent(NoteScreenActivity.this, AddAssignmentActivity.class);
        intent.putExtra("id", noteId);

        Intent noteIntent = new Intent(NoteScreenActivity.this, ClassworkActivity.class);
        noteIntent.putExtra("id", noteId);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id", noteId); //InputString: from the EditText
        editor.putString("noteTitle", noteTitle); //InputString: from the EditText
        editor.apply();


 //       Intent intent1 = new Intent(NoteScreenActivity.this, AddStreamActivity.class);
 //       intent1.putExtra("id", noteId);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Notes").child(noteId);

        NoteClass noteClass = new NoteClass(noteId, noteTitle, note, createdDate);
        reference.setValue(noteClass);

        Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show();

        return true;
    }

    private boolean deleteNote(String id) {
        //noteList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(id);
        reference.removeValue();

        Toast.makeText(this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();

        return true;
    }
}
