package com.example.smartlock.controller

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object NavControllerSingleton {
    @SuppressLint("StaticFieldLeak")
    @Volatile
    private var INSTANCE: NavHostController? = null

    @Composable
    fun getInstance(): NavHostController {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: rememberNavController().also { INSTANCE = it }
        }
    }
}
