package com.example.artistexplorerapp.ui.viewmodel

import java.util.concurrent.TimeUnit

fun FormatDuration(millis: String?): String {
    val durationMs = millis?.toLongOrNull() ?: return ""
    val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) -
            TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%d:%02d", minutes, seconds)
}