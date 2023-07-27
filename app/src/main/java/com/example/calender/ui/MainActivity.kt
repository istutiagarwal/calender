package com.example.calender.ui


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calender.R
import com.example.calender.source.CalenderRepository
import com.example.calender.ui.CalendarAdapter.OnItemListener
import com.example.calender.util.CalendarUtils.daysInMonthArray
import com.example.calender.util.CalendarUtils.monthYearFromDate
import com.example.calender.util.CalendarUtils.selectedDate
import com.example.calender.util.CalenderViewModelFactory
import com.example.calender.util.SharedPref
import com.example.calender.viewModel.CalenderViewModel
import java.time.LocalDate
import java.util.Calendar


class MainActivity : AppCompatActivity(), OnItemListener {
    private lateinit var monthYearText: TextView
    private lateinit var viewModel: CalenderViewModel
    private var calendarRecyclerView: RecyclerView? = null
    private lateinit var eventListView: RecyclerView
    private  var userId : Int = -1
    private lateinit var datePickerDialog : DatePickerDialog
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
        initDatePicker()
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
        //onSwipe()
    }

    private fun observeListData(){
        viewModel.taskListLiveData.observe(this){
            it?.let {
                val eventAdapter = EventAdapter(this, it, viewModel, userId)
                eventListView.layoutManager = LinearLayoutManager(this)
                eventListView.adapter = eventAdapter
            }
        }
    }

    fun newEventAction(view: View?) {
        startActivity(Intent(this, EventEditActivity::class.java).apply {
            putExtra("user_id" , userId)
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDatePicker() {
        val dateSetListener =
            OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month += 1
                val date = makeDateString(month, year)
                monthYearText.text = date
                selectedDate = LocalDate.of(year , month , day)
                setMonthView()
            }
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
    }

    private fun makeDateString( month: Int, year: Int): String? {
        return getMonthFormat(month)  + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "JAN"
        if (month == 2) return "FEB"
        if (month == 3) return "MAR"
        if (month == 4) return "APR"
        if (month == 5) return "MAY"
        if (month == 6) return "JUN"
        if (month == 7) return "JUL"
        if (month == 8) return "AUG"
        if (month == 9) return "SEP"
        if (month == 10) return "OCT"
        if (month == 11) return "NOV"
        return if (month == 12) "DEC" else "JAN"

        //default should never happen
    }

    fun openDatePicker(view: View?) {
        datePickerDialog.show()
    }
}