package com.vasi.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vasi.test.Data.UserNotes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements UpdateInterface, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar_home)
    Toolbar toolbarHome;

    @BindView(R.id.text_home_no_data)
    TextView textHomeNoData;
    @BindView(R.id.button_add_note)
    FloatingActionButton buttonAddNote;

    ArrayList<UserNotes> allNotesList = new ArrayList<>();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    DatabaseReference databaseNotes;
    @BindView(R.id.recycler_all_notes)
    RecyclerView recyclerAllNotes;

    LinearLayoutManager linearLayoutManager;
    private DrawerLayout drawer;

    TextView userTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        String uname = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setSupportActionBar(toolbarHome);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbarHome, R.string.navigation_drawer_open,R.string.navigation_draw_close);
        toggle.syncState();

        View header = navigationView.getHeaderView(0);
        userTextView = header.findViewById(R.id.usernameTextField);
        userTextView.setText(uname);


        sharedPreferences = getSharedPreferences("FIRENOTESDATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        String uid = sharedPreferences.getString("UID", "");
        databaseNotes = FirebaseDatabase.getInstance().getReference("USERNOTES").child(uid);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerAllNotes.setLayoutManager(linearLayoutManager);

        readAllNotes();
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        allNotesList.clear();
        readAllNotes();
    }

    @OnClick(R.id.button_add_note)
    public void onViewClicked() {
        Intent intentAdd = new Intent(MainActivity.this, AddNotesActivity.class);
        startActivity(intentAdd);
    }

    public void readAllNotes() {

        allNotesList.clear();

        databaseNotes.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserNotes userNotes = snapshot.getValue(UserNotes.class);
                    allNotesList.add(userNotes);
                }

                NotesAdapter notesAdapter = new NotesAdapter(MainActivity.this, allNotesList);
                recyclerAllNotes.setAdapter(notesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void updateUsernote(UserNotes userNotes) {
        databaseNotes.child(userNotes.getNoteId()).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Note updated successfully!" , Toast.LENGTH_SHORT).show();
                    readAllNotes();
                } else {
                    Toast.makeText(MainActivity.this, "Try again!" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void deleteNote(UserNotes userNotes) {
        databaseNotes.child(userNotes.getNoteId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Note deleted!", Toast.LENGTH_SHORT).show();
                    readAllNotes();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                editor.putBoolean("LOGINSTATUS",false);
                editor.commit();
                Intent intent  = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
