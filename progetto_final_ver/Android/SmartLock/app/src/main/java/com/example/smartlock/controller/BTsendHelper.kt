package com.example.smartlock.controller

import android.bluetooth.BluetoothGattService
import android.util.Log
import java.util.UUID

class BTsendHelper {
    /*
    fun sendCommandToConnectedDevice(command: String, service: BluetoothGattService, characteristicUuid: String) {
        /*
        Log.d("BluetoothScan", "DEFAULT")
        sendDefaultCommandToConnectedDevice()
        Log.d("BluetoothScan", "END DEFAULT: $command")
        */
        val connectedGatt = this.connectedGatt
        if (connectedGatt == null) {
            Log.e("BluetoothScan", "No connected device found.")
            return
        }

        // Recupera tutti i servizi
        val services = connectedGatt.services
        if (services.isEmpty()) {
            Log.e("BluetoothScan", "No services found on the connected device.")
            return
        }

        // Itera su tutti i servizi
        for (service in services) {
            Log.d("BluetoothScan", "Service UUID: ${service.uuid}")

            // Stampa tutte le caratteristiche disponibili per ogni servizio
            for (characteristic in service.characteristics) {
                Log.d("BluetoothScan", "Sending message to Characteristic UUID: ${characteristic.uuid}")
                sendCommand(command, service, characteristic.uuid.toString())
            }

        }
    }

    fun sendDefaultCommandToConnectedDevice() {
        val commandServiceUuid = "01000000-0000-1000-8000-00805f9b34fb"
        val commandCharacteristicUuid = "02000000-0000-1000-8000-00805f9b34fb"
        val command = "default"
        val connectedGatt = this.connectedGatt

        Log.d("BluetoothScan", "Sending default command to connected device   ")

        val service =  BluetoothGattService(UUID.fromString(commandServiceUuid), BluetoothGattService.SERVICE_TYPE_PRIMARY)

        sendCommand(command, service, commandCharacteristicUuid)
    }

    @SuppressLint("MissingPermission")
    fun sendCommand(command: String, service: BluetoothGattService, characteristicUUIDStr: String = "") {
        Log.d("BluetoothScan", "Sending command: $command")
        val characteristic = service.getCharacteristic(characteristicUUIDStr)
        if (characteristic == null) {
            Log.e("BluetoothScan", "Characteristic not found.")
            service.characteristics.forEach { char ->
                Log.e("BluetoothScan", " characteristic: ${char.uuid}"+ " message sent:  $command")
                var serviceCharacteristic = service.getCharacteristic(char.uuid)
                sendCommand(command, service, serviceCharacteristic.uuid.toString())
            }
            return
        }
        characteristic.value = command.toByteArray(Charsets.UTF_8)
        val success = connectedGatt?.writeCharacteristic(characteristic) ?: false

        if (success) {
            Log.d("BluetoothScan", "Command sent successfully.")
            //abilita le notifiche
            val responceCharacteristic = service.getCharacteristic(characteristicUUIDStr)
            if (responceCharacteristic != null) {
               enableNotification(responceCharacteristic)
            }else{
                Log.e("BluetoothScan", "error in setting notification")
            }
        } else {
            Log.e("BluetoothScan", "Failed to send command.")
        }
    }

     */
}