package com.example.smartlock

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smartlock.controller.DeviceLogController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LogsDeviceView(navController: NavHostController) {
    val context = LocalContext.current
    val DeviceLogsmodel: DeviceLogController = viewModel()
    var isLoading by remember { mutableStateOf(true) }
    var showErrorToast by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        DeviceLogsmodel.fetchDevices {
            isLoading = false
            if (DeviceLogsmodel.errorMessage.value.isNotEmpty()) {
                showErrorToast = true
                Toast.makeText(context, DeviceLogsmodel.errorMessage.value, Toast.LENGTH_LONG).show()
                navController.navigate("home") // Ritorna alla schermata Home
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (DeviceLogsmodel.errorMessage.value.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Device Logs List",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(DeviceLogsmodel.devices) { index, entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                Toast.makeText(context, "Selected Logs device: ${entry.device_name}", Toast.LENGTH_LONG).show()
                                navController.navigate("logsDetail/${entry.device_name}")
                            }
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = entry.device_name,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(2f)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Logs: ${entry.number_of_logs}",
                                    fontSize = 20.sp,
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
