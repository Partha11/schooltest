package com.example.schoolteacher.parents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.schoolteacher.FindFriendActivity;
import com.example.schoolteacher.R;
import com.example.schoolteacher.parents.Adapter.SectionPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MessageParentActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    SectionPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_parent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // tab
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        //bottom nav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_messages);


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
            }
        });



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

        adapter.addFragment(new ChatsFragment(), "Chats");
        adapter.addFragment(new RequestsFragment(), "Requests");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message_parent_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_find_friends_option:
                Intent profileIntent = new Intent(this, FindFriendActivity.class);
                startActivity(profileIntent);
                return true;

            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }




}
