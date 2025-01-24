package com.example.smartlock.model


class DeviceList(val devices : MutableList<DeviceEntry>) {}


class DeviceEntry(val device : String, val num_logs: Int) {
    val device_name : String
        get() {
            return device
        }

    val number_of_logs : Int
        get() {
            return num_logs
        }

}