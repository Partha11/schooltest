package com.example.schoolteacher.parents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.schoolteacher.MainActivity;
import com.example.schoolteacher.PhotoActivity;
import com.example.schoolteacher.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PhotoParentActivity extends AppCompatActivity {

    private Button deletePost;
    private DatabaseReference post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_parent);

        final String mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String from = getIntent().getStringExtra("from");
        final ImageView iv = findViewById(R.id.img_full_screen);
        deletePost = findViewById(R.id.delete_post);


        if(from.equals("RequestsFragment")) {

            final String mChatUser = getIntent().getStringExtra("uid");
            post = FirebaseDatabase.getInstance().getReference().child("Blog").child(mChatUser);
            post.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.d("mchatUSer +  user_id ",mCurrentUser+  "   +    "+dataSnapshot.child("user_id").getValue().toString() );
                    if(!mCurrentUser.equals(dataSnapshot.child("user_id").getValue().toString())){
                        deletePost.setVisibility(View.INVISIBLE);
                        deletePost.setEnabled(false);
                    }
                    else{
                        deletePost.setVisibility(View.VISIBLE);
                        deletePost.setEnabled(true);
                    }

                    String imgPath = dataSnapshot.child("postImage").getValue().toString();

                    Picasso.get().load(imgPath).placeholder(R.drawable.img).into(iv);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    post = FirebaseDatabase.getInstance().getReference().child("Blog").child(mChatUser);
                    post.removeValue();


                    startActivity(new Intent(PhotoParentActivity.this, ExploreParentActivity.class));


                }
            });

        }

        else if(from.equals("MessageAdapter")){
            deletePost.setEnabled(false);
            deletePost.setVisibility(View.INVISIBLE);
            File f = new File(getIntent().getStringExtra("file"));
            Log.d("File_name",f.getAbsolutePath());
            Picasso.get().load(f).placeholder(R.drawable.back).into(iv);

        }
    }
}
