package com.example.schoolteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolteacher.Model.Contacts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindParentActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView findParentsRecyclerList;
    private DatabaseReference userRef;

    private FirebaseAuth mAuth;
    private String currentUserId;

    FirebaseDatabase firebaseDatabase;

    String userID;
    String noteId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_parent);

//        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();

        noteId = getIntent().getStringExtra("id");

        userRef = firebaseDatabase.getReference()
                .child("Users");

        findParentsRecyclerList = findViewById(R.id.find_parent_recycler_list);
        findParentsRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        onStart();

    }




    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options = new
                FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(userRef, Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts, FindParentActivity.FindParentViewHolder> adapter = new
                FirebaseRecyclerAdapter<Contacts, FindParentActivity.FindParentViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindParentActivity.FindParentViewHolder holder, final int position, @NonNull Contacts model) {
                        holder.userName.setText(model.getName());
                        holder.userStatus.setText(model.getStatus());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.avatar)
                                .into(holder.profileImage);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String visitParentUserId = getRef(position).getKey();
                                Intent profileIntent = new Intent(FindParentActivity.this, ParentProfileActivity.class);
                                profileIntent.putExtra("visitClassUserId", visitParentUserId);
                                profileIntent.putExtra("id",noteId );

                                startActivity(profileIntent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FindParentActivity.FindParentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.users_display_layout,viewGroup, false);
                        FindParentActivity.FindParentViewHolder viewHolder = new FindParentViewHolder(view);
                        return viewHolder;

                    }
                };

        findParentsRecyclerList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class FindParentViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;

        public FindParentViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.users_profile_name);
            userStatus = itemView.findViewById(R.id.users_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
        }
    }



}
