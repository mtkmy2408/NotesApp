package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createNotes extends AppCompatActivity {

    EditText mcreateTitleOfNote, mcreateContentOfNote;
    FloatingActionButton msaveNote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressBar mprogressBarOfCreateNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);


        msaveNote=findViewById(R.id.savenote);
        mcreateContentOfNote=findViewById(R.id.createcontentofnote);
        mcreateTitleOfNote=findViewById(R.id.createtitleofnote);

        Toolbar toolbar=findViewById(R.id.toolbarofcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        mprogressBarOfCreateNote=findViewById(R.id.progressbarofcreatenote);



        msaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=mcreateTitleOfNote.getText().toString();
                String content=mcreateContentOfNote.getText().toString();
                if(title.isEmpty()||content.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Both field are Required", Toast.LENGTH_LONG).show();
                }
                else{

                    mprogressBarOfCreateNote.setVisibility(View.VISIBLE);
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", title);
                    note.put("content", content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Note Created Succesfully", Toast.LENGTH_LONG).show();
//                            mprogressBarOfCreateNote.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(createNotes.this, notesactivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To Create Note", Toast.LENGTH_LONG).show();
                            mprogressBarOfCreateNote.setVisibility(View.INVISIBLE);
//                            startActivity(new Intent(createNotes.this, notesactivity.class));


                        }

                    });
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}