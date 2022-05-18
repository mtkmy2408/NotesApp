package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

    private EditText mforgotPassword;
    private Button mpasswordRecoverButton;
    private TextView mgoBackToLogin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().hide();

        mforgotPassword=findViewById(R.id.forgotpassword);
        mpasswordRecoverButton=findViewById(R.id.passwordrecoverbutton);
        mgoBackToLogin=findViewById(R.id.gobacktologin);

        firebaseAuth=FirebaseAuth.getInstance();

        mgoBackToLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(forgotPassword.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mpasswordRecoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mforgotPassword.getText().toString().trim();
                if(mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Your Email First!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //

                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Mail Sent, You Can Recover Your Password Using Mail", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(forgotPassword.this, MainActivity.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Email is Wrong of Account Not Exist", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        });



    }
}