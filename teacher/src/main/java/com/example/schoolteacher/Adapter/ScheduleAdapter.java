package com.example.schoolteacher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.schoolteacher.Model.Schedule;
import com.example.schoolteacher.R;

import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Schedule> {

    private Context context;
    private List<Schedule> schedules;

    public ScheduleAdapter(@NonNull Context context, @NonNull List<Schedule> objects) {

        super(context, R.layout.parents_schedule_item, objects);

        this.context = context;
        this.schedules = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.parents_schedule_item, parent, false);
        }

        TextView className = convertView.findViewById(R.id.schedule_class);
        TextView subjectName = convertView.findViewById(R.id.schedule_subject);
        TextView classTime = convertView.findViewById(R.id.schedule_time);

        className.setText(schedules.get(position).getClassName());
        subjectName.setText(schedules.get(position).getSubjectName());
        classTime.setText(schedules.get(position).getClassTime());

        return convertView;
    }
}
