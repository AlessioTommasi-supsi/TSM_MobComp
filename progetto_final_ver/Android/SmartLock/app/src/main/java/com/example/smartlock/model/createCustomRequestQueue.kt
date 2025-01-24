package com.example.smartlock.model

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.HttpStack
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.NoCache
import com.android.volley.toolbox.Volley
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun createCustomRequestQueue(context: Context, timeout: Int): RequestQueue {
    val httpStack: HttpStack = object : HurlStack() {
        @Throws(java.io.IOException::class)
        override fun createConnection(url: URL): HttpURLConnection {
            val connection: HttpURLConnection = super.createConnection(url)
            connection.readTimeout = timeout
            connection.connectTimeout = timeout
            return connection
        }
    }

    return RequestQueue(NoCache(), BasicNetwork(httpStack)).apply {
        start()
    }
}
