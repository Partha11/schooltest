package com.example.schoolteacher;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.schoolteacher.Model.Stream;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddStreamActivity extends AppCompatActivity {

    private TextInputEditText etAddPost;
    private TextInputEditText etAddPostTitle;

    private DatabaseReference reference;

    String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stream);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        etAddPost = findViewById(R.id.etAddPost);
        etAddPostTitle = findViewById(R.id.ettAddPostTitle);

        noteId = getIntent().getStringExtra("postId");
        reference = FirebaseDatabase.getInstance().getReference("Notes").child(noteId).child("Streams");
    }

    public void AddClassPost(View view) {

        String post = Objects.requireNonNull(etAddPost.getText()).toString().trim();
        String postTitle = Objects.requireNonNull(etAddPostTitle.getText()).toString().trim();

        if (!TextUtils.isEmpty(post) && !TextUtils.isEmpty(postTitle)) {

            String postId = reference.push().getKey();
            Stream stream = new Stream();

            stream.setStreamId(postId);
            stream.setStreamTitle(postTitle);
            stream.setStreamContent(post);

            if (postId == null) {

                Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();

            } else {

                reference.child(postId).setValue(stream).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(AddStreamActivity.this, getString(R.string.post_added), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {

                        Toast.makeText(AddStreamActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else {

            Toast.makeText(this, getString(R.string.write_post), Toast.LENGTH_LONG).show();
        }
    }
}
