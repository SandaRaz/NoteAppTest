package com.example.noteapp.model

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NoteModel(var title: String, var desc: String) {
    fun save(prefs: SharedPreferences, position: Int,notes: MutableList<NoteModel>) {
        notes.add(position, this)
        val gson = Gson()
        val jsonNotes = gson.toJson(notes)

        prefs.edit().putString("notes", jsonNotes).apply()
    }

    fun save(prefs: SharedPreferences, notes: MutableList<NoteModel>) {
        save(prefs, notes.count(), notes)
    }

    fun update(prefs: SharedPreferences, position: Int, notes: MutableList<NoteModel>) {
        if(position != -1){
            notes[position].title = this.title
            notes[position].desc = this.desc

            val gson = Gson()
            val jsonNotes = gson.toJson(notes)

            prefs.edit().putString("notes", jsonNotes).apply()
        }
    }

    companion object {
        fun delete(prefs: SharedPreferences, position: Int, notes: MutableList<NoteModel>) {
            if(position != -1){
                notes.removeAt(position)

                val gson = Gson()
                val jsonNotes = gson.toJson(notes)

                prefs.edit().putString("notes", jsonNotes).apply()
            }
        }

        fun getAll(prefs: SharedPreferences): MutableList<NoteModel> {
            val gson = Gson()
            val jsonNotes = prefs.getString("notes", "[]")

            val notes: MutableList<NoteModel> = gson.fromJson(jsonNotes, object: TypeToken<MutableList<NoteModel>>(){}.type)
            return notes
        }
    }
}