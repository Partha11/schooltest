package com.example.schoolteacher.parents;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.schoolteacher.R;
import com.example.schoolteacher.login.LoginActivity;
import com.example.schoolteacher.parents.Adapter.SectionPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainParentsActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    SectionPagerAdapter adapter;

    private String mName;
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference rootRef;
    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_parents);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // tab
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // bottom nav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_class);

        //bottom nav

        bottomNav.setOnNavigationItemSelectedListener(item -> {

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
                    startActivity(new Intent(getApplicationContext(), ExploreParentActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.navigation_notifications:
                    startActivity(new Intent(getApplicationContext(), RequestParentActivity.class));
                    overridePendingTransition(0, 0);
                    break;
            }

            return true;
        });


        //    bottomNav.setOnNavigationItemSelectedListener(navListener);

//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ClassFragment()).commit();


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        // Sets default values only once, first time this is called. The third
        // argument is a boolean that indicates whether the default values
        // should be set more than once. When false, the system sets the default
        // values only the first time it is called.
        PreferenceManager.setDefaultValues(this,
                R.xml.pref_students, false);
        PreferenceManager.setDefaultValues(this,
                R.xml.pref_information, false);
        PreferenceManager.setDefaultValues(this,
                R.xml.pref_security, false);
        PreferenceManager.setDefaultValues(this,
                R.xml.pref_notifications, false);



        viewPager.setAdapter(adapter);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





    }


    // tab


    private void setupViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ScheduleFragment(), "Schedule");
        adapter.addFragment(new AttendanceFragment(), "Attendance");
        adapter.addFragment(new NotesFragment(), "Notes");

        viewPager.setAdapter(adapter);
    }






    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser == null){
            sendToStart();
        } else {
            VerifyUserExistance();
            updateUserStatus("online");
        }
    }


    private void VerifyUserExistance() {
        String currentUserID = mFirebaseAuth.getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("name").exists())){
                    SendUserToProfileInfoParentActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToProfileInfoParentActivity() {
        Intent settingsIntent = new Intent(MainParentsActivity.this, ProfileInfoParentActivity.class);
        startActivity(settingsIntent);
    }



    private void updateUserStatus(String state) {

        String saveCurrentUserTime, saveCurrentUserDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate  = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentUserDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime  = new SimpleDateFormat("hh:mm ss");
        saveCurrentUserTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap = new HashMap<>();

        onlineStateMap.put("time", saveCurrentUserTime);
        onlineStateMap.put("date", saveCurrentUserDate);
        onlineStateMap.put("state", state);

        currentUserId = mFirebaseUser.getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Users").child(currentUserId).child("userState")
                .updateChildren(onlineStateMap);
    }


    private void sendToStart() {

        Intent startIntent = new Intent(MainParentsActivity.this, LoginActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parents_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent profileIntent = new Intent(this,
                        com.example.schoolteacher.parents.ProfileInfoParentActivity.class);
                startActivity(profileIntent);
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this,
                        com.example.schoolteacher.parents.SettingsParentsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.menu_logout_btn:
                FirebaseAuth.getInstance().signOut();
                sendToStart();
                overridePendingTransition(0, 0);

                return true;

            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayToast(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
