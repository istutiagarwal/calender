package com.example.calender.ui


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calender.R
import com.example.calender.source.CalenderRepository
import com.example.calender.ui.CalendarAdapter.OnItemListener
import com.example.calender.util.CalendarUtils
import com.example.calender.util.CalendarUtils.daysInMonthArray
import com.example.calender.util.CalendarUtils.monthYearFromDate
import com.example.calender.util.CalendarUtils.selectedDate
import com.example.calender.util.CalenderViewModelFactory
import com.example.calender.util.SharedPref
import com.example.calender.viewModel.CalenderViewModel
import java.time.LocalDate


class MainActivity : AppCompatActivity(), OnItemListener {
    lateinit var monthYearText: TextView
    private lateinit var viewModel: CalenderViewModel
    private var calendarRecyclerView: RecyclerView? = null
    private lateinit var eventListView: ListView
    private  var userId : Int = -1
    private val sharedPref by lazy {
        SharedPref(this)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userId =  getUserId()
        initWidgets()
       selectedDate = LocalDate.now()
        getViewModel()
        setMonthView()
    }

    private fun getUserId() : Int{
        var id = sharedPref.getUserId("user_id")
        if(id == -1){
            id = Math.random().toInt()
             sharedPref.saveUserId("user_id", id)
        }
        Log.d("istuti", " main $id")
        return id
    }

    private fun getViewModel(){
        val repository = CalenderRepository()
        viewModel = ViewModelProvider(
            this,
            CalenderViewModelFactory(repository)
        )[CalenderViewModel::class.java]
    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)
        eventListView = findViewById(R.id.eventListView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysInMonth: ArrayList<LocalDate?> = daysInMonthArray(selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth , this@MainActivity)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonthAction(view: View?) {
        selectedDate = selectedDate!!.minusMonths(1)
        setMonthView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextMonthAction(view: View?) {
        selectedDate =selectedDate!!.plusMonths(1)
        setMonthView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            selectedDate = date
            setMonthView()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListOfData(userId)
        observeListData()
    }

    private fun observeListData(){
        viewModel.taskListLiveData.observe(this){
            it?.let {
                val eventAdapter = EventAdapter(this, it, viewModel, userId)
                eventListView.adapter = eventAdapter
            }
        }
    }

    fun newEventAction(view: View?) {
        startActivity(Intent(this, EventEditActivity::class.java).apply {
            putExtra("user_id" , userId)
        })
    }
}