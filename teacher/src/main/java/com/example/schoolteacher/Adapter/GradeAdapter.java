package com.example.schoolteacher.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.schoolteacher.Model.Contact;
import com.example.schoolteacher.R;

import java.util.List;

public class GradeAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private List<Contact> contacts;

    public GradeAdapter(@NonNull Context context, @NonNull List<Contact> objects) {

        super(context, R.layout.grades_item, objects);

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

            convertView = LayoutInflater.from(context).inflate(R.layout.grades_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.gradesName);
        EditText editText = convertView.findViewById(R.id.gradesEt);
        String name = contacts.get(position).getClassName() + "\n" + contacts.get(position).getName() +
                "\n" + contacts.get(position).getStatus();

        textView.setText(name);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence != null) {

                    contacts.get(position).setGrade(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return convertView;
    }
}
