package com.example.calender.ui


import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.calender.R
import com.example.calender.util.CalendarUtils
import java.time.LocalDate

class CalendarAdapter(
    private val days: ArrayList<LocalDate?>,
    private val onItemListener: OnItemListener
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calender_cell, parent, false)
        val layoutParams = view.layoutParams
        if (days.size > 15)
            layoutParams.height = (parent.height * 0.166666666).toInt() else
            layoutParams.height = parent.height
        return CalendarViewHolder(view, onItemListener, days)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = days[position]
        holder.dayOfMonth.text = date?.dayOfMonth.toString()
        if (date == CalendarUtils.selectedDate) holder.parentView.setBackgroundColor(Color.LTGRAY)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate?)
    }
}