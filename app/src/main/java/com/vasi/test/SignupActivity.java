package com.vasi.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vasi.test.Data.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.editText_signup_name)
    EditText editTextSignupName;
    @BindView(R.id.editText_signup_email)
    EditText editTextSignupEmail;
    @BindView(R.id.editText_signup_password)
    EditText editTextSignupPassword;
    @BindView(R.id.button_register)
    Button buttonRegister;

    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    DatabaseReference databaseUsers;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("FIRENOTESDATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        databaseUsers = FirebaseDatabase.getInstance().getReference("USERS");

    }

    @OnClick(R.id.button_register)
    public void onViewClicked() {

        String name = editTextSignupName.getText().toString();
        String email = editTextSignupEmail.getText().toString();
        String password = editTextSignupPassword.getText().toString();

        registerUser(name, email, password);

    }

    public void registerUser(final String name, final String email, String password) {

        if (!name.equalsIgnoreCase("")) {
            if (!email.equalsIgnoreCase("")) {
                if (!password.equalsIgnoreCase("")) {

                    progressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                        String uid = currentUser.getUid();

                                        com.vasi.test.Data.UserInfo userInfo = new UserInfo(name, email, "");

                                        databaseUsers.child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignupActivity.this, "Registration completed!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                        });


                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);

                                        Toast.makeText(SignupActivity.this, "Registration failed(1).",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                } else {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
        }

    }

}