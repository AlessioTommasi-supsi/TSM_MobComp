package com.example.smartlock.view.BT

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartlock.controller.BTcontroller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
@SuppressLint("MissingPermission")
@Composable
fun ParsingMethodListScreen(
    navController: NavHostController,
    deviceAddress: String,
    serviceUuid: String,
    characteristicUuid: String
) {
    var message by remember { mutableStateOf("") }
    var selectedParseMethod by remember { mutableStateOf("ByteArray") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bluetoothViewModel = remember { BTcontroller.getInstance(context.applicationContext as Application) }
    val device = bluetoothViewModel.devices.find { it.address == deviceAddress } ?: return
    val characteristic = device.gattServices.flatMap { it.characteristics }
        .find { it.uuid.toString() == characteristicUuid } ?: return
    val response by bluetoothViewModel.receivedResponse.observeAsState("")
    val characteristicRead = device.gattServices.flatMap { it.characteristics }
        .find { it.uuid.toString() == "6e400003-b5a3-f393-e0a9-e50e24dcca9e" } ?: return
    var textRead by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Characteristic: $characteristicUuid", style = MaterialTheme.typography.bodyLarge)

        Text("Select Parsing Method:", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 16.dp))
        val parsingMethods = listOf("ByteArray", "Unsigned Int", "Bool", "UTF8")
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            items(parsingMethods) { method ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        selectedParseMethod = method
                        message = "" // Reset message when changing parsing method
                    }) {
                    Text(text = method, modifier = Modifier.padding(16.dp))
                }
            }

        }

        when (selectedParseMethod) {
            "ByteArray", "Bool" -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(onClick = { message = "0" }, modifier = Modifier.padding(8.dp)) {
                        Text("0")
                    }
                    Button(onClick = { message = "1" }, modifier = Modifier.padding(8.dp)) {
                        Text("1")
                    }
                }
            }
            else -> {
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Enter message") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = {
                val command = when (selectedParseMethod) {
                    "ByteArray" -> message.toByteArray(Charsets.UTF_8)
                    "Unsigned Int" -> message.toUIntOrNull()?.let { ByteBuffer.allocate(4).putInt(it.toInt()).array() } ?: ByteArray(0)
                    "Bool" -> if (message == "1") byteArrayOf(1) else byteArrayOf(0)
                    "UTF8" -> message.toByteArray(Charsets.UTF_8)
                    else -> message.toByteArray(Charsets.UTF_8)
                }
                characteristic.value = command
                val success = bluetoothViewModel.connectedGatt?.writeCharacteristic(characteristic) ?: false
                if (success) {
                    Toast.makeText(context, "Message sent successfully", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = message.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Send")
        }

        Spacer(modifier = Modifier.height(5.dp))

        Button(

            onClick = {

                Toast.makeText(context, "Refresh ", Toast.LENGTH_SHORT).show()
                scope.launch(Dispatchers.IO) {
                    //non devo leggere su questa caratteristica ma su quella 6e400003-b5a3-f393-e0a9-e50e24dcca9e
                    textRead=bluetoothViewModel.readCharacteristic(characteristicRead)
                    //bluetoothViewModel.readCharacteristic(characteristic)
                }
            },
            enabled = message.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.padding(top = 5.dp)
        ) {
            Text("refresh")
        }


        Button(onClick = { navController.popBackStack() }, modifier = Modifier.padding(top = 16.dp)) {
            Text("Back to Characteristic List")
        }


    }
}
