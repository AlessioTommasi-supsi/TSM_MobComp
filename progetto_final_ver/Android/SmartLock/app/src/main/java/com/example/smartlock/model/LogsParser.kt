package com.example.smartlock.model

class LogsList(val logs: MutableList<LogEntry>)

class LogEntry(
    val state: Int,
    val date: String
)
