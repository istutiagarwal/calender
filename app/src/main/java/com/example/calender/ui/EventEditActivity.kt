package com.example.calender.ui


import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.calender.R
import com.example.calender.source.CalenderRepository
import com.example.calender.util.CalendarUtils
import com.example.calender.util.CalendarUtils.formattedDate
import com.example.calender.util.CalendarUtils.formattedTime
import com.example.calender.util.CalenderViewModelFactory
import com.example.calender.util.Event
import com.example.calender.viewModel.CalenderViewModel
import java.time.LocalTime


class EventEditActivity : AppCompatActivity() {
    private var eventNameTitleET: EditText? = null
    private var eventNameDescriptionET: EditText? = null
    private var eventDateTV: TextView? = null
    private var eventTimeTV: TextView? = null
    private lateinit var viewModel: CalenderViewModel
    private  var userId : Int = -1
    private lateinit var time: LocalTime
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edit)
        userId = intent.getIntExtra("user_id" , -1)
        initWidgets()
        getViewModel()
        time = LocalTime.now()
        eventDateTV!!.text = "Date: " + formattedDate(CalendarUtils.selectedDate!!)
        eventTimeTV!!.text = "Time: " + formattedTime(time)
    }

    private fun getViewModel(){
        val repository = CalenderRepository()
        viewModel = ViewModelProvider(
            this,
            CalenderViewModelFactory(repository)
        )[CalenderViewModel::class.java]
    }

    private fun initWidgets() {
        eventNameTitleET = findViewById<EditText>(R.id.eventNameET)
        eventDateTV = findViewById<TextView>(R.id.eventDateTV)
        eventTimeTV = findViewById<TextView>(R.id.eventTimeTV)
        eventNameDescriptionET = findViewById(R.id.eventDescriptionET)
    }

    fun saveEventAction(view: View?) {
        val eventName = eventNameTitleET!!.text.toString()
        val eventDescription = eventNameDescriptionET!!.text.toString()
        val newEvent = Event(eventName, CalendarUtils.selectedDate, time)
        viewModel.storeData(userId, eventName , eventDescription)
        Event.eventsList.add(newEvent)
        finish()
    }
}