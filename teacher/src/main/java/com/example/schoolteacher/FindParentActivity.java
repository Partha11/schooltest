package com.example.schoolteacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolteacher.Model.Contact;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindParentActivity extends AppCompatActivity {

    private RecyclerView findParentsRecyclerList;
    private DatabaseReference userRef;

    private FirebaseAuth mAuth;
    private String currentUserId;

    String userID;
    String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_parent);

//        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        noteId = getIntent().getStringExtra("id");

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        findParentsRecyclerList = findViewById(R.id.find_parent_recycler_list);
        findParentsRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Contact> options = new FirebaseRecyclerOptions.Builder<Contact>()
                .setQuery(userRef, Contact.class)
                .build();

        FirebaseRecyclerAdapter<Contact, FindParentActivity.FindParentViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contact, FindParentActivity.FindParentViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FindParentActivity.FindParentViewHolder holder,
                                            final int position, @NonNull Contact model) {

                holder.userName.setText(model.getName());
                holder.userStatus.setText(model.getStatus());

                Picasso.get().load(model.getImage()).placeholder(R.drawable.avatar)
                        .into(holder.profileImage);

                holder.itemView.setOnClickListener(v -> {

                    String visitParentUserId = getRef(position).getKey();
                    Intent profileIntent = new Intent(FindParentActivity.this, ParentProfileActivity.class);
                    profileIntent.putExtra("visitClassUserId", visitParentUserId);
                    profileIntent.putExtra("id", noteId);

                    startActivity(profileIntent);
                });
            }

            @NonNull
            @Override
            public FindParentActivity.FindParentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.users_display_layout,viewGroup, false);
                return new FindParentViewHolder(view);

            }
        };

        findParentsRecyclerList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class FindParentViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userStatus;
        CircleImageView profileImage;

        FindParentViewHolder(@NonNull View itemView) {

            super(itemView);

            userName = itemView.findViewById(R.id.users_profile_name);
            userStatus = itemView.findViewById(R.id.users_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
        }
    }
}
