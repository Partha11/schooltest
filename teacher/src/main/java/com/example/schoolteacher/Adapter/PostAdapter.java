package com.example.schoolteacher.Adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.schoolteacher.Model.Stream;
import com.example.schoolteacher.R;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Stream> {

    private Activity context;
    private List<Stream> streams;

    public PostAdapter(Activity context, List<Stream> streams) {

        super(context, R.layout.stream_item, streams);

        this.context = context;
        this.streams = streams;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.stream_item, parent, false);
        }

        TextView tvCustomPostTitle = convertView.findViewById(R.id.tvCustomPostTitle);
        TextView tvCustomPost = convertView.findViewById(R.id.tvCustomPost);

        tvCustomPostTitle.setText(streams.get(position).getStreamTitle());
        tvCustomPost.setText(streams.get(position).getStreamContent());

        return convertView;
    }
}