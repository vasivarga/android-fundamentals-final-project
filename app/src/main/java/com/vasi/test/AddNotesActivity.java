package com.vasi.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vasi.test.Data.UserNotes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNotesActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_add_note)
    Toolbar toolbarAddNote;
    @BindView(R.id.editText_title)
    EditText editTextTitle;
    @BindView(R.id.editText_description)
    EditText editTextDescription;
    @BindView(R.id.button_save_note)
    Button buttonSaveNote;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    DatabaseReference databaseNotes;
    @BindView(R.id.addNotePb)
    ProgressBar addNotePb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        ButterKnife.bind(this);

        getSupportActionBar().hide();

        toolbarAddNote.setTitle("Add Note");
        toolbarAddNote.setTitleTextColor(Color.WHITE);
        toolbarAddNote.setNavigationIcon(R.drawable.ic_arrow_back);

        addNotePb.setVisibility(View.INVISIBLE);

        sharedPreferences = getSharedPreferences("FIRENOTESDATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbarAddNote.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String userId = sharedPreferences.getString("UID", "");

        databaseNotes = FirebaseDatabase.getInstance().getReference("USERNOTES").child(userId);


    }

    @OnClick(R.id.button_save_note)
    public void onViewClicked() {

        String title = editTextTitle.getText().toString();
        String desc = editTextDescription.getText().toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Calendar calendar = Calendar.getInstance();
        String todaysDate = simpleDateFormat.format(calendar.getTime());

        if (!title.equalsIgnoreCase("")) {
            if (!desc.equalsIgnoreCase("")) {

                addNotePb.setVisibility(View.VISIBLE);
                String key = databaseNotes.push().getKey();
                UserNotes userNotes = new UserNotes(title, desc, todaysDate, key);
                databaseNotes.child(key).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        addNotePb.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNotesActivity.this, "Note saved!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else
                        {
                            Toast.makeText(AddNotesActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                Toast.makeText(AddNotesActivity.this, "Please enter description!.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AddNotesActivity.this, "Please enter title!.", Toast.LENGTH_SHORT).show();
        }

    }
}
