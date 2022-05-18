package com.example.notesapp;

//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText mloginEmail, mloginPassword;
    private RelativeLayout mlogin, mgoToSignup;
    private TextView mgoToForgotPassword;

    private FirebaseAuth firebaseAuth;
    ProgressBar mprogressBarOfMainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mloginEmail=findViewById(R.id.loginemail);
        mloginPassword=findViewById(R.id.loginpassword);
        mlogin=findViewById(R.id.login);
        mgoToForgotPassword=findViewById(R.id.gotoforgotpassword);
        mgoToSignup=findViewById(R.id.gotosignup);
        mprogressBarOfMainActivity = findViewById(R.id.progressbarofmainactivity);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this, notesactivity.class));
        }

        mgoToSignup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, signup.class));
                Intent intent=new Intent(MainActivity.this, signup.class);
                startActivity(intent);
                finish();
            }
        });

        mgoToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, forgotPassword.class);
                startActivity(intent);
                finish();
            }
        });


        mlogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String mail=mloginEmail.getText().toString().trim();
                String password=mloginPassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Field Are Required", Toast.LENGTH_LONG).show();
                }
                else{
                    //đăng nhập

                    mprogressBarOfMainActivity.setVisibility(View.VISIBLE);


                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                checkMailVerfication();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Account Doesn't Exist", Toast.LENGTH_LONG).show();
                                mprogressBarOfMainActivity.setVisibility(View.INVISIBLE);

                            }

                        }
                    });
                }
            }
        });

    }

    private void checkMailVerfication(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

            if(firebaseUser.isEmailVerified()==true){
                Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(MainActivity.this, notesactivity.class));
        }

        else{
            mprogressBarOfMainActivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verify Your Mail First!", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();

        }
    }
}