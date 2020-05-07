package com.example.schoolteacher.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.schoolteacher.ResetPasswordActivity;
import com.example.schoolteacher.SplashActivity;
import com.example.schoolteacher.parents.MainParentsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "";
    private static final int RC_SIGN_IN = 9001;

    private EditText inputEmail, inputPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private View mViewHelper;

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

        mAuth = FirebaseAuth.getInstance();

        //check the current user
        if (mAuth.getCurrentUser() != null) {

            Intent intent = getType().equals("teacher") ? new Intent(LoginActivity.this, MainActivity.class) :
                    new Intent(LoginActivity.this, MainParentsActivity.class);

            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }

        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.et_email_address);
        inputPassword = findViewById(R.id.et_password);
        final Button ahlogin = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.loading_spinner);
        TextView btnForgot = findViewById(R.id.forgot);
        TextView btnSignUp = findViewById(R.id.signup_here_Button);

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




        btnSignUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

        btnForgot.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class)));


        mAuth = FirebaseAuth.getInstance();

        // Checking the email id and password is Empty
        ahlogin.setOnClickListener(v -> {

            String email = inputEmail.getText().toString();
            final String password = inputPassword.getText().toString();
            String item = signUpUserType.getSelectedItem().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Email is required.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if(password.length() < 6) {
                inputPassword.setError("Password Must be >= 6 Characters");
                return;
            } loginrUser(email, password, item);


            progressBar.setVisibility(View.VISIBLE);
            mViewHelper.setVisibility(View.VISIBLE);
            ahlogin.setVisibility(View.INVISIBLE);


        });


        mAuthListner = firebaseAuth -> {

            if (firebaseAuth.getCurrentUser() != null && userType.equals("Teacher")) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION );
                overridePendingTransition(0, 0);

            } else if (firebaseAuth.getCurrentUser() != null && userType.equals("Parent")) {

                Intent intent = new Intent(LoginActivity.this, MainParentsActivity.class);
                startActivity(intent);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION );
                overridePendingTransition(0, 0);
            }
        };
    }

    private void loginrUser(String email, String password, final String item) {

        final Button ahlogin = findViewById(R.id.login_btn);


        //authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {

                    progressBar.setVisibility(View.GONE);
                    mViewHelper.setVisibility(View.INVISIBLE);
                    ahlogin.setVisibility(View.VISIBLE);

                    if (task.isSuccessful()) {
                        // there was an error
                        Log.d(TAG, "signInWithEmail:success");

                        userID = mAuth.getCurrentUser().getUid();

                        Intent userIntent = new Intent(LoginActivity.this, SplashActivity.class);
                        userIntent.putExtra("userID", userID);
                        startActivity(userIntent);


                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

                        current_user_db.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String userType = dataSnapshot.child("userType").getValue().toString();

                                if (userType.equals("Parent") && item.equals("Parent")) {

                                    setType("parent");

                                    Intent intent = new Intent(LoginActivity.this, MainParentsActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION );
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(0, 0);

                                }  else if (userType.equals("Teacher") && item.equals("Teacher")) {

                                    setType("teacher");

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(0, 0);

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    } else {
                        Log.d(TAG, "singInWithEmail:Fail");
                        Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setType(String type) {

        SharedPreferences preferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("type", type);
        editor.apply();
    }

    private String getType() {

        SharedPreferences preferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return preferences.getString("type", "teacher");
    }
}
