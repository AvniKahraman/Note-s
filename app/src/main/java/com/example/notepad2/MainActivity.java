package com.example.notepad2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.notepad2.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    NoteAdapter noteAdapter;
    ArrayList<Note_class> noteArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        FloatingActionButton addButton = findViewById(R.id.addButton);

        noteArrayList = new ArrayList<>();

        binding.recylerview.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(MainActivity.this, noteArrayList);
        binding.recylerview.setAdapter(noteAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notepad.class);
                intent.putExtra("info", "new");
                startActivity(intent);
                getData();
            }
        });




        getData();
    }

    private void getData() {
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("Note", MODE_PRIVATE, null);
            Log.d("Database", "Database created successfully");


            Cursor cursor = database.rawQuery("SELECT * FROM note", null);
            int titleIx = cursor.getColumnIndex("title");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()) {
                String title = cursor.getString(titleIx);
                int id = cursor.getInt(idIx);
                Note_class note = new Note_class(title, id);
                noteArrayList.add(note);
            }
            noteAdapter.notifyDataSetChanged();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public void onBindViewHolder(@NonNull NoteAdapter.NoteHolder holder, int position) {
            // İmajı ayarla, metni gösterme
            holder.binding.editView.setImageResource(R.drawable.baseline_delete_24);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = holder.getAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(holder.itemView.getContext(), Notepad.class);
                        intent.putExtra("info", "old");
                        intent.putExtra("noteId", noteArrayList.get(clickedPosition).id);
                        holder.itemView.getContext().startActivity(intent);
                    }
                }
            });



        // Uzun tıklamaya tepki veren bir OnLongClickListener ekleyin
            holder.binding.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Silme işlemi burada gerçekleştirilebilir
                    deleteNoteFromDatabase(noteArrayList.get(position).id);

                }
            });
        }

    private void deleteNoteFromDatabase(int noteId) {
        // SQLite veritabanından notu silme işlemini gerçekleştirin
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("Note", MODE_PRIVATE, null);
            database.execSQL("DELETE FROM note WHERE id = ?", new Object[]{noteId});
            // Veriyi güncelledikten sonra listeyi yeniden yükleyebilirsiniz: getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
