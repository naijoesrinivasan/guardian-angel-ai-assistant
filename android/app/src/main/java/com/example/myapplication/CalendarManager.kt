package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CalendarManager(private val context: Context) {
    fun scheduleEvents(events: List<Event>) {
        val contentResolver = context.contentResolver

        for (event in events) {
            val startMillis = getMillisFromDateTime(event.date, event.startTime)
            val endMillis = getMillisFromDateTime(event.date, event.endTime)

            val timeZone = TimeZone.getDefault()
            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.DTEND, endMillis)
                put(CalendarContract.Events.TITLE, event.event)
                put(CalendarContract.Events.DESCRIPTION, "Scheduled event")
                put(CalendarContract.Events.CALENDAR_ID, 3) // Change calendar ID if needed
                put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.id)
            }

            val uri: Uri? = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            Log.d("uritag",uri.toString())// uri now contains the URI of the newly inserted event
        }
    }

    private fun getMillisFromDateTime(date: String, time: String): Long {
        val dateTime = "$date $time"

        // Use the provided date and time format from the response
        val format = SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault())

        try {
            // Attempt to parse with the format that includes the period in time ("p.m.")
            return format.parse(dateTime)?.time ?: 0L
        } catch (e: ParseException) {
            // Handle parsing error
            e.printStackTrace()
        }

        return 0L
    }
}
