package com.example.noteapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.model.NoteModel

class NoteAdapter(var notes: List<NoteModel>, var listener: NoteListener): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface NoteListener {
        fun onItemClicked(position: Int)
        fun onDeleteNoteClicked(position: Int)
    }

    class NoteViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val noteTitleTextView: TextView = view.findViewById(R.id.title_text_view)
        val noteDescTextView: TextView = view.findViewById(R.id.desc_text_view)
        val deleteNoteButton: ImageButton = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size
    // equivalent @ override fun getItemCount(): Int { return notes.size }

    // permet de donner des valeurs a nos TextView
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        holder.noteTitleTextView.text = note.title
        holder.noteDescTextView.text = note.desc
        holder.deleteNoteButton.setOnClickListener {
            listener.onDeleteNoteClicked(position)
        }
        holder.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }

        val logs = "Binding ViewHolder, notes: ${MainActivity.printNotes(notes)}"
        Log.d("OnBindNoteAdapter",logs)
    }
}