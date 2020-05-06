package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Adapter.PostAdapter;
import com.example.schoolteacher.Model.Stream;
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
import java.util.Objects;

public class StreamActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DatabaseReference reference;

    private List<Stream> streams;
    private ListView listView;

    AlertDialog dialog;
    String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // bottom nav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.streamClass);

        bottomNav.setOnNavigationItemSelectedListener(item -> {

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
        });
        // end bottom nav

        // Classwork

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            noteId = bundle.getString("id");
        }

        initialize();
    }

    private void initialize() {

        streams = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Notes").child(noteId).child("Streams");

        listView = findViewById(R.id.listViewPosts);
        FloatingActionButton fab = findViewById(R.id.fabStream);

        fab.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), AddStreamActivity.class);
            intent.putExtra("postId", noteId);
            startActivity(intent);
        });

        populateListView();
        listView.setOnItemClickListener(this);
    }

    private void populateListView() {

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                streams.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    Stream stream = d.getValue(Stream.class);

                    if (stream != null) {

                        streams.add(stream);
                    }
                }

                Collections.reverse(streams);

                PostAdapter postAdapter = new PostAdapter(StreamActivity.this, streams);
                listView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Error", databaseError.getMessage());
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


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(getApplicationContext(), StreamScreenActivity.class);

        intent.putExtra("id", noteId);
        intent.putExtra("postId", streams.get(i).getStreamId());
        intent.putExtra("postTitle", streams.get(i).getStreamTitle());
        intent.putExtra("post", streams.get(i).getStreamContent());

        startActivity(intent);
    }
}
