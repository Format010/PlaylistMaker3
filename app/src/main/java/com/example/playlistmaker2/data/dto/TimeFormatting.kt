package com.example.playlistmaker2.data.dto

import java.text.SimpleDateFormat
import java.util.Locale

class TimeFormatting() {

    fun simpleDataFormat(formatTime: String) : String {

        if (formatTime != null) {
            return SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(formatTime.toLong())

        } else return SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)

    }

}