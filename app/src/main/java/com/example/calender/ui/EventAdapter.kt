package com.example.calender.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.calender.R
import com.example.calender.source.TasksList
import com.example.calender.viewModel.CalenderViewModel

class EventAdapter(
    private val context: Context,
    private val events: TasksList,
    private val viewModel: CalenderViewModel,
    private val userId: Int
) : RecyclerView.Adapter<EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.event_cell, parent, false)
        return EventViewHolder(view)
    }

    override fun getItemCount(): Int {
        return events.tasks.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.eventCellTV.text = events.tasks[position].task_detail.title
        holder.eventCellDescriptionTV.text = events.tasks[position].task_detail.description
        holder.deleteBin.setOnClickListener {
            viewModel.deleteEvent(userId = userId, taskId = events.tasks[position].task_id)
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
        }
    }
}

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val eventCellTV: TextView = itemView.findViewById(R.id.eventCellTitleTV)
    val deleteBin: ImageButton = itemView.findViewById(R.id.deleteBin)
    val eventCellDescriptionTV: TextView = itemView.findViewById(R.id.eventCellDescriptionTV)
}