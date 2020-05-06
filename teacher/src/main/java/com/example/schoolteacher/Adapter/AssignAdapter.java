package com.example.schoolteacher.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.schoolteacher.Model.Assignment;
import com.example.schoolteacher.R;

import java.util.List;

public class AssignAdapter extends ArrayAdapter<Assignment> {

    private Activity context;
    private List<Assignment> assignments;

    public AssignAdapter( Activity context, List<Assignment> assignments) {

        super(context, R.layout.assignment_item, assignments);

        this.context = context;
        this.assignments = assignments;
    }

    @Override
    public int getCount() {

        assignments.size();
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.assignment_item, parent, false);
        }

        TextView tvCustomAssignmentTitle = convertView.findViewById(R.id.tvCustomAssignmentTitle);
        TextView tvCustomAssignment = convertView.findViewById(R.id.tvCustomAssignment);

        tvCustomAssignmentTitle.setText(assignments.get(position).getAssignmentTitle());
        tvCustomAssignment.setText(assignments.get(position).getAssignmentDescription());

        return convertView;
    }
}
