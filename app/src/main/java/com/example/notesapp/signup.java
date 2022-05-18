package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {

    private EditText msignUpEmail, msignUpPassword;
    private RelativeLayout msignUp;
    private TextView mgoToLogin;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        msignUpEmail=findViewById(R.id.signupemail);
        msignUpPassword=findViewById(R.id.signuppassword);
        msignUp=findViewById(R.id.signup);
        mgoToLogin=findViewById(R.id.gotologin);
        firebaseAuth=FirebaseAuth.getInstance();


        mgoToLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signup.this, MainActivity.class);
                startActivity(intent);
            }
        });


        msignUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String mail=msignUpEmail.getText().toString().trim();
                String password=msignUpPassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are Required", Toast.LENGTH_LONG).show();
                }
                else if(password.length()<5){
                    Toast.makeText(getApplicationContext(), "Password Should Greater Than 5 Digits", Toast.LENGTH_LONG).show();
                }
                else {
//                    //đăng ký người dùng bằng firebase
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Registration Successful", Toast.LENGTH_LONG).show();
                                sendEmailVerification();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Failed To Register", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }



            }
        });
    }

    //gửi email xác minh
    private void sendEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Verification Email is Sent, Verify and Log In Again", Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(signup.this, MainActivity.class));

                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Fail To Send Verification Email.", Toast.LENGTH_LONG).show();

        }
    }


}