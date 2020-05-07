package com.example.schoolteacher;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.schoolteacher.Adapter.GradeAdapter;
import com.example.schoolteacher.Model.ClassModel;
import com.example.schoolteacher.Model.Contact;
import com.example.schoolteacher.Model.Grade;
import com.example.schoolteacher.Model.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GradesActivity extends AppCompatActivity {

    private Spinner spinner;
    private ListView listView;

    private DatabaseReference reference;

    private ArrayAdapter<String> adapter;
    private GradeAdapter gradeAdapter;

    private List<ClassModel> classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initialize();
    }

    private void initialize() {

        Button button = findViewById(R.id.loadButton);
        Button saveGrade = findViewById(R.id.buttonSaveGrade);

        spinner = findViewById(R.id.gradesSpinner);
        listView = findViewById(R.id.gradesListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        classes = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();

        spinner.setAdapter(adapter);
        button.setOnClickListener(v -> populateListView());
        saveGrade.setOnClickListener(v -> updateGrades());
        populateSpinner();
    }

    private void updateGrades() {

        if (gradeAdapter != null) {

            List<Contact> contacts = gradeAdapter.getContacts();
            String key = reference.push().getKey();

            if (key == null) {

                return;
            }

            Grade grade = new Grade(key, String.valueOf(System.currentTimeMillis()));
            HashMap<String, String> grades = new HashMap<>();

            for (Contact contact: contacts) {

                grades.put(contact.getUserId(), contact.getGrade());
            }

            grade.setGrades(grades);
            reference.child("Notes").child(classes.get(spinner.getSelectedItemPosition()).getClassId())
                    .child("Grades").child(key).setValue(grade);
        }
    }

    private void populateListView() {

        ClassModel classModel = classes.get(spinner.getSelectedItemPosition());
        List<Member> members = new ArrayList<>();

        reference.child("Notes").child(classModel.getClassId()).child("memberList").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    Member member = d.getValue(Member.class);
                    members.add(member);
                }

                fetchMember(members);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populateSpinner() {

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
                            adapter.add(mClass.getClassName());
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

    private void fetchMember(List<Member> members) {

        List<Contact> contacts = new ArrayList<>();

        for (Member member : members) {

            reference.child("Users").child(member.getMemberId()).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Contact contact = dataSnapshot.getValue(Contact.class);

                    if (contact != null) {

                        contact.setClassName(classes.get(spinner.getSelectedItemPosition()).getClassName());
                        contact.setUserId(dataSnapshot.getKey());
                        contacts.add(contact);
                    }

                    gradeAdapter = new GradeAdapter(GradesActivity.this, contacts);
                    listView.setAdapter(gradeAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.d("Error", databaseError.getMessage());
                }
            });
        }
    }
}
