package com.example.schoolteacher.parents;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.schoolteacher.R;
import com.example.schoolteacher.parents.Adapter.SectionPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class DueParentsActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    SectionPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_parents);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // tab
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // bottom nav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_due);

        //bottom nav

        bottomNav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navigation_class:
                    startActivity(new Intent(getApplicationContext(), MainParentsActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.navigation_due:
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

        adapter.addFragment(new UpcomingFragment(), "Upcoming");
        adapter.addFragment(new CompletedFragment(), "Class Updates");

        viewPager.setAdapter(adapter);
    }




}
