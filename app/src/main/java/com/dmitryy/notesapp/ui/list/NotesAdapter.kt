package com.dmitryy.notesapp.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.data.Note

class NotesAdapter(
    private val onItemClick: (Note) -> Unit
) : ListAdapter<Note, NotesAdapter.NoteViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NoteViewHolder(itemView: View, val onItemClick: (Note) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        private val contentTextView: TextView = itemView.findViewById(R.id.text_view_content)
        private val timestampTextView: TextView = itemView.findViewById(R.id.text_view_timestamp)

        fun bind(note: Note) {
            titleTextView.text = note.title
            contentTextView.text = note.content
            
            val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
            timestampTextView.text = dateFormat.format(java.util.Date(note.createdAt))
            
            itemView.setOnClickListener { onItemClick(note) }
        }
    }

    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}
