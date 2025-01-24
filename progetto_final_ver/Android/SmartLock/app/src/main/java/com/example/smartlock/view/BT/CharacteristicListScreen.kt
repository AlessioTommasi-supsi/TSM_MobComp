package com.example.smartlock.view.BT

import com.example.smartlock.R
import android.app.Application
import android.bluetooth.BluetoothGattCharacteristic
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartlock.controller.BTcontroller

@Composable
fun CharacteristicListScreen(navController: NavHostController, deviceAddress: String, serviceUuid: String) {
    val context = LocalContext.current
    val bluetoothViewModel = remember { BTcontroller.getInstance(context.applicationContext as Application) }

    val device = bluetoothViewModel.devices.find { it.address == deviceAddress } ?: return
    val selectedService = device.gattServices.find { it.uuid.toString() == serviceUuid } ?: return

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Service: $serviceUuid", style = MaterialTheme.typography.bodyLarge)
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            items(selectedService.characteristics) { characteristic ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        //navController.navigate("parsingMethodList/${deviceAddress}/${serviceUuid}/${characteristic.uuid}")
                    }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = characteristic.uuid.toString())
                        val properties = characteristic.properties
                        val isReadable = properties and BluetoothGattCharacteristic.PROPERTY_READ != 0
                        val isWritable = properties and BluetoothGattCharacteristic.PROPERTY_WRITE != 0
                        val isWritableWithoutResponse = properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0
                        val isNotifiable = properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0

                        Spacer(modifier = Modifier.height(4.dp))

                        // Aggiungi bottoni per le proprietà
                        Row {
                            if (isReadable) {
                                IconButton(onClick = {
                                    val res = bluetoothViewModel.readCharacteristic(characteristic)
                                    Toast.makeText(
                                        context,
                                        "Reading: " + res,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_download),
                                        contentDescription = "Readable"
                                    )
                                }
                            }
                            if (isWritable) {
                                IconButton(onClick = {
                                    navController.navigate("parsingMethodList/${deviceAddress}/${serviceUuid}/${characteristic.uuid}")
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_upload),
                                        contentDescription = "Writable"
                                    )
                                }
                            }
                            if (isWritableWithoutResponse) {
                                IconButton(onClick = {
                                    navController.navigate("parsingMethodList/${deviceAddress}/${serviceUuid}/${characteristic.uuid}")
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_cloud),
                                        contentDescription = "Writable without response"
                                    )
                                }
                            }
                            if (isNotifiable) {
                                var notificationEnabled by remember { mutableStateOf(false) }

                                IconButton(onClick = {
                                    notificationEnabled = !notificationEnabled
                                    if (notificationEnabled) {
                                        bluetoothViewModel.enableNotification(characteristic)
                                        Toast.makeText(context, "Notification enabled", Toast.LENGTH_SHORT).show()
                                    } else {
                                        bluetoothViewModel.disableNotification(characteristic)
                                        Toast.makeText(context, "Notification disabled", Toast.LENGTH_SHORT).show()
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_notify),
                                        contentDescription = "Notifiable",
                                        tint = if (notificationEnabled) Color.Red else LocalContentColor.current
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Back to Service List")
        }
    }
}
