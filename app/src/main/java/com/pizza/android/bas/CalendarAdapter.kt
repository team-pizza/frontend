package com.pizza.android.bas

import java.time.Duration
import java.util.*

class CalendarAdapter(mainActivity: MainActivity) {
    fun queryCalendarEvents(start: Date, span: Duration, callback: (List<Event>)->Unit) {
        TODO("Needs implemented")
    }
}

data class Event(val start: Date, val duration: Duration)