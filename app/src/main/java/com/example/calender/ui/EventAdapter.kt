package com.example.calender.ui


import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.calender.R
import com.example.calender.source.Task
import com.example.calender.source.TasksList
import com.example.calender.viewModel.CalenderViewModel


//class EventAdapter(
//    context: Context,
//    private val events: TasksList,
//    private val viewModel: CalenderViewModel,
//    private val userId: Int
//) :
//    ArrayAdapter<Task>(context, 0, (events.tasks as MutableList<Task>) ) {
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var cv = convertView
//        if (cv == null) cv =
//            LayoutInflater.from(context).inflate(R.layout.event_cell, parent, false)
//
//        val eventCellTV = cv?.findViewById<TextView>(R.id.eventCellTitleTV)
//        val deleteBin = cv?.findViewById<ImageButton>(R.id.deleteBin)
//        val eventCellDescriptionTV = cv?.findViewById<TextView>(R.id.eventCellDescriptionTV)
//
//        eventCellTV?.text = events.tasks[position].task_detail.title
//        eventCellDescriptionTV?.text = events.tasks[position].task_detail.description
//        deleteBin?.setOnClickListener {
//            viewModel.deleteEvent(userId = userId, taskId = events.tasks[position].task_id)
//            Toast.makeText(parent.context, "Deleted", Toast.LENGTH_SHORT).show()
//        }
//        return cv!!
//    }
//}


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
        holder.eventCellTV?.text = events.tasks[position].task_detail.title
        holder.eventCellDescriptionTV?.text = events.tasks[position].task_detail.description
        holder.deleteBin?.setOnClickListener {
            viewModel.deleteEvent(userId = userId, taskId = events.tasks[position].task_id)
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
        }
    }
}


class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val eventCellTV = itemView.findViewById<TextView>(R.id.eventCellTitleTV)
    val deleteBin = itemView.findViewById<ImageButton>(R.id.deleteBin)
    val eventCellDescriptionTV = itemView.findViewById<TextView>(R.id.eventCellDescriptionTV)

}