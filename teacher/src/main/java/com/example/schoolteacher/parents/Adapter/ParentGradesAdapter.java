package com.example.schoolteacher.parents.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.schoolteacher.Model.Grade;
import com.example.schoolteacher.R;

import java.util.List;

public class ParentGradesAdapter extends ArrayAdapter<Grade> {

    private Context context;
    private List<Grade> grades;

    public ParentGradesAdapter(@NonNull Context context, @NonNull List<Grade> objects) {

        super(context, R.layout.parents_grades_item, objects);

        this.context = context;
        this.grades = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.parents_grades_item, parent, false);
        }

        TextView className = convertView.findViewById(R.id.grade_title);
        TextView parentName = convertView.findViewById(R.id.parent_name);
        TextView parentStatus = convertView.findViewById(R.id.parent_status);
        TextView gradeText = convertView.findViewById(R.id.grade_text);

        className.setText(grades.get(position).getClassName());
        parentName.setText(grades.get(position).getParentName());
        parentStatus.setText(grades.get(position).getParentStatus());
        gradeText.setText(grades.get(position).getGrade());

        return convertView;
    }
}
