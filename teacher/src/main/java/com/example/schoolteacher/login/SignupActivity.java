package com.example.schoolteacher.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolteacher.MainActivity;
import com.example.schoolteacher.R;
import com.example.schoolteacher.parents.MainParentsActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "";
    private static final int RC_SIGN_IN = 9001;

    TextInputEditText mName, mEmail,mPassword;
    Button mSignupBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    View mViewHelper;

    FirebaseAuth.AuthStateListener mAuthListner;
    String userID;

    private Spinner signUpUserType;


    @Override
    protected void onStart() {

        super.onStart();

        mAuth.addAuthStateListener(mAuthListner);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView textView = findViewById(R.id.textView);
        textView.setText(Html.fromHtml(getString(R.string.agree_terms)));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mEmail = findViewById(R.id.et_email_address);
        mPassword = findViewById(R.id.et_password);
        mName = findViewById(R.id.et_name);
        mSignupBtn = findViewById(R.id.create_btn);
        progressBar = findViewById(R.id.loading_spinner);
        mViewHelper = findViewById(R.id.view_helper);

        signUpUserType = findViewById(R.id.signUpUserType);

        /* SIGNUP USER TYPE SPINNER */

        // Spinner element
        final Spinner spinner = signUpUserType;

        // Spinner Drop down elements
        final List<String> userType = new ArrayList<>();
        userType.add("Teacher");
        userType.add("Parent");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userType);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        /* SIGNUP USER TYPE SPINNER */




        mAuth = FirebaseAuth.getInstance();

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String name = mName.getText().toString();
                String item = signUpUserType.getSelectedItem().toString();

                if(TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(name)) {
                    mName.setError("Name is required.");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required.");
                    return;
                }
                if(password.length() < 6) {
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                } registerUser(email, password, name, item);

                progressBar.setVisibility(View.VISIBLE);
                mViewHelper.setVisibility(View.VISIBLE);
                mSignupBtn.setVisibility(View.INVISIBLE);


            }
        });

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null && userType.equals("Teacher")) {
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);


                } else if (firebaseAuth.getCurrentUser() != null && userType.equals("Parent")) {
                    Intent intent = new Intent(SignupActivity.this, MainParentsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                }

            }
        };


    }

    private void registerUser(final String email, String password, final String name, final String item) {

        // register the user in firebase

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    Toast.makeText(SignupActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                    userID = mAuth.getCurrentUser().getUid();

                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

                    current_user_db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userType = dataSnapshot.child("userType").getValue().toString();
                            if(userType.equals("Parent") && item.equals("Parent")) {
                                Intent intentParent = new Intent(SignupActivity.this, MainParentsActivity.class);
                                intentParent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intentParent);
                                finish();
                                overridePendingTransition(0, 0);
                            } else if (userType.equals("Teacher") && item.equals("Teacher")) {
                                Intent intentParent = new Intent(SignupActivity.this, MainActivity.class);
                                intentParent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intentParent);
                                finish();
                                overridePendingTransition(0, 0);
                            } else {
                                Toast.makeText(SignupActivity.this, "Failed Login. Please Try Again", Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);
                                mViewHelper.setVisibility(View.INVISIBLE);
                                mSignupBtn.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", email);
                    user.put("userType", item);
                    user.put("image", "default");
                    user.put("status", item);
                    user.put("thumb_image", "default");

                    current_user_db.setValue(user);

                }

            }
        });
    }



    public void signIn(View v) { startActivity(new Intent(this, LoginActivity.class));}



}