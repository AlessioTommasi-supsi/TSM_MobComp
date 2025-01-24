package com.example.smartlock

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.smartlock.controller.LogsController

@Composable
fun LogsDetailView(navController: NavHostController, deviceName: String) {
    val context = LocalContext.current
    val logsController: LogsController = viewModel()

    var showErrorToast by remember { mutableStateOf(false) }

    LaunchedEffect(deviceName) {
        logsController.fetchLogs(deviceName)
    }

    if (logsController.isLoading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        if (logsController.errorMessage.value.isNotEmpty()) {
            showErrorToast = true
            Toast.makeText(context, logsController.errorMessage.value, Toast.LENGTH_LONG).show()
            //navController.popBackStack() // Ritorna alla schermata precedente non buono perche non aggiorna se ci sono cambiamenti sul db
            navController.navigate("logs")
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(" < ")
                    }
                    Text(
                        text = "Logs for $deviceName",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(logsController.logs) { _, entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            var text_to_show = ""
                            if (entry.state == 0) {
                                text_to_show = "Open"
                            } else if (entry.state == 1) {
                                text_to_show = "Close"
                            } else {
                                text_to_show = "Unknown"
                            }
                            Text(
                                text = "State: $text_to_show",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Date: ${entry.date}",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
