package com.example.schoolteacher;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.schoolteacher.Model.PostClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StreamScreenActivity extends AppCompatActivity {

    List<PostClass> postList;

    TextInputEditText etUpdatePostTitle;
    TextInputEditText etUpdatePost;
    Button btnUpdatePost;
    Button btnDeletePost;

    String postId;
    String id;
    String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        Intent intent = getIntent();

        etUpdatePostTitle = findViewById(R.id.etUpdatePostTitle);
        etUpdatePost = findViewById(R.id.etUpdatePost);
        btnUpdatePost = findViewById(R.id.btnUpdatePost);
        btnDeletePost = findViewById(R.id.btnDeletePost);


        etUpdatePostTitle.setText(intent.getStringExtra("postTitle"));
        etUpdatePost.setText(intent.getStringExtra("post"));

        postId = intent.getStringExtra("postId");

        btnUpdatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String etUpdatePostTitles = etUpdatePostTitle.getText().toString();
                String etUpdatePosts = etUpdatePost.getText().toString();

                if (!TextUtils.isEmpty(etUpdatePosts) && !TextUtils.isEmpty(etUpdatePostTitles)) {
                    updatePost(postId, etUpdatePostTitles, etUpdatePosts);

                    Intent intent = new Intent(getApplicationContext(), StreamActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(StreamScreenActivity.this, getString(R.string.enter_informations), Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deletePost(postId);
                Intent intent = new Intent(getApplicationContext(), StreamActivity.class);
                startActivity(intent);

            }
        });


    }


    private boolean updatePost(String postid, String postTitle, String post) {

        String noteId = getIntent().getStringExtra("id");
//        Intent intent = getIntent();
//        noteId = intent.getStringExtra("noteId");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(noteId).child("ClassPosts").child(postid);

        PostClass postClass = new PostClass(postid, postTitle, post);
        reference.setValue(postClass);

        Toast.makeText(this, getString(R.string.post_updated), Toast.LENGTH_SHORT).show();

        return true;
    }

    private boolean deletePost(String postid) {

        //Intent intent = getIntent();
        String id = getIntent().getStringExtra("id");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes").child(id).child("ClassPosts").child(postid);
        reference.removeValue();

        Toast.makeText(this, getString(R.string.post_deleted), Toast.LENGTH_SHORT).show();

        return true;
    }


    // toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }



}
