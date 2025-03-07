package com.example.smartlock.view.BT

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartlock.R
import com.example.smartlock.controller.BTcontroller
import com.example.smartlock.controller.DeviceLogController
import kotlinx.coroutines.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTOpenCloseService(nav: NavHostController, deviceAddress: String) {
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val bluetoothViewModel = remember { BTcontroller.getInstance(context.applicationContext as Application) }

    val data by bluetoothViewModel.receivedResponse.observeAsState("")

    var status by remember { mutableStateOf("Unknown") }
    var buttonTxt by remember { mutableStateOf(false) }

    val device = bluetoothViewModel.devices.find { it.address == deviceAddress } ?: return
    val characteristicRead = device.gattServices.flatMap { it.characteristics }
        .find { it.uuid.toString() == "6e400003-b5a3-f393-e0a9-e50e24dcca9e" } ?: return
    val characteristicWrite = device.gattServices.flatMap { it.characteristics }
        .find { it.uuid.toString() == "6e400002-b5a3-f393-e0a9-e50e24dcca9e" } ?: return

    val logController = remember { DeviceLogController(context.applicationContext as Application) }

    fun updateStatus() {
        status = bluetoothViewModel.readCharacteristic(characteristicRead) ?: "Unknown"
        isLoading = false
    }

    LaunchedEffect(Unit) {
        updateStatus()
        if (status == "door is open") {
            buttonTxt = false
        } else if (status == "door is closed") {
            buttonTxt = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                updateStatus()
                            }

                    }) {
                        Icon(painterResource(id = R.drawable.ic_refresh), contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    isLoading = true
                    var sedingCommand = ""
                    if (!buttonTxt) {
                        sedingCommand = "Open"
                    } else {
                        sedingCommand = "Close"
                    }
                    //characteristicWrite.value = status.toByteArray(Charsets.UTF_8)
                    characteristicWrite.value = sedingCommand.toByteArray(Charsets.UTF_8)
                    CoroutineScope(Dispatchers.Main).launch {
                        val success = bluetoothViewModel.connectedGatt?.writeCharacteristic(characteristicWrite) ?: false

                        if (success) {
                            delay(500)
                            updateStatus()
                            //Toast.makeText(context, "The Door is: $status", Toast.LENGTH_SHORT).show()
                            // Invia log al server
                            if (device.name == null){
                                device.name = "nrf52"
                            }

                            logController.sendLogToServer(device.name, buttonTxt)
                            delay(500)
                            buttonTxt = !buttonTxt
                        } else {
                            Toast.makeText(context, "Failed to send command", Toast.LENGTH_SHORT).show()
                        }

                    }
                },
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
            ) {
                Text(if (buttonTxt) "Close" else "Open")
            }

            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                item {
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            CoroutineScope(Dispatchers.Main).launch {
                                updateStatus()
                            }
                        }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // caso in cui vado in errore:
                            /*
                            if (status.trim() == "door is closed" && buttonTxt) {
                                Text(text = "opening door is in progress click me to refreshh")
                            } else if (status.trim() == "door is open" && !buttonTxt) {
                                Text(text = "closing door is in progress click me to refresh")
                            }else {
                                if (isLoading) {
                                    Text(text = "clik me to refresh")
                                } else {
                                    Text(text = status)
                            }*/
                            if (isLoading) {
                                Text(text = "clik me to refresh")
                            } else {
                                Text(text = status)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

