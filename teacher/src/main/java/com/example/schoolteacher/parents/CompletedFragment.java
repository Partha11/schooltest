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

import com.example.schoolteacher.Adapter.DueAdapter;
import com.example.schoolteacher.Model.ClassModel;
import com.example.schoolteacher.Model.Stream;
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

public class CompletedFragment extends Fragment {

    private ListView listView;
    private Context context;

    private DueAdapter adapter;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        listView = view.findViewById(R.id.due_list_view);

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {

            return;
        }

        List<String> classIds = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();

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

    private void loadClassAttendance(List<ClassModel> classes) {

        List<Stream> streams = new ArrayList<>();

        for (ClassModel c : classes) {

            reference.child("Notes").child(c.getClassId()).child("Streams")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot d : dataSnapshot.getChildren()) {

                        Stream stream = d.getValue(Stream.class);

                        if (stream != null) {

                            streams.add(stream);
                        }
                    }

                    adapter = new DueAdapter(context, streams);
                    listView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
