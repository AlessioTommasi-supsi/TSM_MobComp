package com.example.smartlock.controller

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import com.example.smartlock.model.LogEntry

class LogsController(application: Application) : AndroidViewModel(application) {

    var logs = mutableStateListOf<LogEntry>()
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    fun fetchLogs(deviceName: String) {
        val context = getApplication<Application>().applicationContext
        val requestQueue = Volley.newRequestQueue(context)
        val url = "http://137.74.196.58/tsm_mob/BACKEND/html/view/get_logs.php?device=$deviceName"

        isLoading.value = true
        errorMessage.value = ""

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                val logsList = Klaxon().parseArray<LogEntry>(response)
                logs.clear()
                if (logsList != null) {
                    logs.addAll(logsList)
                }
                isLoading.value = false
            },
            {
                Log.e("Volley", "Error loading data: $it")
                errorMessage.value = it.toString()
                isLoading.value = false
            }
        )

        requestQueue.add(request)
    }
}
