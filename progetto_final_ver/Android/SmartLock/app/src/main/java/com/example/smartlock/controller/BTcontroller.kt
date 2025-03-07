package com.example.smartlock.controller

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.smartlock.model.BluetoothDeviceData
import java.util.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList

class BTcontroller private constructor(application: Application) : AndroidViewModel(application) {

    var devices = mutableStateListOf<BluetoothDeviceData>()
    var isScanning = mutableStateOf(false)
    var connectedGatt: BluetoothGatt? = null
    private val _receivedResponse = MutableLiveData<String>()
    val receivedResponse: LiveData<String> = _receivedResponse
    val connectedDevices = mutableStateListOf<BluetoothDevice>()
    var servicesDiscovered = mutableStateOf(false)

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            val deviceAddress = gatt?.device?.address ?: return
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("BluetoothScan", "Connected to GATT server for device $deviceAddress.")
                gatt?.discoverServices()
                updateDeviceConnectionStatus(deviceAddress, true)
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("BluetoothScan", "Disconnected from GATT server for device $deviceAddress.")
                updateDeviceConnectionStatus(deviceAddress, false)
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BluetoothScan", "Services discovered.")
                gatt?.services?.forEach { svc ->
                    BTHelper.getInstance(getApplication()).printServicesLogs(svc)
                    devices.find { it.address == gatt.device.address }?.gattServices?.add(svc)
                    servicesDiscovered.value = true
                }
            } else {
                Log.w("BluetoothScan", "onServicesDiscovered received: $status")
            }
        }


        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            Log.d("BluetoothScan", "Characteristic changed: ${characteristic?.uuid}")
            _receivedResponse.postValue(readCharacteristic(characteristic))
        }
    }


    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE).toInt()
                    device?.let {
                        val deviceName = if (it.name.isNullOrEmpty()) "Unknown (${it.address})" else it.name
                        val deviceClass = it.bluetoothClass?.deviceClass ?: BluetoothClass.Device.Major.UNCATEGORIZED
                        val majorDeviceClass = it.bluetoothClass?.majorDeviceClass ?: BluetoothClass.Device.Major.UNCATEGORIZED

                        Log.d("BluetoothScan", "Device found: Name=${deviceName}, Address=${it.address}, RSSI=${rssi}, Class=${deviceClass}, MajorClass=${majorDeviceClass}")
                        val deviceData = BluetoothDeviceData(deviceName, it.address, rssi, deviceClass, majorDeviceClass)
                        if (devices.none { d -> d.address == deviceData.address }) {
                            devices.add(deviceData)
                        }
                    }
                }
                BluetoothDevice.ACTION_NAME_CHANGED -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        devices.find { d -> d.address == it.address }?.let { dev ->
                            dev.name = it.name ?: "Unknown (${it.address})"
                            Log.d("BluetoothScan", "Device name updated: Name=${dev.name}, Address=${it.address}")
                        }
                    }
                }
            }
        }
    }

    init {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED)
        application.registerReceiver(receiver, filter)
    }

    @SuppressLint("MissingPermission")
    fun startScan() {
        Log.d("BluetoothScan", "Starting scan")
        devices.clear()
        isScanning.value = true
        if (bluetoothAdapter?.isEnabled == true) {
            bluetoothAdapter.startDiscovery()
        } else {
            Log.e("BluetoothScan", "Bluetooth is not enabled")
            isScanning.value = false
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        Log.d("BluetoothScan", "Stopping scan")
        isScanning.value = false
        bluetoothAdapter?.cancelDiscovery()
        val gatt = connectedGatt
        if (gatt != null) {
            gatt.disconnect()
            connectedGatt = null
        }
        devices.forEach {
            it.isConnected = false
            it.gattServices.clear()
        }
        connectedDevices.clear()

    }

    @SuppressLint("MissingPermission")
    fun connectDevice(context: Context, device: BluetoothDevice) {
        connectedGatt = device.connectGatt(context, false, gattCallback)
        connectedDevices.add(device)
    }

    @SuppressLint("MissingPermission")
    fun disconnectDevice(address: String) {
        val gatt = connectedGatt?.takeIf { it.device.address == address }
        gatt?.disconnect()
        updateDeviceConnectionStatus(address, false)
        devices.find { it.address == address }?.gattServices?.clear()
        devices.find { it.address == address }?.isConnected = false

    }

    private fun updateDeviceConnectionStatus(address: String, isConnected: Boolean) {
        devices.find { it.address == address }?.isConnected = isConnected
        if (isConnected) {
            connectedDevices.add(bluetoothAdapter?.getRemoteDevice(address)!!)
        } else {
            connectedDevices.removeAll { it.address == address }
        }
    }

    fun isDeviceConnected(address: String): Boolean {
        return connectedDevices.any { it.address == address }
    }



    @SuppressLint("MissingPermission")
    fun enableNotification(characteristic: BluetoothGattCharacteristic) {
        try {
            val cccdUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
            connectedGatt?.setCharacteristicNotification(characteristic, true)
            val descriptor = characteristic.getDescriptor(cccdUuid)
            if (descriptor != null) {
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                val success = connectedGatt?.writeDescriptor(descriptor)
                if (success == true) {
                    Log.d("BluetoothScan", "Notification enabled for characteristic: ${characteristic.uuid}")
                    Toast.makeText(getApplication(), "Notification enabled for characteristic: ${characteristic.uuid}", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("BluetoothScan", "Error enabling notification for characteristic: ${characteristic.uuid}")
                    Toast.makeText(getApplication(), "Error enabling notification for characteristic: ${characteristic.uuid}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("BluetoothScan", "Descriptor not found for characteristic: ${characteristic.uuid}")
                Toast.makeText(getApplication(), "Descriptor not found for characteristic: ${characteristic.uuid}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("BluetoothScan", "Error enabling notification for characteristic: ${characteristic.uuid}")
            Toast.makeText(getApplication(), "Error enabling notification for characteristic: ${characteristic.uuid}", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unregisterReceiver(receiver)
    }

    @SuppressLint("MissingPermission")
    fun getBluetoothDevice(address: String): BluetoothDevice? {
        Log.d("BluetoothScan", "Looking for device with address $address")
        return bluetoothAdapter?.bondedDevices?.find { it.address == address } ?: devices.find { it.address == address }?.let { deviceData ->
            bluetoothAdapter?.getRemoteDevice(deviceData.address)
        }
    }




    @SuppressLint("MissingPermission")
    fun disableNotification(characteristic: BluetoothGattCharacteristic){
        if (connectedGatt?.setCharacteristicNotification(characteristic, false) == true) {
            Log.d("BluetoothScan", "Notification disabled for characteristic: ${characteristic.uuid}")
            Toast.makeText(getApplication(), "Notification disabled for characteristic: ${characteristic.uuid}", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("BluetoothScan", "Error disabling notification for characteristic: ${characteristic.uuid}")
            Toast.makeText(getApplication(), "Error disabling notification for characteristic: ${characteristic.uuid}", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic?): String {
        _receivedResponse.postValue("Reading characteristic: ${characteristic?.uuid}")
        return BTHelper.getInstance(getApplication()).readCh(characteristic, connectedGatt!!)
    }


    companion object {
        @Volatile
        private var INSTANCE: BTcontroller? = null

        fun getInstance(application: Application): BTcontroller {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BTcontroller(application).also { INSTANCE = it }
            }
        }
    }
}