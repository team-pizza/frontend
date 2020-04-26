package com.pizza.android.bas.networking

import com.pizza.android.bas.Event
import java.util.*

data class EventList(val events: List<Event>, val start: Date, val spanInMinutes: Long)