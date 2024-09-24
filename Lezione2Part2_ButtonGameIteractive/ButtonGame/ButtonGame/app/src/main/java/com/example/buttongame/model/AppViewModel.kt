package com.example.buttongame.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    val result = mutableStateOf("")
    var colorButtonNum : Int = 0

    var startTime: Long = 0

    var backgroundColor = mutableStateListOf(
        Color.Red, //quisto e index o e non dovrebbe esserci quindi lo metto per far partire index da 1!
        Color.Blue, Color.Blue, Color.Blue,
        Color.Blue, Color.Blue, Color.Blue,
        Color.Blue, Color.Blue, Color.Blue
    )

    fun changeName() {
        result.value = "new name"
    }

    fun startGame() {
        colorButtonNum = (1..9).random()
        backgroundColor[colorButtonNum] = Color.Green
        startTime = System.currentTimeMillis()
        result.value = "Find the green button!"

    }

    fun gameButtonClicked(buttonNum: Int /*numero bottone che user ha cliccato*/) {
        if (buttonNum == colorButtonNum) {
            result.value = "You win!"
            //stampo il tempo impiegato per cliccare il bottone giusto
            val seconds = (System.currentTimeMillis() - startTime)
            result.value = "You win! Time: $seconds ms"
            //faccio tornare il colore del bottone a blu
            backgroundColor[colorButtonNum] = Color.Blue

        } else {
            //result.value = "You lose!"
        }
    }

}