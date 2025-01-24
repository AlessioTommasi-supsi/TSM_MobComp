package com.example.smartlock.view

import ConnectedDeviceBehaviour
import DisconnectedDeviceBehaviour
import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smartlock.controller.BTcontroller

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {


    var showScanButton by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val bluetoothViewModel: BTcontroller = remember {
        BTcontroller.getInstance(context.applicationContext as Application)
    }






    showScanButton = !bluetoothViewModel.isScanning.value

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (showScanButton) {
            Button(
                onClick = {
                    bluetoothViewModel.startScan()
                    showScanButton = false
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Scan")
            }
        } else {
            Button(
                onClick = {
                    bluetoothViewModel.stopScan()
                    showScanButton = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
            ) {
                Text("Stop Scan")
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(bluetoothViewModel.devices) { index, device ->
                    val isConnected = bluetoothViewModel.isDeviceConnected(device.address)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Name: ${device.name}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Address: ${device.address}",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Signal Strength: ${device.signalStrength} dBm",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Device Class: ${device.deviceClass}",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Major Device Class: ${device.majorDeviceClass}",
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            if (isConnected) {
                                ConnectedDeviceBehaviour(bluetoothViewModel, device.address, device)
                            } else {
                                DisconnectedDeviceBehaviour(bluetoothViewModel, device.address)
                            }
                        }
                    }
                }
            }
        }
    }
}
