package com.example.noteapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.noteapp.model.NoteModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NoteAdapter.NoteListener {
    lateinit var addNewNoteButton: FloatingActionButton
    lateinit var noteRecycleView: RecyclerView

    var notes = mutableListOf<NoteModel>(
        NoteModel("title1","desc1"),
        NoteModel("title2","desc2"),
        NoteModel("title3","desc3")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initNoteRecyclerView()
        addNewNoteButton = findViewById(R.id.add_new_note_button)
        addNewNoteButton.setOnClickListener {
            Log.d("MainActivity", "Clicked!")
        }
    }

    private fun initNoteRecyclerView() {
        noteRecycleView = findViewById(R.id.note_recycler_view)
        val adapter = NoteAdapter(notes, this)
        val layoutManager = LinearLayoutManager(this)

        noteRecycleView.adapter = adapter
        noteRecycleView.layoutManager = layoutManager
    }

    override fun onItemClicked(position: Int) {
        Log.d("MainActivity", "Le titre est ${notes[position].title}")
    }
}