package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.schoolteacher.Adapter.NoteAdapter;
import com.example.schoolteacher.Model.ClassModel;
import com.example.schoolteacher.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private List<ClassModel> classList;

    private ListView listView;
    private TextView tvNoteCount;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportActionBar();
        setSupportActionBar(toolbar);
        setTitle("Classes");

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // bottom nav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.bottomBarItemSecond);

        bottomNav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.bottomBarItemFirst:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    break;
                case R.id.bottomBarItemSecond:

                    break;
                case R.id.bottomBarItemThird:
                    startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                    overridePendingTransition(0,0);
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
                            intent = new Intent(ClassActivity.this, ProfileInfoActivity.class);
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
                            intent = new Intent(ClassActivity.this, Settings.class);
                        } else if (drawerItem.getIdentifier() == 98) {
                            intent = new Intent(ClassActivity.this, About.class);
                        } else if (drawerItem.getIdentifier() == 99) {
                            FirebaseAuth.getInstance().signOut();
                            sendToStart();
                            overridePendingTransition(0, 0);

                        }
                        if (intent != null) {
                            ClassActivity.this.startActivity(intent);
                        }
                    }

                    return false;
                })
                .build();

        initialize();
    }

    private void initialize() {

        listView = findViewById(R.id.listView);
        tvNoteCount = findViewById(R.id.tvNoteCount);

        classList = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Notes");

        if (user == null) {

            sendToStart();
            return;
        }

        fetchClasses();

        listView.setOnItemClickListener((parent, view, position, noteId) -> {

            ClassModel mClass = classList.get(position);
            Intent intent = new Intent(getApplicationContext(), ClassworkActivity.class);

            intent.putExtra("id", mClass.getClassId());
            intent.putExtra("noteTitle", mClass.getClassName());
            intent.putExtra("note", mClass.getDescription());

            startActivity(intent);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AddNoteActivity.class)));
    }

    private void fetchClasses() {

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int count = 0;
                String classText;

                classList.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    ClassModel mClass = d.getValue(ClassModel.class);

                    if (mClass != null && mClass.getCreatedBy().equals(user.getEmail())) {

                        classList.add(mClass);
                    }
                }

                Collections.reverse(classList);
                NoteAdapter noteAdapter = new NoteAdapter(ClassActivity.this, classList);

                classText = count + " " + getString(R.string.classes);

                tvNoteCount.setText(classText);
                listView.setAdapter(noteAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendToStart() {

        Intent startIntent = new Intent(ClassActivity.this, LoginActivity.class);
        startActivity(startIntent);
        finishAffinity();
    }
}