package com.example.noteapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NoteDetailsActivity : AppCompatActivity() {

    lateinit var noteTitleEditText: EditText
    lateinit var noteDescEditText: EditText
    lateinit var updateNoteButton: Button

    var noteTitle: String? = null
    var noteDesc: String? = null
    var notePosition: Int? = null
    var isUpdated: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_note_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        fetchData()
        updateNoteButton.setOnClickListener {
            noteTitle = noteTitleEditText.text.toString()
            noteDesc = noteDescEditText.text.toString()
            isUpdated = true
        }
    }

    override fun onBackPressed() {
        if (isUpdated) {
            val intent = Intent()
            intent.putExtra(NOTE_TITLE, noteTitle)
            intent.putExtra(NOTE_DESC, noteDesc)
            intent.putExtra(NOTE_POSITION, notePosition)
            setResult(Activity.RESULT_OK, intent)
        }
        super.onBackPressed()
    }

    private fun fetchData() {
        if (intent.hasExtra(NOTE_TITLE)) {
            noteTitle = intent.getStringExtra(NOTE_TITLE)
            noteDesc = intent.getStringExtra(NOTE_DESC)
            notePosition = intent.getIntExtra(NOTE_POSITION, -1)

            Log.d("NoteDetailsActivity","Open title:$noteTitle, desc:$noteDesc at position: $notePosition")

            noteTitleEditText.setText(noteTitle)
            noteDescEditText.setText(noteDesc)
        }else{
            noteTitle = intent.getStringExtra(NOTE_TITLE)
            Log.d("NoteDetailsActivity","Don't has any Extra, title: $noteTitle")
        }
    }

    private fun initViews() {
        noteTitleEditText = findViewById(R.id.note_title_edit_text)
        noteDescEditText = findViewById(R.id.note_desc_edit_text)
        updateNoteButton = findViewById(R.id.update_note_button)
    }
}