package com.example.smartlock.view.BT

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartlock.controller.NavControllerSingleton
import com.example.smartlock.controller.BTcontroller


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun stopScanAndWait(bt: BTcontroller) {
    withContext(Dispatchers.IO) {
        //bt.stopScan()
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CorutineBackBTScan(bt: BTcontroller, deviceAddress: String, navController: NavHostController) {
    GlobalScope.launch {
        stopScanAndWait(bt)
        withContext(Dispatchers.Main) {
            Log.d("isConnected", "pre navigate backBTScan")
            navController.navigate("scan")
        }
    }
}




@Composable
fun BackBTScan(bt: BTcontroller, deviceAddress: String) {
    //DisconnectedDeviceActions(bluetoothViewModel = BT, deviceAddress = deviceAddress)
    //bT.disconnectDevice(deviceAddress)
    //bt.stopScan()
    //Log.d("isConnected", "pre navigate backBTScan")
    //NavControllerSingleton.getInstance().navigate("scan2" )
    CorutineBackBTScan(bt, deviceAddress, NavControllerSingleton.getInstance() )
}

@Composable
fun ServiceListScreen(navController: NavHostController, deviceAddress: String) {
    var isConnected by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val bluetoothViewModel = remember { BTcontroller.getInstance(context.applicationContext as Application) }
    val device = bluetoothViewModel.devices.find { it.address == deviceAddress } ?: return

    Spacer(modifier = Modifier.height(8.dp))
    /*
    Button(
        onClick = {

            isConnected = false //torno a schermata di scansione
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    ) {
        Text("<- Back", color = Color.White)
    }
    if (!isConnected) {

        Log.d("isConnected", "BackBTScan")
        BackBTScan(bt = bluetoothViewModel, deviceAddress = deviceAddress)
    }
    Spacer(modifier = Modifier.height(20.dp))
    Spacer(modifier = Modifier.height(20.dp))
    */

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Select Service:", style = MaterialTheme.typography.bodyLarge)
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            items(device.gattServices) { service ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate("characteristicList/${deviceAddress}/${service.uuid}")
                    }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = service.uuid.toString())
                        Spacer(modifier = Modifier.height(8.dp))

                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))



}
