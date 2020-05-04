package com.example.schoolteacher.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolteacher.Model.GetTimeAgo;
import com.example.schoolteacher.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogViewHolder extends RecyclerView.ViewHolder {

    private View mView;
    private Context context;
    public ImageButton mLikeBtn;
    public ImageView postImage;
    private TextView count;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    public BlogViewHolder(View itemView) {

        super(itemView);
        this.mView = itemView;
        this.mLikeBtn = itemView.findViewById(R.id.btn_like);
        this.postImage = itemView.findViewById(R.id.post_image);
        this.count = itemView.findViewById(R.id.likes_count);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Likes");
        mAuth = FirebaseAuth.getInstance();
        databaseReference.keepSynced(true);
    }


    public void setLikeBtns(final String post_key){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long c = dataSnapshot.child(post_key).getChildrenCount();
                if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){

                    mLikeBtn.setImageResource(R.drawable.like_1);
                    count.setText(c+" Likes");
                }
                else{
                    mLikeBtn.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setContext(Context context){
        this.context = context;

    }


    public void setDesc(String desc){
        TextView  post_desc = (TextView)mView.findViewById(R.id.post_desc);
        post_desc.setText(desc);
    }

    public void setPostImage(String download_url){

        ImageView post_img = (ImageView)mView.findViewById(R.id.post_image);
        Picasso.get().load(download_url).placeholder(R.drawable.img).into(post_img);


    }

    public void setImage(String image){

        CircleImageView profileImage = mView.findViewById(R.id.post_user_profile_pic);
        Picasso.get().load(image).placeholder(R.drawable.avatar).into(profileImage);

    }

    public void setUsername(String username){

        TextView  post_desc = mView.findViewById(R.id.nameTv);
        post_desc.setText(username);
    }

    public void setStatus(String status){

        TextView  post_desc = mView.findViewById(R.id.statusTv);
        post_desc.setText(status);
    }


    public void setTimestamp(Object timestamp){

        String timeStamp = timestamp.toString();

        GetTimeAgo getTimeAgo = new GetTimeAgo();

        long lastTime = Long.parseLong(timeStamp);

        String blogPostTime = getTimeAgo.getTimeAgo(lastTime, mView.getContext());

        TextView  post_desc = (TextView)mView.findViewById(R.id.post_time);
        post_desc.setText(blogPostTime);
    }



}
