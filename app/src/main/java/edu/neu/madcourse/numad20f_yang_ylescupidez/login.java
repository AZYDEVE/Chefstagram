package edu.neu.madcourse.numad20f_yang_ylescupidez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class login extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgotPassword;
    private EditText email, password;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.login_register);
       
        email = findViewById(R.id.Login_email);
        password = findViewById(R.id.Login_password);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.login_progress);

        register.setOnClickListener(this);
        loginBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.login_btn:
                userLogin();
                break;

        }
    }

    private void userLogin() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        if (emailStr.isEmpty()) {
            email.setError("email is required");
            email.requestFocus();
            return;
        }


        if (passwordStr.isEmpty()) {
            password.setError("password is required");
            password.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(login.this, MainActivity.class));


                } else {
                    Toast toast = Toast.makeText(login.this, "Failed to login, please check your credentials", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            ;

        });


    }
}