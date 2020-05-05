package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolteacher.Model.Contacts;
import com.example.schoolteacher.login.LoginActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private View privateChatsView;
    private RecyclerView chatsList;
    private FirebaseAuth mAuth;
    private DatabaseReference chatsRef, usersRef;
    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
//        Intent intent = new Intent(MessageActivity.this, SettingsActivity.class);
//        MessageActivity.this.startActivity(intent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.bottomBarItemThird);


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        chatsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserId);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        chatsList = findViewById(R.id.chats_list);
        chatsList.setLayoutManager(new LinearLayoutManager(MessageActivity.this));


        bottomNav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.bottomBarItemFirst:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    break;
                case R.id.bottomBarItemSecond:
                    startActivity(new Intent(getApplicationContext(), ClassActivity.class));
                    overridePendingTransition(0,0);
                    break;
                case R.id.bottomBarItemThird:
                    break;
                case R.id.bottomBarItemFourth:
                    startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                    overridePendingTransition(0,0);
                    break;
            }

            return true;
        });


        //Navigation drawer
        new DrawerBuilder().withActivity(this).build();

        //primary items
        PrimaryDrawerItem profile = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.drawer_item_name)
                .withDescription("Edit Profile")
                .withDescriptionTextColorRes(R.color.black_overlay)
                .withIcon(R.drawable.ic_account_circle);

        //secondary items
        SecondaryDrawerItem calendar = new SecondaryDrawerItem()
                .withIdentifier(11)
                .withName(R.string.drawer_item_calendar)
                .withIcon(R.drawable.ic_calendar);
        SecondaryDrawerItem attendance = new SecondaryDrawerItem()
                .withIdentifier(12)
                .withName(R.string.drawer_item_attendance)
                .withIcon(R.drawable.ic_attendance);
        SecondaryDrawerItem whatsdue = new SecondaryDrawerItem()
                .withIdentifier(13)
                .withName(R.string.drawer_item_due)
                .withIcon(R.drawable.ic_assignment);
        SecondaryDrawerItem grades = new SecondaryDrawerItem()
                .withIdentifier(14)
                .withName(R.string.drawer_item_grades)
                .withIcon(R.drawable.ic_grades);
        SecondaryDrawerItem folders = new SecondaryDrawerItem()
                .withIdentifier(15)
                .withName(R.string.drawer_item_folders)
                .withIcon(R.drawable.ic_folder);

        //settings, help, contact items
        SecondaryDrawerItem settings = new SecondaryDrawerItem()
                .withIdentifier(97)
                .withName(R.string.drawer_item_settings)
                .withIcon(R.drawable.ic_settings);
        SecondaryDrawerItem help = new SecondaryDrawerItem()
                .withIdentifier(98)
                .withName(R.string.drawer_item_help)
                .withIcon(R.drawable.ic_help);
        SecondaryDrawerItem logout = new SecondaryDrawerItem()
                .withIdentifier(99)
                .withName(R.string.drawer_item_logout)
                .withIcon(R.drawable.ic_logout);



        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(false)
                .withFullscreen(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        profile,
                        new SectionDrawerItem(),
                        calendar,
                        attendance,
                        whatsdue,
                        grades,
                        folders,
                        new DividerDrawerItem(),
                        settings,
                        help,
                        logout

                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {

                    if (drawerItem != null) {
                        Intent intent = null;
                        if (drawerItem.getIdentifier() == 1) {
                            intent = new Intent(MessageActivity.this, ProfileInfoActivity.class);
                        } else if (drawerItem.getIdentifier() == 2) {
                            //intent = new Intent(MainActivity.this, Class.class);
                        } else if (drawerItem.getIdentifier() == 3) {
                            //intent = new Intent(MainActivity.this, Class.class);
                        } else if (drawerItem.getIdentifier() == 11) {
                            //intent = new Intent(MainActivity.this, Class.class);
                        } else if (drawerItem.getIdentifier() == 12) {
                            //intent = new Intent(MainActivity.this, Class.class);
                        } else if (drawerItem.getIdentifier() == 13) {
                            //intent = new Intent(MainActivity.this, Class.class);
                        } else if (drawerItem.getIdentifier() == 97) {
                            intent = new Intent(MessageActivity.this, Settings.class);
                        } else if (drawerItem.getIdentifier() == 98) {
                            intent = new Intent(MessageActivity.this, About.class);
                        } else if (drawerItem.getIdentifier() == 99) {
                            FirebaseAuth.getInstance().signOut();
                            sendToStart();
                            overridePendingTransition(0, 0);

                        }
                        if (intent != null) {
                            MessageActivity.this.startActivity(intent);
                        }
                    }

                    return false;
                })
                .build();
        //End of Navigation drawer


    }

    private void sendToStart() {
        Intent startIntent = new Intent(MessageActivity.this, LoginActivity.class);
        startActivity(startIntent);
        finish();
    }


    // item

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.requests:
                Intent intent2 = new Intent(this, RequestsActivity.class);
                startActivity(intent2);
                return true;

            case R.id.main_find_friends_option:
                Intent intent1 = new Intent(this, FindFriendActivity.class);
                startActivity(intent1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // start message activ
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(chatsRef, Contacts.class)
                .build();


        FirebaseRecyclerAdapter<Contacts, ChatsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, ChatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, int position, @NonNull Contacts model) {
                        final String userIds = getRef(position).getKey();
                        final String [] profileImage = {"default"};
                        usersRef.child(userIds).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("image")){
                                    profileImage[0] = dataSnapshot.child("image").getValue().toString();
                                    Picasso.get().load(profileImage[0]).placeholder(R.drawable.avatar).into(holder.profileImage);
                                }

                                final String userName = dataSnapshot.child("name").getValue().toString();
                                final String userStatus = dataSnapshot.child("status").getValue().toString();
                                holder.userName.setText(userName);

                                if(dataSnapshot.child("userState").hasChild("state")){
                                    String state = dataSnapshot.child("userState").child("state").getValue().toString();
                                    String date = dataSnapshot.child("userState").child("date").getValue().toString();
                                    String time = dataSnapshot.child("userState").child("time").getValue().toString();

                                    if(state.equals("online")){
                                        holder.userStatus.setText("Online");
                                        holder.userOnlineStatus.setImageResource(R.drawable.online);
                                    }
                                    else if(state.equals("offline")){
                                        holder.userStatus.setText("Last Active\n"+ date + " " + time);
                                        holder.userOnlineStatus.setImageResource(R.drawable.offline);
                                    }
                                }
                                else{
                                    holder.userStatus.setText("Offline");
                                    holder.userOnlineStatus.setImageResource(R.drawable.offline);
                                }


                                holder.itemView.setOnClickListener(v -> {

                                    Intent chatIntent = new Intent(MessageActivity.this, ChatsActivity.class);
                                    chatIntent.putExtra("visitUserId", userIds);
                                    chatIntent.putExtra("visitUserName", userName);
                                    chatIntent.putExtra("status", userStatus);
                                    chatIntent.putExtra("visitUserImage", profileImage[0]);
                                    startActivity(chatIntent);
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        return new ChatsViewHolder(view);
                    }
                };
        chatsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileImage;
        TextView userStatus, userName;
        ImageView userOnlineStatus;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            userStatus = itemView.findViewById(R.id.users_status);
            userName = itemView.findViewById(R.id.users_profile_name);
            userOnlineStatus = itemView.findViewById(R.id.user_online_status);
        }
    }


}
