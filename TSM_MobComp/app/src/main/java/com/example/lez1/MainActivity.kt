package com.example.lez1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lez1.ui.theme.Lez1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lez1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "sdroid",
                        modifier = Modifier.padding(innerPadding) //faccio questo solamente per avereproprieta in comune a tutti!
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // Load the image resource
    val image: Painter = painterResource(id = R.drawable.screenshot) // Usa il nome corretto della tua immagine

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Padding globale per evitare che gli elementi tocchino i bordi dello schermo
            .border(1.dp, color = Color.Yellow)
    ) {
        Image(
            painter = image,
            contentDescription = null, // Puoi aggiungere una descrizione se necessario
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp) // Distanza tra l'immagine e il testo
        )
        Text(
            text = "Hello $name!",
            modifier = Modifier
                .background(Color.Blue)
                .padding(16.dp)
                .border(width = 2.dp, color = Color.Red)
                .fillMaxWidth() // Assicura che il testo non superi i bordi orizzontali
                .padding(bottom = 16.dp) // Distanza tra il testo e i bottoni
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Spaziatura uniforme tra i bottoni
        ) {
            Button(
                onClick = { /* ... */ },
                modifier = Modifier.padding(2.dp)
            ) {
                Text("OK")
            }
            Button(
                onClick = { /* ... */ },
                modifier = Modifier.padding(2.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_front_hand_24),
                    contentDescription = "Login",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text("Login")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Spaziatura uniforme tra i bottoni
        ) {
            Text(
                text = "Hello $name!",
                modifier = Modifier
                    .background(Color.Blue)
                    .padding(16.dp)
                    .border(width = 2.dp, color = Color.Red)
                    .padding(bottom = 16.dp) // Distanza tra il testo e i bottoni
            )
            Text(
                text = "Hello $name!",
                modifier = Modifier
                    .background(Color.Blue)
                    .padding(16.dp)
                    .border(width = 2.dp, color = Color.Red)
                    .padding(bottom = 16.dp) // Distanza tra il testo e i bottoni
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Spaziatura uniforme tra i bottoni
        ) {
            Text( /*TESTO CENTRATO*/
                text = "Hello $name!",
                fontSize = 24.sp,//ga senso usare sp perche se uno ha modificato impost sistema vuole le robe piu grandi e devo tenerne conto!
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Color.Blue)
                    .padding(16.dp)
                    .border(width = 2.dp, color = Color.Red)
                    .padding(bottom = 16.dp) // Distanza tra il testo e i bottoni
            )
        }

        Row(modifier = Modifier
            .border(1.dp, color = Color.Green)

        ){
            GameButton(1,modifier)
            GameButton(2,modifier)
            GameButton(3,modifier)
            GameButton(4,modifier)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, color = Color.Green),
        //metto spacer tra i bottoni
         horizontalArrangement = Arrangement.spacedBy(2.dp)

        ) {
            GameButton(1,modifier = Modifier.weight(5f))
            GameButton(2,modifier = Modifier.weight(1f))
            GameButton(3,modifier = Modifier.weight(1f))
            GameButton(4,modifier = Modifier.weight(2f))
        }

        Column(modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.dp)) {
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
            Row(modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                GameButtonProf(1, modifier = Modifier.weight(1f))
                GameButtonProf(2, modifier = Modifier.weight(1f))
                GameButtonProf(3, modifier = Modifier.weight(1f))
            }
        }


    }


}

//creo un componente personalizzato similmente a come ho fatto prima per Greeting

@Composable
fun GameButton(num: Int, modifier: Modifier){
    Button(onClick = {},modifier = modifier) {
        Text("$num")
    }
}


@Composable
fun GameButtonProf(num : Int, modifier: Modifier) {
    Button(onClick = {}, modifier = modifier.fillMaxHeight(), shape = RoundedCornerShape(12.dp)) {
        Text("$num", fontSize = 24.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lez1Theme {
        Greeting("Android")
    }
}
