package com.example.calender.ui


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.FloatProperty
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calender.R
import com.example.calender.ui.CalendarAdapter.OnItemListener
import com.example.calender.util.CalendarUtils.daysInMonthArray
import com.example.calender.util.CalendarUtils.monthYearFromDate
import com.example.calender.util.CalendarUtils.selectedDate
import com.example.calender.util.SharedPref
import com.example.calender.viewModel.CalenderViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.abs
import java.time.LocalDate
import java.util.Calendar


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnItemListener, GestureDetector.OnGestureListener {
    private lateinit var monthYearText: TextView
    private lateinit var viewModel: CalenderViewModel
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var eventListView: RecyclerView
    private  var userId : Int = -1
    private lateinit var datePickerDialog : DatePickerDialog
    private val sharedPref by lazy {
        SharedPref(this)
    }
    lateinit var gestureDetector: GestureDetector
    var x2:Float = 0.0f
    var x1:Float = 0.0f
    var y2:Float = 0.0f
    var y1:Float = 0.0f

    companion object{
        const val MINIMUM_DISTANCE = 25
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
        gestureDetector = GestureDetector(this , this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        when(event?.action){
            0 ->{
                    x1 = event.x
                    y1 = event.y
            }
            1 ->{
                    x2 = event.x
                    y2 = event.y

                val valueX: Float= x2-x1

                if(abs(valueX) > MINIMUM_DISTANCE){
                    if(x2 > x1){
                        selectedDate =selectedDate.plusMonths(1)
                        setMonthView()
                    }
                    else{
                        selectedDate = selectedDate.minusMonths(1)
                        setMonthView()
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getUserId() : Int{
        var id = sharedPref.getUserId("user_id")
        if(id == -1){
            id = Math.random().toInt()
             sharedPref.saveUserId("user_id", id)
        }
        return id
    }

    private fun getViewModel(){
        viewModel = ViewModelProvider(
            this
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
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonthAction(view: View?) {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextMonthAction(view: View?) {
        selectedDate =selectedDate.plusMonths(1)
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
                eventListView.layoutManager = LinearLayoutManager(this)
                eventListView.adapter = eventAdapter
            }

        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.item_divider)
            ?.let { divider.setDrawable(it) }
        eventListView.addItemDecoration(divider)

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
                var mon = month
                mon += 1
                val date = makeDateString(mon, year)
                monthYearText.text = date
                selectedDate = LocalDate.of(year , mon , day)
                setMonthView()
            }
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
    }

    private fun makeDateString( month: Int, year: Int): String {
        return getMonthFormat(month)  + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        return when (month) {
            1 -> "JAN"
            2 -> "FEB"
            3 -> "MAR"
            4 -> "APR"
            5 -> "MAY"
            6 -> "JUN"
            7 -> "JUL"
            8 -> "AUG"
            9 -> "SEP"
            10 -> "OCT"
            11 -> "NOV"
            12 -> "DEC"
            else -> "JAN"
        }
    }
    fun openDatePicker(view: View?) {
        datePickerDialog.show()
    }

    override fun onDown(e: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
       return false
    }

    override fun onLongPress(e: MotionEvent) {
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
       return false
    }
}