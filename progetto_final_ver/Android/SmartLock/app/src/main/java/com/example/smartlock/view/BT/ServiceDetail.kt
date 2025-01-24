package com.example.smartlock.view.BT

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartlock.controller.NavControllerSingleton
import com.example.smartlock.controller.BTcontroller
import java.nio.ByteBuffer

@Composable
fun BTScan(bT: BTcontroller, deviceAddress: String) {
    bT.disconnectDevice(deviceAddress)
    NavControllerSingleton.getInstance().navigate("scan")
}


@SuppressLint("NotConstructor", "MissingPermission")
@Composable
fun serviceDetail(deviceAddress: String) {
    var message by remember { mutableStateOf("") }
    var selectedService by remember { mutableStateOf<BluetoothGattService?>(null) }
    var selectedCharacteristic by remember { mutableStateOf<BluetoothGattCharacteristic?>(null) }
    var selectedParseMethod by remember { mutableStateOf("ByteArray") }
    val context = LocalContext.current
    var bluetoothViewModel = remember { BTcontroller.getInstance(context.applicationContext as Application) }
    val device = bluetoothViewModel.devices.find { it.address == deviceAddress } ?: return
    var isConnected by remember { mutableStateOf(true) }

    Column {
        // Lista di servizi
        Text("Select Service:")
        LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            items(device.gattServices) { service ->
                Text(
                    text = service.uuid.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedService = service
                            selectedCharacteristic = null
                        }
                        .padding(8.dp)
                )
            }
        }

        // Mostra descrittore del servizio
        selectedService?.let { service ->
            Text("Service UUID: ${service.uuid}")
            Spacer(modifier = Modifier.height(8.dp))

            // Lista di caratteristiche
            Text("Select Characteristic:")
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                items(service.characteristics) { characteristic ->
                    Text(
                        text = characteristic.uuid.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedCharacteristic = characteristic
                            }
                            .padding(8.dp)
                    )
                }
            }

            selectedCharacteristic?.let { characteristic ->
                val properties = characteristic.properties
                val canRead = properties and BluetoothGattCharacteristic.PROPERTY_READ != 0
                val canWrite = properties and BluetoothGattCharacteristic.PROPERTY_WRITE != 0 || properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0

                Text("Properties: ${if (canRead) "Read" else ""} ${if (canWrite) "Write" else ""}")

                Spacer(modifier = Modifier.height(8.dp))

                // Lista per metodi di parsing
                Text("Select Parsing Method:")
                val parsingMethods = listOf("ByteArray", "Unsigned Int", "Bool", "UTF8")
                LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    items(parsingMethods) { method ->
                        Text(
                            text = method,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedParseMethod = method
                                }
                                .padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Enter message") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val command = when (selectedParseMethod) {
                            "ByteArray" -> message.toByteArray(Charsets.UTF_8)
                            "Unsigned Int" -> message.toUIntOrNull()?.let { ByteBuffer.allocate(4).putInt(it.toInt()).array() } ?: ByteArray(0)
                            "Bool" -> if (message.toBoolean()) byteArrayOf(1) else byteArrayOf(0)
                            "UTF8" -> message.toByteArray(Charsets.UTF_8)
                            else -> message.toByteArray(Charsets.UTF_8)
                        }
                        selectedCharacteristic?.let {
                            it.value = command
                            val success = bluetoothViewModel.connectedGatt?.writeCharacteristic(it) ?: false
                            if (success) {
                                Toast.makeText(context, "Message sent successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to send message", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    enabled = message.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Send")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {

                    isConnected = false //torno a schermata di scansione
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Disconnetti")
            }
            if (!isConnected) {
                Toast.makeText(context, "isConnected = false! ", Toast.LENGTH_SHORT).show()
                Log.d("isConnected", "isConnected = false! ")
                BTScan(bT = bluetoothViewModel, deviceAddress = deviceAddress)
            }
        }


    }



}
