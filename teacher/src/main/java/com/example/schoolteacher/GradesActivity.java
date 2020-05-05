package com.example.schoolteacher;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.schoolteacher.Model.ClassModel;
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

public class GradesActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;

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

        Spinner spinner = findViewById(R.id.gradesSpinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        classes = new ArrayList<>();

        spinner.setAdapter(adapter);
        populateSpinner();
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
}
