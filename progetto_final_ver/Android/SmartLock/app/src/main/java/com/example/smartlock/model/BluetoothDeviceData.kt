package com.example.smartlock.model

import android.bluetooth.BluetoothGattService

data class BluetoothDeviceData(
    var name: String,
    val address: String,
    val signalStrength: Int,
    val deviceClass: Int,
    val majorDeviceClass: Int,
    var isConnected: Boolean = false,
    var gattServices: MutableList<BluetoothGattService> = mutableListOf()
)