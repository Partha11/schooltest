package com.example.schoolteacher.Adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.schoolteacher.Model.PostClass;
import com.example.schoolteacher.R;

import java.util.List;

public class PostAdapter extends ArrayAdapter<PostClass> {

    private Activity context;
    List<PostClass> posts;

    public PostAdapter(Activity context, List<PostClass> posts) {
        super(context, R.layout.stream_item, posts);
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        posts.size();
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.stream_item, null, true);

        TextView tvCustomPostTitle = (TextView) customView.findViewById(R.id.tvCustomPostTitle);
        TextView tvCustomPost = (TextView) customView.findViewById(R.id.tvCustomPost);

        PostClass post = posts.get(position);
        tvCustomPostTitle.setText(post.getPostTitle());
        tvCustomPost.setText(post.getPost());


        return customView;
    }
}