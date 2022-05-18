package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class notesactivity extends AppCompatActivity {

    FloatingActionButton mcreateNoteFab;
    private FirebaseAuth firebaseAuth;

    RecyclerView mrecyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter <firebasemodel, NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notesactivity);

        mcreateNoteFab=findViewById(R.id.createnotefab);
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("All Notes");

        mcreateNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(notesactivity.this, createNotes.class));
            }
        });

        Query query=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> allUserNotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter <firebasemodel, NoteViewHolder>(allUserNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {

                ImageView popuButton = noteViewHolder.itemView.findViewById(R.id.menupopbutton);
                int colourcode = getRandomColor();
//                noteViewHolder.mnote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colourcode, null));
                noteViewHolder.noteTitle.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colourcode, null));
                noteViewHolder.menuPopButton.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colourcode, null));

                noteViewHolder.noteTitle.setText(firebasemodel.getTitle());
                noteViewHolder.noteContent.setText(firebasemodel.getContent());

                String nhi = noteAdapter.getSnapshots().getSnapshot(i).getId();

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //m

                        Intent intent = new Intent(v.getContext(), noteDetails.class);
                        intent.putExtra("title", firebasemodel.getTitle());
                        intent.putExtra("content",firebasemodel.getContent());
                        intent.putExtra("noteId", nhi);

                        v.getContext().startActivity(intent);
//                        Toast.makeText(getApplicationContext(), "Thí is Clicked", Toast.LENGTH_LONG).show();
                    }
                });

                popuButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popUpMenu = new PopupMenu(v.getContext(), v);
                        popUpMenu.setGravity(Gravity.END);
                        popUpMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent intent = new Intent(v.getContext(), editNoteactivity.class);
                                intent.putExtra("title", firebasemodel.getTitle());
                                intent.putExtra("content",firebasemodel.getContent());
                                intent.putExtra("noteId", nhi);
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popUpMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(v.getContext(),"This note is deleted", Toast.LENGTH_LONG).show();
                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(nhi);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(v.getContext(),"This note is deleted", Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(v.getContext(),"Failed To Delete", Toast.LENGTH_LONG).show();


                                    }
                                });

                                return false;
                            }
                        });

                        popUpMenu.show();


             }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new NoteViewHolder(view);

            }
        };


        mrecyclerView = findViewById(R.id.recyclerview);
        mrecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mrecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerView.setAdapter(noteAdapter);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        private TextView noteTitle;
        private TextView noteContent;
        LinearLayout mnote;
        ImageView menuPopButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.notetitle);
            noteContent = itemView.findViewById(R.id.notecontent);
            mnote = itemView.findViewById(R.id.note);
            menuPopButton = itemView.findViewById(R.id.menupopbutton);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(notesactivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter != null){
            noteAdapter.startListening();
        }
    }


    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.gray);
        colorCode.add(R.color.green);
        colorCode.add(R.color.lightgreen);
        colorCode.add(R.color.darkblue);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.purple);
        colorCode.add(R.color.red);
        colorCode.add(R.color.orange);

//        colorCode.add(R.color.c1);
//        colorCode.add(R.color.c2);
//        colorCode.add(R.color.c3);
//        colorCode.add(R.color.c4);
//        colorCode.add(R.color.c5);

        Random random = new Random();
        int number = random.nextInt(colorCode.size());
        return colorCode.get(number);



    }
}