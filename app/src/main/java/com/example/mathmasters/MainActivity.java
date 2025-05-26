package com.example.mathmasters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnSignIn, btnSignUp;
    TextView tvError;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);
        tvError = findViewById(R.id.tv_error);


        mAuth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    // Signin with Firebase
    private void signIn() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            tvError.setText("Please fill in all fields");
            tvError.setVisibility(View.VISIBLE);
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        tvError.setText("Sign in successful!");
                        tvError.setTextColor(0xFF388E3C);
                        tvError.setVisibility(View.VISIBLE);
                        // Go to HomeActivity
                        Intent i = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        tvError.setText("Sign in failed: " + task.getException().getMessage());
                        tvError.setTextColor(0xFFB00020);
                        tvError.setVisibility(View.VISIBLE);
                    }
                });
    }

    //Signup with Firebase
    private void signUp() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            tvError.setText("Please fill in all fields");
            tvError.setVisibility(View.VISIBLE);
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        tvError.setText("Sign up successful!");
                        tvError.setTextColor(0xFF388E3C);
                        tvError.setVisibility(View.VISIBLE);
                        // Go to HomeActivity
                        Intent i = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        tvError.setText("Sign up failed: " + task.getException().getMessage());
                        tvError.setTextColor(0xFFB00020);
                        tvError.setVisibility(View.VISIBLE);
                    }
                });
    }
}