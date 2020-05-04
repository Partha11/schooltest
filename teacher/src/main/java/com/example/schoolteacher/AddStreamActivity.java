package com.example.schoolteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.schoolteacher.Model.PostClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStreamActivity extends AppCompatActivity {

    TextInputEditText etAddPost;
    TextInputEditText etAddPostTitle;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stream);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //


        etAddPost = findViewById(R.id.etAddPost);
        etAddPostTitle = findViewById(R.id.ettAddPostTitle);

        noteId = getIntent().getStringExtra("postId");
//        Intent intent = getIntent();
//        noteId = intent.getStringExtra("noteId");

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Notes").child(noteId).child("ClassPosts");

    }



    public void AddClassPost(View view) {


        String post = etAddPost.getText().toString().trim();
        String postTitle = etAddPostTitle.getText().toString().trim();

        if (!TextUtils.isEmpty(post) && !TextUtils.isEmpty(postTitle)) {

            String postId = myRef.push().getKey();

            PostClass postClass = new PostClass(postId, postTitle, post);

            myRef.child(postId).setValue(postClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        Toast.makeText(AddStreamActivity.this, getString(R.string.post_added), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), StreamActivity.class);
                        intent.putExtra("id",noteId);
                        startActivity(intent);



                    }else {

                        Toast.makeText(AddStreamActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(this, getString(R.string.write_post), Toast.LENGTH_LONG).show();
        }


    }




}
