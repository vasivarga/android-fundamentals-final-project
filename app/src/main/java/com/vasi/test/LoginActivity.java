package com.vasi.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editText_email)
    EditText editTextEmail;
    @BindView(R.id.editText_password)
    EditText editTextPassword;
    @BindView(R.id.button_login)
    Button buttonLogin;
    @BindView(R.id.text_signup)
    TextView textSignup;
    @BindView(R.id.text_forgot_password)
    TextView textForgotPassword;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressBar = findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.INVISIBLE);

        sharedPreferences = getSharedPreferences("FIRENOTESDATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.button_login, R.id.text_signup, R.id.text_forgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                progressBar.setVisibility(View.VISIBLE);
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                if (!email.equalsIgnoreCase("")) {
                    if (!password.equalsIgnoreCase("")) {
                        loginUser(email, password);
                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter password!.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter your email!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_signup:
                Intent intentSignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intentSignup);
                break;
            case R.id.text_forgot_password:
                String emailId = editTextEmail.getText().toString();
                if (!emailId.equalsIgnoreCase("")) {

                    firebaseAuth.sendPasswordResetEmail(emailId).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Email sent!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Error sending reset link. Check the email address again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "Please enter a valid email!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    final String uid = currentUser.getUid();
                    editor.putString("UID",uid);
                    editor.putBoolean("LOGINSTATUS",true);
                    editor.commit();
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    editor.putBoolean("LOGINSTATUS",false);
                    editor.commit();
                    String errorMsg = task.getException().getMessage();
                    Toast.makeText(LoginActivity.this, "" + errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}