package com.example.noteapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateNoteActivity : AppCompatActivity() {
    lateinit var noteTitleEditText: EditText
    lateinit var noteDescEditText: EditText
    lateinit var createNoteButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        noteTitleEditText = findViewById(R.id.note_title_edit_text)
        noteDescEditText = findViewById(R.id.note_desc_edit_text)
        createNoteButton = findViewById(R.id.create_note_button)

        createNoteButton.setOnClickListener {
            val title = noteTitleEditText.text.toString()
            val desc = noteDescEditText.text.toString()
            Log.d("CreateNoteActivity","Titre: $title, desc: $desc")

            val intent = Intent()
            intent.putExtra(NOTE_TITLE, title)
            intent.putExtra(NOTE_DESC, desc)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}