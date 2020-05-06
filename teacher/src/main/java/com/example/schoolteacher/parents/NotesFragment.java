package com.example.schoolteacher.parents;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.schoolteacher.Model.ClassModel;
import com.example.schoolteacher.Model.Contact;
import com.example.schoolteacher.Model.Grade;
import com.example.schoolteacher.R;
import com.example.schoolteacher.parents.Adapter.ParentGradesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class NotesFragment extends Fragment {

    private ListView listView;

    private FirebaseUser user;
    private DatabaseReference reference;

    private ParentGradesAdapter adapter;
    private Context context;

    private String parentName;
    private String parentStatus;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        listView = view.findViewById(R.id.gradesLv);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        this.context = context;
    }

    private void initialize() {

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {

            return;
        }

        reference = FirebaseDatabase.getInstance().getReference();
        List<String> classes = new ArrayList<>();

        fetchParentData();

        reference.child("Users").child(user.getUid()).child("Classes").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    classes.add(d.getKey());
                }

                fetchClasses(classes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchParentData() {

        reference.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Contact contact = dataSnapshot.getValue(Contact.class);

                if (contact != null) {

                    parentName = contact.getName();
                    parentStatus = contact.getStatus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchClasses(List<String> classes) {

        List<ClassModel> classModels = new ArrayList<>();

        for (String s : classes) {

            reference.child("Notes").child(s).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ClassModel mClass = dataSnapshot.getValue(ClassModel.class);
                    classModels.add(mClass);

                    fetchGrades(classModels);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void fetchGrades(List<ClassModel> classes) {

        List<Grade> grades = new ArrayList<>();

        for (ClassModel c : classes) {

            reference.child("Notes").child(c.getClassId()).child("Grades")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot d : dataSnapshot.getChildren()) {

                        Grade grade = d.getValue(Grade.class);

                        if (grade != null) {

                            grade.setClassName(c.getClassName());
                            grade.setParentName(parentName);
                            grade.setParentStatus(parentStatus);
                            grade.setGrade(grade.getGrades().get(user.getUid()));

                            Log.d("Grade", new Gson().toJson(grade));

                            grades.add(grade);
                        }
                    }

                    adapter = new ParentGradesAdapter(context, grades);
                    listView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
