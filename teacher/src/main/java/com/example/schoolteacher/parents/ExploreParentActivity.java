package com.example.schoolteacher.parents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.schoolteacher.Adapter.BlogViewHolder;
import com.example.schoolteacher.Model.Blog;
import com.example.schoolteacher.NewPostActivity;
import com.example.schoolteacher.PhotoActivity;
import com.example.schoolteacher.R;
import com.example.schoolteacher.parents.Adapter.SectionPagerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ExploreParentActivity extends AppCompatActivity {

    //post var
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private String userID;
    private boolean mProcessLike = false;
    private DatabaseReference mDatabaseLike, mDatabaseUsers;
    private FirebaseAuth mAuth;
    public ImageView postImage;
    ImageButton addPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_parent);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        // bottom nav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_explore);

        //bottom nav

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_class:
                        startActivity(new Intent(getApplicationContext(), MainParentsActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_due:
                        startActivity(new Intent(getApplicationContext(), DueParentsActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_messages:
                        startActivity(new Intent(getApplicationContext(), MessageParentActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_explore:
                        break;
                    case R.id.navigation_notifications:
                        startActivity(new Intent(getApplicationContext(), RequestParentActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }

                return true;
            }
        });




        // post
        postImage = findViewById(R.id.post_image);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        mDatabase.keepSynced(true);
        mDatabaseUsers.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mBlogList = findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(linearLayoutManager);



    }



    public void AddPost(View v) {
        startActivity(new Intent(this, NewPostParentActivity.class));
    }


    @Override
    public void onStart() {
        super.onStart();


        Query conversationQuery = mDatabase.orderByChild("timestamp");

        FirebaseRecyclerOptions<Blog> options = new FirebaseRecyclerOptions.Builder<Blog>()
                .setQuery(conversationQuery, Blog.class)
                .build();

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(options) {
            @NonNull
            @Override
            public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_single_layout, parent, false);
                mAuth = FirebaseAuth.getInstance();
                return new BlogViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final BlogViewHolder viewHolder, int position, @NonNull final Blog model) {

                viewHolder.setContext(getApplicationContext());
                final String list_blog_id = getRef(position).getKey();
                viewHolder.setLikeBtns(list_blog_id);

                Query lastMessageQuery = mDatabase.child(list_blog_id).limitToLast(1);
                lastMessageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setImage(model.getImage());
                        viewHolder.setPostImage(model.getPostImage());
                        viewHolder.setUsername(model.getUsername());
                        viewHolder.setStatus(model.getStatus());
                        viewHolder.setTimestamp(model.getTimestamp());
                        userID = model.getUid();



                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                viewHolder.postImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent imageFullScreen = new Intent(getApplicationContext(), PhotoParentActivity.class);
                        imageFullScreen.putExtra("uid", list_blog_id);
                        imageFullScreen.putExtra("from", "RequestsFragment");
                        startActivity(imageFullScreen);

                    }
                });



                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        mProcessLike = true;

                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mProcessLike) {

                                    if (dataSnapshot.child(list_blog_id).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child(list_blog_id).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcessLike = false;

                                    } else {
                                        mDatabaseLike.child(list_blog_id).child(mAuth.getCurrentUser().getUid()).setValue("Lliked");
                                        mProcessLike = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

            }

        };

        firebaseRecyclerAdapter.startListening();
        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }








}
