package com.example.schoolteacher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.schoolteacher.Model.Stream;
import com.example.schoolteacher.R;

import java.util.List;

public class DueAdapter extends ArrayAdapter<Stream> {

    private Context context;
    private List<Stream> streams;

    public DueAdapter(@NonNull Context context, @NonNull List<Stream> objects) {

        super(context, R.layout.parents_updates_item, objects);

        this.context = context;
        this.streams = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.parents_updates_item, parent, false);
        }

        TextView assignmentTitle = convertView.findViewById(R.id.tvCustomPostTitle);
        TextView assignmentBody = convertView.findViewById(R.id.tvCustomPost);

        assignmentTitle.setText(streams.get(position).getStreamTitle());
        assignmentBody.setText(streams.get(position).getStreamContent());

        return convertView;
    }
}
