package com.example.dbtestapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dbtestapplication.ui.theme.DBTestApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DBTestApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, model : AppViewModel = viewModel()) {
    Column(modifier = modifier) {
        Text(
            text = model.userName.value,
            modifier = modifier
        )
        Button(onClick = { model.readSettings() }) {
            Text("Read Settings")
        }
        Button(onClick = {model.storeSettings()}) {
            Text("Store Settings")
        }

        Button(onClick = {model.readUsers()}) { //quando clicco su questo bottone carico gli utenti dal db ad una lista di utenti NOTA ne aggiunge quindi se clicco 2 volte nella lista ci saranno duplicati
            Text("Load Users from Database")
        }

        LazyColumn { //lazy column e un composable che permette di fare scroll di una lista di elementi, automaticamente aggiorna gli elementi se vengono aggiunti alla lista che controlla in questo caso model.users
            itemsIndexed(model.users) {index, user ->
                Text("${user.firstName} ${user.lastName}")
            }
        }
    }
}
