package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class editNoteactivity extends AppCompatActivity {

    Intent data;
    EditText mEditTitleOfNote, mEditContentOfNote;
    FloatingActionButton mSaveEditNote;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_noteactivity);

        mEditContentOfNote = findViewById(R.id.editcontentofnote);
        mEditTitleOfNote = findViewById(R.id.edittitleofnote);
        mSaveEditNote = findViewById(R.id.saveeditnote);
        Toolbar toolbar = findViewById(R.id.toolbarofeditnote);

        data = getIntent();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSaveEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "savebutton click", Toast.LENGTH_LONG).show();

                String newTitle = mEditTitleOfNote.getText().toString();
                String newContent = mEditContentOfNote.getText().toString();


                if(newTitle.isEmpty() || newContent.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Something is Empty", Toast.LENGTH_LONG).show();
                    return;

                }

                else{

                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String,Object> note = new HashMap<>();
                    note.put("title", newTitle);
                    note.put("content", newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                            Toast.makeText(getApplicationContext(), "Note is Updated", Toast.LENGTH_LONG).show();

                            startActivity(new Intent(editNoteactivity.this, notesactivity.class));


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed To Updated", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }
        });

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");
        mEditTitleOfNote.setText(noteTitle);
        mEditContentOfNote.setText(noteContent);




    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}