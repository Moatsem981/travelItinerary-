package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_signup_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Find UI elements
        usernameInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);
        forgotPassword = findViewById(R.id.forgotPassword);

        // Sign In Button Click Listener
        signInButton.setOnClickListener(v -> loginUser());

        // Sign Up Button Click Listener
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginSignUpPage.this, SignUpPage.class);
            startActivity(intent);
        });

        // Forgot Password Click Listener
        forgotPassword.setOnClickListener(v -> {
            Toast.makeText(LoginSignUpPage.this, "Password reset feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loginUser() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginSignUpPage.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check Firestore for the user's credentials
        db.collection("Users").document(username)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the stored password
                        String storedPassword = documentSnapshot.getString("password");

                        // Verify password
                        if (storedPassword != null && storedPassword.equals(password)) {
                            Toast.makeText(LoginSignUpPage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginSignUpPage.this, AppMainPage.class);
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
}
