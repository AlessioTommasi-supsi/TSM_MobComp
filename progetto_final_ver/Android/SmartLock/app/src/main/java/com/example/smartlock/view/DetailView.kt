package com.example.smartlock.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.smartlock.model.AppViewModel

@Composable
fun DetailView (navController: NavHostController,
                modifier: Modifier = Modifier,
                index : Int,
                model: AppViewModel = viewModel()) /*prende un istanza del mio model*/ {
    val entry = model.entries[index]
    Column(modifier = modifier) {
        Text("Detail Page", fontSize = 24.sp)
        Text(entry.train, fontSize = 24.sp)
        Text(entry.to, fontSize = 24.sp)

    }
}
