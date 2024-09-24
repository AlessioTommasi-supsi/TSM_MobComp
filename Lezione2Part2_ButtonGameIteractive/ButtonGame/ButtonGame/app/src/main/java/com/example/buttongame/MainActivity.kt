package com.example.buttongame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buttongame.ui.theme.ButtonGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ButtonGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ButtonGame(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonGame( modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Button Game",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() // fillMaxWidth() is used to make the text occupy the full width of the screen
        )
        
        Text(
            text = "Result:",
            fontSize = 24.sp
        )
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("Start")
        }
        Column(modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Row(modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                GameButtonProf(1, modifier = Modifier.weight(1f)/*modifico il peso per far si che occupino tutto lo spazio sullo schermo */)
                GameButtonProf(2, modifier = Modifier.weight(1f)/*non possiamo muovere weight in GameButtonProf ma defe essere messo qui! */)
                GameButtonProf(3, modifier = Modifier.weight(1f))
            }
            Row(modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                GameButtonProf(1, modifier = Modifier.weight(1f))
                GameButtonProf(2, modifier = Modifier.weight(1f))
                GameButtonProf(3, modifier = Modifier.weight(1f))
            }
            Row(modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                GameButtonProf(1, modifier = Modifier.weight(1f))
                GameButtonProf(2, modifier = Modifier.weight(1f))
                GameButtonProf(3, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun GameButtonProf(num : Int, modifier: Modifier) {
    Button(onClick = {}, modifier = modifier.fillMaxHeight(), shape = RoundedCornerShape(12.dp)) {
        Text("$num", fontSize = 24.sp)
    }
}