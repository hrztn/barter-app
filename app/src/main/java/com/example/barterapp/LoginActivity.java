package com.example.barterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barterapp.modules.LoadSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText login_email, login_password;
    private TextView new_user;
    private Button loginBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private LoadSpinner spinner;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        if (user != null) {
            Intent i = new Intent(this, Activity.class);
            startActivity(i);
            finish();
        }
        init();
        firebaseAuth = FirebaseAuth.getInstance();
        clickListener();
    }

    private void init(){
        pref = this.getSharedPreferences("appinfo",MODE_PRIVATE);
        editor = pref.edit();
        login_email = findViewById(R.id.login_email_input);
        login_password= findViewById(R.id.login_password_input);
        login_email = findViewById(R.id.login_email_input);
        spinner = new LoadSpinner(this);
        loginBtn = findViewById(R.id.login_btn);
        new_user = findViewById(R.id.login_reg_btn);
    }

    public void skipLogin(View view) {
        Intent i=new Intent(this, DashboardActivity.class);
        startActivity(i);
    }

    private void clickListener(){
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = login_email.getText().toString();
                String password = login_password.getText().toString();

                if (TextUtils.isEmpty(email)){
                    login_email.setError("Invalid email.");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    login_password.setError("Invalid password");
                    return;
                }

                signIn(email, password);
            }
        });
    }
    private void signIn(String email, String password){

        spinner.startLoadingAnimation();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            spinner.stopLoadingAnimation();
                            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                            if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                                Intent i=new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }else {
                            spinner.stopLoadingAnimation();
                            Toast.makeText(LoginActivity.this, "Invalid account: "+
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

}
