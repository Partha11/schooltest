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

            convertView = LayoutInflater.from(context).inflate(R.layout.attendance_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.attendanceName);
        CheckBox checkBox = convertView.findViewById(R.id.attMarker);
        String name = contacts.get(position).getClassName() + "\n" + contacts.get(position).getName() +
                "\n" + contacts.get(position).getStatus();

        textView.setText(name);
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> contacts.get(position).setPresent(b));

        return convertView;
    }
}
