package com.example.calender.util

import java.time.LocalDate
import java.time.LocalTime


data class Event(var name: String, var date: LocalDate, var time: LocalTime) {

    companion object {
        var eventsList = ArrayList<Event>()
    }
}