package com.pizza.android.bas

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.provider.CalendarContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class CalendarAdapter(mainActivity: MainActivity) {
    private val mainActivity = mainActivity
    private var callback: ((List<Event>)->Unit)? = null
    private var backgroundThread: Thread? = null
    private var start: Date? = null
    private var span: Long? = null

    private val READ_PERMISSION_REQUEST = 9901

    /**
     * Queries the user's default calendar for all events within [start, start+duration].
     * callback is then called when the data is ready.
     */
    fun queryCalendarEvents(start: Date, span: Long, callback: (List<Event>)->Unit) {
        // Function has already been called and is awaiting result, don't overwrite it
        if(this.callback != null || this.start!=null || this.span!=null) return

        this.callback = callback
        this.start = start
        this.span = span

        if(ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity, arrayOf(Manifest.permission.READ_CALENDAR), READ_PERMISSION_REQUEST)
        } else {
            performQuery()
        }
    }

    private fun eventsOverlap(a: Event, b: Event): Boolean {
        val aStartMilliseconds = a.start.time
        val aEndMilliseconds = aStartMilliseconds + (a.durationInMinutes*60*1000)

        val bStartMilliseconds = b.start.time
        val bEndMilliseconds = bStartMilliseconds + (b.durationInMinutes*60*1000)

        return (aStartMilliseconds in bStartMilliseconds..bEndMilliseconds || aEndMilliseconds in bStartMilliseconds..bEndMilliseconds) ||
                (bStartMilliseconds in aStartMilliseconds..aEndMilliseconds || bEndMilliseconds in aStartMilliseconds..aEndMilliseconds)
    }

    /**
     * Assumes firstEvent begins before secondEvent and that they overlap
     */
    private fun mergeEvents(firstEvent: Event, secondEvent: Event): Event {
        val caseOneDuration = firstEvent.durationInMinutes * 60 * 1000
        val caseTwoDuration = (secondEvent.start.time - firstEvent.start.time) + (secondEvent.durationInMinutes*60*1000)

        val newDuration: Int = if(caseOneDuration > caseTwoDuration) { caseOneDuration/(60*1000) } else { (caseTwoDuration/(60*1000)).toInt() }

        return Event(firstEvent.start, newDuration)
    }

    /**
     * Queries the user's calendars on a background thread for all event instances within the time frame specified.
     *
     * Permission to read from the user's calendars must be granted before this function is called!
     */
    private fun performQuery() {
        backgroundThread = Thread {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)

            val start = this.start ?: Date()
            val span = this.span ?: 0
            val callback = this.callback ?: {}

            val projection = arrayOf(CalendarContract.Instances.BEGIN,  CalendarContract.Instances.END, CalendarContract.Instances.TITLE, CalendarContract.Instances.DESCRIPTION, CalendarContract.Instances.CALENDAR_DISPLAY_NAME)

            val cr = mainActivity.contentResolver

            val spanInMilliseconds = span * 1000 * 60

            val uriBuilder = Uri.parse(CalendarContract.Instances.CONTENT_URI.toString()).buildUpon()
            ContentUris.appendId(uriBuilder, start.time)
            ContentUris.appendId(uriBuilder, start.time + spanInMilliseconds)

            var result = MutableList(0) { Event(Date(), 0) }
            var rawEvents = MutableList(0) { Event(Date(), 0) }

            try {
                val cursor = cr.query(uriBuilder.build(), projection, null, null, null)
                while(cursor != null && cursor.moveToNext()) {
                    val begin = cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.BEGIN)).toLong()
                    val end = cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.END)).toLong()

                    // Used for debugging purposes
                    val title = cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.TITLE))
                    val description = cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.DESCRIPTION))
                    val calendarName = cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.CALENDAR_DISPLAY_NAME))


                    val startDate = Date(begin)
                    val durationInMilliseconds = end - begin
                    val durationInMinutes = durationInMilliseconds / (1000*60)

                    rawEvents.add(Event(startDate, durationInMinutes.toInt()))

                }

                rawEvents.sortBy { it.start }

                while(rawEvents.size > 1) {
                    if(eventsOverlap(rawEvents[0], rawEvents[1])) {
                        val mergedEvent = mergeEvents(rawEvents[0], rawEvents[1])
                        rawEvents.removeAt(0)
                        rawEvents[0] = mergedEvent
                    } else {
                        result.add(rawEvents.removeAt(0))
                    }
                }
                if(rawEvents.size > 0) {
                    result.add(rawEvents.removeAt(0))
                }

            } catch (e: Exception) {


            }



            // Execute the callback from the main thread
            val mainHandler = Handler(mainActivity.mainLooper)
            mainHandler.post {
                callback(result)
            }
        }
        backgroundThread?.start()
    }

    fun handlePermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == READ_PERMISSION_REQUEST) {
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                performQuery()
            } else {
                // Permission was denied. Nothing we can do now. Reset callback.
                callback = null
                start = null
                span = null
            }
        }
    }
}

data class Event(val start: Date, val durationInMinutes: Int)