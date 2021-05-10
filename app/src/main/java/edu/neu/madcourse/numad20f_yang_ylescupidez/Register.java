package edu.neu.madcourse.numad20f_yang_ylescupidez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
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
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.numad20f_yang_ylescupidez.model.User;

public class Register extends AppCompatActivity implements View.OnClickListener {

    TextView login;
    EditText name, email, password;
    Button btnRegister;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = findViewById(R.id.register_to_Login);
        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        btnRegister = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.register_progress);

        login.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_to_Login:
                startActivity(new Intent(this, login.class));
                break;
            case R.id.button_register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        progressBar.setVisibility(progressBar.VISIBLE);
        final String nameStr = name.getText().toString().trim();
        final String emailStr = email.getText().toString().trim();
        final String passwordStr = password.getText().toString().trim();

        if (nameStr.isEmpty()) {
            name.requestFocus();
            name.setError("Name is required");

            return;
        }
        if (emailStr.isEmpty()) {
            email.requestFocus();
            email.setError("Email is required");

            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            email.requestFocus();
            email.setError("Please enter a valid email");

            return;
        }

        if (passwordStr.isEmpty()) {
            password.requestFocus();
            password.setError("Password is required");

            return;
        }

        if(passwordStr.length()<6){
            password.requestFocus();
            password.setError("password requires minimum 6 characters");
        }

        mAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User newUser = new User(nameStr,"", "");
                            newUser.setKey(mAuth.getCurrentUser().getUid());
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast toast = Toast.makeText(Register.this, " Account is created succesfully", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();

                                        startActivity(new Intent(Register.this, MainActivity.class));
                                    } else {
                                        Toast toast = Toast.makeText(Register.this, "Fail register! Please try again", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                                    progressBar.setVisibility(progressBar.INVISIBLE);
                                }
                            });
                        } else {
                            Toast toast = Toast.makeText(Register.this, "Fail register! Please try again", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            progressBar.setVisibility(progressBar.INVISIBLE);
                        }
                    }
                });
    }


}