package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolteacher.Adapter.BlogViewHolder;
import com.example.schoolteacher.Model.Blog;
import com.example.schoolteacher.login.LoginActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Firebase instance variables
    private DatabaseReference rootRef;
    private String currentUserId;

    //post var
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private String userID;
    private boolean mProcessLike = false;
    private DatabaseReference mDatabaseLike, mDatabaseUsers;
    private FirebaseAuth mAuth;
    public ImageView postImage;

    private FirebaseUser user;
    private Drawer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

//        bottomNav.setOnNavigationItemSelectedListener(navListener);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.bottomBarItemFirst);

//        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FirstFragment()).commit();


        //Navigation drawer
        //       new DrawerBuilder().build();
//        String name = getIntent().getStringExtra("name");
//        overrideDrawerImageLoaderPicasso();

        ProfileDrawerItem headerItem = new ProfileDrawerItem()
                .withIdentifier(101)
                .withName("Name Surname")
                .withEmail("Edit Profile")
                .withTextColorRes(R.color.material_drawer_header_selection_text)
                .withIcon(R.drawable.profile1);

        //Header
        AccountHeader drawerHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(headerItem).withOnAccountHeaderListener((view, profile, currentProfile) -> {

                    Intent intent = new Intent(MainActivity.this, ProfileInfoActivity.class);
                    startActivity(intent);
                    return false;

                }).build();

//        Picasso.get().load(R.drawable.avatar).into(view);


        //secondary items
        PrimaryDrawerItem calendar = new PrimaryDrawerItem()
                .withIdentifier(11)
                .withName(R.string.drawer_item_calendar)
                .withIcon(R.drawable.ic_calendar);
        PrimaryDrawerItem attendance = new PrimaryDrawerItem()
                .withIdentifier(12)
                .withName(R.string.drawer_item_attendance)
                .withIcon(R.drawable.ic_attendance);
        PrimaryDrawerItem grades = new PrimaryDrawerItem()
                .withIdentifier(14)
                .withName(R.string.drawer_item_grades)
                .withIcon(R.drawable.ic_grades);

        //settings, help, contact items
        PrimaryDrawerItem settings = new PrimaryDrawerItem()
                .withIdentifier(97)
                .withName(R.string.drawer_item_settings)
                .withIcon(R.drawable.ic_setting);
        PrimaryDrawerItem about = new PrimaryDrawerItem()
                .withIdentifier(98)
                .withName(R.string.about)
                .withIcon(R.drawable.ic_help);
        PrimaryDrawerItem logout = new PrimaryDrawerItem()
                .withIdentifier(99)
                .withName(R.string.drawer_item_logout)
                .withIcon(R.drawable.ic_logout);


        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(drawerHeader)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(true)
                .withFullscreen(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
//                        new SectionDrawerItem(),
                        calendar,
                        attendance,
                        grades,
                        new DividerDrawerItem(),
                        settings,
                        about,
                        logout

                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {

                    if (drawerItem != null) {

                        Intent intent = null;

                        if (drawerItem.getIdentifier() == 1) {
//                                intent = new Intent(MainActivity.this, ProfileInfoActivity.class);
                        } else if (drawerItem.getIdentifier() == 11) {
                            intent = new Intent(MainActivity.this, ScheduleActivity.class);
                        } else if (drawerItem.getIdentifier() == 12) {
                            intent = new Intent(MainActivity.this, AttendanceActivity.class);
                        } else if (drawerItem.getIdentifier() == 14) {
                            intent = new Intent(MainActivity.this, GradesActivity.class);
                        } else if (drawerItem.getIdentifier() == 97) {
                            intent = new Intent(MainActivity.this, SettingsActivity.class);
                        } else if (drawerItem.getIdentifier() == 98) {
                            intent = new Intent(MainActivity.this, About.class);
                        } else if (drawerItem.getIdentifier() == 99) {
                            FirebaseAuth.getInstance().signOut();
                            sendToStart();
                            overridePendingTransition(0, 0);

                        }
                        if (intent != null) {

                            MainActivity.this.startActivity(intent);
                        }
                    }

                    return false;
                })
                .build();
        //End of Navigation drawer

        //bottom nav

        bottomNav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.bottomBarItemFirst:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.bottomBarItemSecond:
                    startActivity(new Intent(getApplicationContext(), ClassActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.bottomBarItemThird:
                    startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.bottomBarItemFourth:
                    startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                    overridePendingTransition(0,0);
                    break;
            }

            return true;
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
        startActivity(new Intent(this, NewPostActivity.class));
    }

    @Override
    public void onStart() {

        super.onStart();

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

        if (user == null) {

            sendToStart();

        } else {

            verifyExistence();
            updateUserStatus("online");
        }

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
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setImage(model.getImage());
                        viewHolder.setPostImage(model.getPostImage());
                        viewHolder.setUsername(model.getUsername());
                        viewHolder.setStatus(model.getStatus());
                        viewHolder.setTimestamp(model.getTimestamp());
                        userID = model.getUid();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                viewHolder.postImage.setOnClickListener(view -> {

                    Intent imageFullScreen = new Intent(getApplicationContext(), PhotoActivity.class);
                    imageFullScreen.putExtra("uid", list_blog_id);
                    imageFullScreen.putExtra("from", "RequestsFragment");
                    startActivity(imageFullScreen);

                });



                viewHolder.mLikeBtn.setOnClickListener(v -> {

                    mProcessLike = true;

                    mDatabase.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                });

            }

        };

        firebaseRecyclerAdapter.startListening();
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }


    private void verifyExistence() {

        rootRef.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("name").exists())) {

                    SendUserToProfileInfoActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToProfileInfoActivity() {

        Intent settingsIntent = new Intent(MainActivity.this, ProfileInfoActivity.class);
        startActivity(settingsIntent);
    }

    private void updateUserStatus(String state) {

        String saveCurrentUserTime, saveCurrentUserDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentUserDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm ss");
        saveCurrentUserTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap = new HashMap<>();

        onlineStateMap.put("time", saveCurrentUserTime);
        onlineStateMap.put("date", saveCurrentUserDate);
        onlineStateMap.put("state", state);

        currentUserId = user.getUid();
        rootRef.child("Users").child(currentUserId).child("userState")
                .updateChildren(onlineStateMap);
    }

    private void sendToStart() {

        Intent startIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(startIntent);
        finish();
        overridePendingTransition(0, 0);
    }



    /*
    @Override
    public void onBackPressed() {
        if (fragNavController.getCurrentStack().size() > 1) {
            fragNavController.pop();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    */


}

