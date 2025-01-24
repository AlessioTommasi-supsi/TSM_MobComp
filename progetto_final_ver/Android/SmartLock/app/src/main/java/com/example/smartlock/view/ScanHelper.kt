import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartlock.controller.NavControllerSingleton
import com.example.smartlock.model.BluetoothDeviceData
import com.example.smartlock.controller.BTcontroller

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectedDeviceBehaviour(bluetoothViewModel: BTcontroller, deviceAddress: String, device: BluetoothDeviceData) {
    var nav = NavControllerSingleton.getInstance()

    Button(
        onClick = {
            val bluetoothDevice = bluetoothViewModel.getBluetoothDevice(deviceAddress)
            bluetoothDevice?.let {
                bluetoothViewModel.disconnectDevice(deviceAddress)
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    ) {
        Text("Disconnetti", color = Color.White)
    }
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        onClick = {
            nav.navigate("BTserviceDetail/${device.address}")
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    ) {
        Text("Advance BT", color = Color.White)
    }
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        onClick = {
            val characteristicRead = device.gattServices.flatMap { it.characteristics }
                .find { it.uuid.toString() == "6e400003-b5a3-f393-e0a9-e50e24dcca9e" }

            val readValue = characteristicRead?.let { bluetoothViewModel.readCharacteristic(it) } ?: "Unknown"


            nav.navigate("BTOpenCloseService/${device.address}")
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    ) {
        Text("Default", color = Color.White)
    }

}

@Composable
fun DisconnectedDeviceBehaviour(bluetoothViewModel: BTcontroller, deviceAddress: String) {
    val context = LocalContext.current

    Button(
        onClick = {
            val bluetoothDevice = bluetoothViewModel.getBluetoothDevice(deviceAddress)
            bluetoothDevice?.let {
                bluetoothViewModel.connectDevice(context,it)
                Toast.makeText(context, "Connessione avvenuta con successo", Toast.LENGTH_SHORT).show()
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    ) {
        Text("Connetti", color = Color.White)
    }
}
