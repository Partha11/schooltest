package com.example.schoolteacher.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.schoolteacher.Model.AssignClass;
import com.example.schoolteacher.R;

import java.util.List;

public class AssignAdapter extends ArrayAdapter<AssignClass> {

    private Activity context;
    List<AssignClass> assignments;

    public AssignAdapter( Activity context, List<AssignClass> assignments) {
        super(context, R.layout.assignment_item, assignments);
        this.context = context;
        this.assignments = assignments;
    }

    @Override
    public int getCount() {
        assignments.size();
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.assignment_item, null, true);

        TextView tvCustomAssignmentTitle = (TextView) customView.findViewById(R.id.tvCustomAssignmentTitle);
        TextView tvCustomAssignment = (TextView) customView.findViewById(R.id.tvCustomAssignment);

        AssignClass assigment = assignments.get(position);
        tvCustomAssignmentTitle.setText(assigment.getAsTitle());
        tvCustomAssignment.setText(assigment.getAssignment());


        return customView;
    }
}
