package com.example.schoolteacher.parents;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schoolteacher.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class UpcomingFragment extends Fragment {

    String noteId;

    private View upcomingFragment;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        noteId = getArguments().getString("id");

        // Inflate the layout for this fragment
        upcomingFragment =  inflater.inflate(R.layout.fragment_upcoming, container, false);

        return upcomingFragment;
    }

}
