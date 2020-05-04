package com.example.schoolteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.schoolteacher.Adapter.PostAdapter;
import com.example.schoolteacher.Model.PostClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StreamActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    List<PostClass> postList;
    ListView listView;

    AlertDialog dialog;
    String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        // bottom nav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.streamClass);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.streamClass:
                        break;

                    case R.id.classwork:
                        Intent intent = new Intent(getApplicationContext(), ClassworkActivity.class);
 //                       intent.putExtra("id",noteId );
                        startActivity(intent);
                        break;
                    case R.id.people:

                        Intent intent1 = new Intent(getApplicationContext(), PeopleActivity.class);
//                        intent1.putExtra("id",noteId );
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });
        // end bottom nav

        // Classwork

        listView = findViewById(R.id.listViewPosts);
        postList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Notes");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){

            noteId = bundle.getString("id");
            getDataForFirebase(noteId);

        }



        // fab

        FloatingActionButton fab = findViewById(R.id.fabStream);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddStreamActivity.class);
                intent.putExtra("postId", noteId);
                startActivity(intent);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PostClass postClass = postList.get(position);
                Intent intent = new Intent(getApplicationContext(), StreamScreenActivity.class);

                intent.putExtra("postId", postClass.getPostId());
                intent.putExtra("postTitle", postClass.getPostTitle());
                intent.putExtra("post", postClass.getPost());

                startActivity(intent);

            }
        });

        // end stream




    }


    public void getDataForFirebase(String noteId) {

        myRef.child(noteId).child("ClassPosts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList.clear();
                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {


                    PostClass posts = ds1.getValue(PostClass.class);

                    postList.add(posts);


                }

                Collections.reverse(postList);
                PostAdapter postAdapter = new PostAdapter(StreamActivity.this, postList);

                listView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    // item toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }







}
