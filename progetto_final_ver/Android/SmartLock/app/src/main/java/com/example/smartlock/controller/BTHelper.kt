package com.example.smartlock.controller

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.util.Log
import androidx.lifecycle.AndroidViewModel

class BTHelper private constructor(application: Application) : AndroidViewModel(application){
    @SuppressLint("MissingPermission")
    fun readCh(characteristic: BluetoothGattCharacteristic?, connectedGatt: BluetoothGatt): String {
        if (characteristic == null) {
            Log.e("BluetoothScan", "Characteristic is null.")
            return "Characteristic is null."
        }

        // Leggi il valore della caratteristica
        val gattSuccess = connectedGatt?.readCharacteristic(characteristic) ?: false
        if (!gattSuccess) {
            Log.e("BluetoothScan", "Failed to read characteristic.")
            return "Failed to read characteristic."
        }

        val value = characteristic.value

        // Gestisci diversi tipi di dati
        val readValue = when {
            value == null -> {
                "Value is null."
            }
            value.isEmpty() -> {
                "Value is empty."
            }
            value.size == 1 -> {
                val booleanValue = value[0].toInt() != 0
                "Boolean value: $booleanValue"
            }
            else -> {
                try {
                    val stringValue = String(value, Charsets.UTF_8)
                    " $stringValue"
                } catch (e: Exception) {
                    val byteArrayValue = value.joinToString(" ") { byte -> "%02x".format(byte) }
                    "Byte array value: $byteArrayValue"
                }
            }
        }

        return readValue
    }

    fun printServicesLogs(svc: BluetoothGattService) ={
        Log.d("BluetoothScan", "Discovered service: ${svc.uuid}" + " type: ${svc.type}")
        svc.characteristics.forEach { char ->
            val properties = char.properties
            val isReadable = properties and BluetoothGattCharacteristic.PROPERTY_READ != 0
            val isWritable = properties and BluetoothGattCharacteristic.PROPERTY_WRITE != 0
            val isWritableWithoutResponse = properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0

            Log.d("BluetoothScan", "Discovered characteristic: ${char.uuid}")
            Log.d("BluetoothScan", "Is Readable: $isReadable")
            Log.d("BluetoothScan", "Is Writable: $isWritable")
            Log.d("BluetoothScan", "Is Writable Without Response: $isWritableWithoutResponse")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: BTHelper? = null

        fun getInstance(application: Application): BTHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BTHelper(application).also { INSTANCE = it }
            }
        }
    }
}