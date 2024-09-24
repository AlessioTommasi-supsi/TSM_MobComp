package com.example.stationboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stationboard.model.AppViewModel
import com.example.stationboard.ui.theme.StationboardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StationboardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier,model: AppViewModel = viewModel() /*Dependency injection! con  viewModel() mi sto creando un oggetto anonimo di tipo AppViewModel e lo sto assegnando alla variabile model*/) {
    Column {
        Text(
            text = model.name.value,
            modifier = modifier,
            fontSize = 24.sp
        )
        Button(onClick = {model.changeName()/*metodo definito in AppViewModel da noi! */}) {
            Text("Click me")
        }
    }

}
