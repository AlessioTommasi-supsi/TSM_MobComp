package com.example.smartlock.controller

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import com.example.smartlock.model.DeviceEntry
import com.example.smartlock.model.createCustomRequestQueue
import org.json.JSONObject
import java.net.URLEncoder

class DeviceLogController(application: Application) : AndroidViewModel(application) {

    var devices = mutableStateListOf<DeviceEntry>()
    var errorMessage = mutableStateOf("")

    fun fetchDevices(onComplete: () -> Unit) {
        val context = getApplication<Application>().applicationContext
        val requestQueue = createCustomRequestQueue(context, 300000) // Timeout di 300 secondi
        val url = "http://137.74.196.58/tsm_mob/BACKEND/html/view/get_device.php"

        errorMessage.value = ""

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                val deviceList = Klaxon().parseArray<DeviceEntry>(response)
                devices.clear()
                if (deviceList != null) {
                    devices.addAll(deviceList)
                }
                onComplete()
            },
            { error ->
                Log.e("Volley", "Error loading data: $error")
                errorMessage.value = error.toString()
                onComplete()
            }
        )

        requestQueue.add(request)
    }

    fun sendLogToServer(deviceName: String, state: Boolean) {
        val context = getApplication<Application>().applicationContext
        val url = "http://137.74.196.58/tsm_mob/BACKEND/html/view/put_logs.php?device=${URLEncoder.encode(deviceName, "UTF-8")}&state=${if (state) "1" else "0"}"

        Log.d("DeviceLogController", "Sending log to $url")



        val requestQueue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                Toast.makeText(context, "Log entry created", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(context, "Failed to create log entry: $error", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(request)
    }
}
