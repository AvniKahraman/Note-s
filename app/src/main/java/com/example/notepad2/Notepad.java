package com.example.notepad2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.notepad2.databinding.ActivityAddNoteActivtyBinding;

public class Notepad extends AppCompatActivity {

    private ActivityAddNoteActivtyBinding binding;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteActivtyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        database = this.openOrCreateDatabase("Note", MODE_PRIVATE, null);
        createTable(); // Create the table if not exists

        ImageView saveButton = binding.saveButton;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        if (info != null && info.equals("new")) {
            binding.titleEditText.setText("");
            binding.contentEditText.setText("");
            binding.saveButton.setVisibility(View.VISIBLE);
        } else {
            int noteId = intent.getIntExtra("noteId", 0);
            loadNoteData(noteId);
        }
    }

    private void createTable() {
        try {
            database.execSQL("CREATE TABLE IF NOT EXISTS note (id INTEGER PRIMARY KEY ,title VARCHAR,type VARCHAR)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNoteData(int noteId) {
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM note WHERE id = ?", new String[]{String.valueOf(noteId)});
            int notetitleIx = cursor.getColumnIndex("title");
            int notetypeIx = cursor.getColumnIndex("type");

            if (cursor.moveToFirst()) {
                binding.titleEditText.setText(cursor.getString(notetitleIx));
                binding.contentEditText.setText(cursor.getString(notetypeIx));
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        String title = binding.titleEditText.getText().toString();
        String type = binding.contentEditText.getText().toString();

        try {
            String sqlSting = "INSERT INTO note(title,type) VALUES(?,?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlSting);
            sqLiteStatement.bindString(1, title);
            sqLiteStatement.bindString(2, type);
            sqLiteStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Notepad.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
