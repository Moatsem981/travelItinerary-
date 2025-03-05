package com.example.travelappcw;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginSignUpPage extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button signInButton, signUpButton;
    private TextView forgotPassword;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_page);

        db = FirebaseFirestore.getInstance();

        usernameInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);
        forgotPassword = findViewById(R.id.forgotPassword);

        signInButton.setOnClickListener(v -> loginUser());

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginSignUpPage.this, SignUpPage.class);
            startActivity(intent);
        });

        forgotPassword.setOnClickListener(v -> showPasswordResetDialog());
    }

    private void loginUser() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginSignUpPage.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("Users").document(username)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String storedPassword = documentSnapshot.getString("password");
                        if (storedPassword != null && storedPassword.equals(password)) {
                            Toast.makeText(LoginSignUpPage.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            // Pass User ID to the AppMainPage
                            Intent intent = new Intent(LoginSignUpPage.this, AppMainPage.class);
                            intent.putExtra("USER_ID", username);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginSignUpPage.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginSignUpPage.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(LoginSignUpPage.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showPasswordResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        final EditText input = new EditText(this);
        input.setHint("Enter your username");
        builder.setView(input);

        builder.setPositiveButton("Send Reset Email", (dialog, which) -> {
            String username = input.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(LoginSignUpPage.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("Users").document(username)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String email = documentSnapshot.getString("email");

                            if (email != null) {
                                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(LoginSignUpPage.this, "Password reset email sent!", Toast.LENGTH_LONG).show())
                                        .addOnFailureListener(e -> Toast.makeText(LoginSignUpPage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            } else {
                                Toast.makeText(LoginSignUpPage.this, "No email found for this username", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginSignUpPage.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
