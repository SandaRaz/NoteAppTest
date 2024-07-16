package com.example.noteapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.model.NoteModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NoteAdapter.NoteListener {
    lateinit var addNewNoteButton: FloatingActionButton
    lateinit var noteRecycleView: RecyclerView

    var notes = mutableListOf<NoteModel>()

    lateinit var prefs: SharedPreferences

    @SuppressLint("NotifyDataSetChanged")
    private val createNoteActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val title = data?.getStringExtra(NOTE_TITLE)
            val desc = data?.getStringExtra(NOTE_DESC)

            val noteModel = NoteModel(title!!, desc!!)
            noteModel.save(prefs, 0,notes)

            val adapter = noteRecycleView.adapter
            adapter?.notifyItemInserted(0)
            adapter?.notifyItemRangeChanged(1, adapter.itemCount - 1)
//            noteRecycleView.adapter?.notifyDataSetChanged()

            val logs = "Creating new NoteModel($title,$desc) at position 0 ${printNotes(notes)}"
            Log.d("CreateNote ActivityResult => ", logs)
        }
    }

    private val noteDetailsActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data

            val title = data?.getStringExtra(NOTE_TITLE)
            val desc = data?.getStringExtra(NOTE_DESC)
            val position = data?.getIntExtra(NOTE_POSITION, -1)

            if(position != -1){
                val updateNote = NoteModel(title!!, desc!!)
                updateNote.update(prefs, position!!, notes)
                noteRecycleView.adapter?.notifyItemChanged(position)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefs = getSharedPreferences("notes", Context.MODE_PRIVATE)
        notes = NoteModel.getAll(prefs)

        initNoteRecyclerView()
        addNewNoteButton = findViewById(R.id.add_new_note_button)
        addNewNoteButton.setOnClickListener {
            val intent = Intent(this, CreateNoteActivity::class.java)
//            startActivity(intent)
            createNoteActivityResult.launch(intent)
        }

    }

    private fun initNoteRecyclerView() {
        noteRecycleView = findViewById(R.id.note_recycler_view)
        val adapter = NoteAdapter(notes, this)
        val layoutManager = LinearLayoutManager(this)

        noteRecycleView.adapter = adapter
        noteRecycleView.layoutManager = layoutManager
    }

    private fun showDeleteNoteAlertDialog(note: NoteModel, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Suppression de la note ${note.title}")
            .setMessage("Etes-vous certain de vouloir supprimer la note ?")
            .setIcon(android.R.drawable.ic_menu_delete)
            .setPositiveButton("Supprimer") { dialog, _ ->
                dialog.dismiss()
                deleteNote(position)
                displayToast("La note ${note.title} a bien ete supprimee.")
            }
            .setNegativeButton("Annuler", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteNote(position: Int) {
        NoteModel.delete(prefs, position, notes)
        val adapter = noteRecycleView.adapter
        adapter?.notifyItemRemoved(position)
        adapter?.notifyItemRangeChanged(position, adapter.itemCount - 1)
    }

    private fun displayToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemClicked(position: Int) {
        val intent = Intent(this, NoteDetailsActivity::class.java)
        val note = notes[position]
        intent.putExtra(NOTE_TITLE, note.title)
        intent.putExtra(NOTE_DESC, note.desc)
        intent.putExtra(NOTE_POSITION, position)

        noteDetailsActivityResult.launch(intent)
        Log.d("MainActivity", "Le titre est ${notes[position].title}")
    }

    override fun onDeleteNoteClicked(position: Int) {
        showDeleteNoteAlertDialog(notes[position], position)
    }

    companion object {
        fun printNotes(notes: List<NoteModel>): String {
            var contents = ""
            for (note in notes) {
                contents += "[${note.title}],[${note.desc}] \n"
            }
            return "{\n $contents}"
        }
    }
}