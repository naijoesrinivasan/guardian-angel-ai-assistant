package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.view.View
import androidx.core.app.ActivityCompat
import java.util.stream.Collectors
import java.util.stream.IntStream

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}



