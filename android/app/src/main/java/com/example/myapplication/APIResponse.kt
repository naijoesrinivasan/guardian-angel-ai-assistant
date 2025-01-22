package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("date")
    val date: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("event")
    val event: String
)

data class Task(
    @SerializedName("events")
    val events: List<Event>
)
data class ApiResponse(
    val task:Task?,
    val email:String?,
    val text:String?,
    val song:String?
)
