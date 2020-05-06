package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Model.Stream;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class StreamScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText etUpdatePostTitle;
    private TextInputEditText etUpdatePost;

    private DatabaseReference reference;

    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initialize();
    }

    private void initialize() {

        etUpdatePostTitle = findViewById(R.id.etUpdatePostTitle);
        etUpdatePost = findViewById(R.id.etUpdatePost);
        Button btnUpdatePost = findViewById(R.id.btnUpdatePost);
        Button btnDeletePost = findViewById(R.id.btnDeletePost);

        Intent intent = getIntent();

        if (intent != null) {

            etUpdatePostTitle.setText(intent.getStringExtra("postTitle"));
            etUpdatePost.setText(intent.getStringExtra("post"));

            postId = intent.getStringExtra("postId");
            String noteId = intent.getStringExtra("id");

            if (noteId != null) {

                reference = FirebaseDatabase.getInstance().getReference("Notes").child(noteId).child("Streams").child(postId);

            } else {

                reference = null;
            }
        }

        btnUpdatePost.setOnClickListener(this);
        btnDeletePost.setOnClickListener(this);
    }

    private void updatePost(String postId, String postTitle, String post) {

        if (reference != null) {

            Stream stream = new Stream();

            stream.setStreamId(postId);
            stream.setStreamTitle(postTitle);
            stream.setStreamContent(post);

            reference.setValue(stream).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    Toast.makeText(this, getString(R.string.post_updated), Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deletePost() {

        reference.removeValue().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                Toast.makeText(this, getString(R.string.post_deleted), Toast.LENGTH_SHORT).show();
                finish();

            } else {

                Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onClick(View view) {

        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();

        if (view.getId() == R.id.btnUpdatePost) {

            String etUpdatePostTitles = Objects.requireNonNull(etUpdatePostTitle.getText()).toString();
            String etUpdatePosts = Objects.requireNonNull(etUpdatePost.getText()).toString();

            if (!TextUtils.isEmpty(etUpdatePosts) && !TextUtils.isEmpty(etUpdatePostTitles)) {

                updatePost(postId, etUpdatePostTitles, etUpdatePosts);

            } else {

                Toast.makeText(StreamScreenActivity.this, getString(R.string.enter_informations), Toast.LENGTH_SHORT).show();
            }

        } else if (view.getId() == R.id.btnDeletePost) {

            deletePost();
        }
    }
}
