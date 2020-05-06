package com.example.schoolteacher.parents;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.schoolteacher.Adapter.AttendanceAdapter;
import com.example.schoolteacher.Model.Attendance;
import com.example.schoolteacher.Model.ClassModel;
import com.example.schoolteacher.Model.Contact;
import com.example.schoolteacher.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendanceFragment extends Fragment {

    private Context context;

    private ListView listView;
    private AttendanceAdapter adapter;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String parentName;
    private String parentStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        this.listView = view.findViewById(R.id.attendanceLv);

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

        List<String> classIds = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();

        fetchUserInfo();

        reference.child("Users").child(user.getUid()).child("Classes").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    classIds.add(d.getKey());
                }

                loadClassInfo(classIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadClassInfo(List<String> classIds) {

        List<ClassModel> classes = new ArrayList<>();

        reference.child("Notes").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    if (classIds.contains(d.getKey())) {

                        classes.add(d.getValue(ClassModel.class));
                    }
                }

                loadClassAttendance(classes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchUserInfo() {

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

    private void loadClassAttendance(List<ClassModel> classes) {

        List<Contact> contacts = new ArrayList<>();

        for (ClassModel c : classes) {

            reference.child("Notes").child(c.getClassId()).child("Attendances")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot d : dataSnapshot.getChildren()) {

                        Attendance attendance = d.getValue(Attendance.class);

                        if (attendance != null && attendance.getAttendance() != null &&
                                attendance.getAttendance().containsKey(user.getUid())) {

                            Contact contact = new Contact();

                            contact.setClassName(c.getClassName());
                            contact.setClassId(c.getClassId());
                            contact.setName(parentName);
                            contact.setStatus(parentStatus);
                            contact.setPresent(attendance.getAttendance().get(user.getUid()));

                            contacts.add(contact);
                        }
                    }

                    adapter = new AttendanceAdapter(context, contacts);
                    listView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
