package com.example.schoolteacher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.schoolteacher.Model.Contact;
import com.example.schoolteacher.R;

import java.util.List;

public class AttendanceAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private List<Contact> contacts;

    public AttendanceAdapter(@NonNull Context context, @NonNull List<Contact> objects) {

        super(context, R.layout.attendance_item, objects);

        this.context = context;
        this.contacts = objects;
    }

    public List<Contact> getContacts() {

        return contacts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.parents_attendance_item, parent, false);
        }

        TextView className = convertView.findViewById(R.id.class_title);
        TextView parentName = convertView.findViewById(R.id.parent_name);
        TextView parentStatus = convertView.findViewById(R.id.parent_status);
        CheckBox checkBox = convertView.findViewById(R.id.attMarker);

        className.setText(contacts.get(position).getClassName());
        parentName.setText(contacts.get(position).getName());
        parentStatus.setText(contacts.get(position).getStatus());
        contacts.get(position).setPresent(checkBox.isChecked());

        checkBox.setOnCheckedChangeListener((compoundButton, b) -> contacts.get(position).setPresent(b));

        return convertView;
    }
}
