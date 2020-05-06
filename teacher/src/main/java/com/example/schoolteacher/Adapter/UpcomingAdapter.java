package com.example.schoolteacher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.schoolteacher.Model.Assignment;
import com.example.schoolteacher.R;

import java.util.List;

public class UpcomingAdapter extends ArrayAdapter<Assignment> {

    private Context context;
    private List<Assignment> assignments;

    public UpcomingAdapter(@NonNull Context context, @NonNull List<Assignment> objects) {

        super(context, R.layout.parents_upcoming_item, objects);

        this.context = context;
        this.assignments = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.parents_upcoming_item, parent, false);
        }

        TextView assignmentTitle = convertView.findViewById(R.id.tvCustomAssignmentTitle);
        TextView assignmentBody = convertView.findViewById(R.id.tvCustomAssignment);

        assignmentTitle.setText(assignments.get(position).getAssignmentTitle());
        assignmentBody.setText(assignments.get(position).getAssignmentDescription());

        return convertView;
    }
}
