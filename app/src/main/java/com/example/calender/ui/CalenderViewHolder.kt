package com.example.calender.ui


import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calender.R
import com.example.calender.ui.CalendarAdapter.OnItemListener
import java.time.LocalDate

class CalendarViewHolder  constructor(
    itemView: View,
    onItemListener: OnItemListener,
    days: ArrayList<LocalDate?>
) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val days: ArrayList<LocalDate?>
    val parentView: View
    val dayOfMonth: TextView
    private val onItemListener: OnItemListener

    init {
        parentView = itemView.findViewById<View>(R.id.parentView)
        dayOfMonth = itemView.findViewById<TextView>(R.id.cellDayText)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
        this.days = days
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, days[adapterPosition])
    }
}