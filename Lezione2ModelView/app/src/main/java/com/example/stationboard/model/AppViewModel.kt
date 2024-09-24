package com.example.stationboard.model

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

//A ViewModel must inherit from ViewModel
class AppViewModel(application: Application) : AndroidViewModel(application) {
    //val context = getApplication<Application>().applicationContext//check this why do not work! in teoria voglio prendere contesto dell'applicazione
    //non ho capito perche qui non funziona ma nella funzione openServer funziona penso perche non posso usare getApplication in un campo ma necessita di essere usato in una funzione

    var name = mutableStateOf("initial state")
    //it is possible to create methods and
//change attributes within
    fun changeName() {
//use .value to access the value of the state
//variable
        name.value = "New Name from Model State"
    }

    init {
        openServer()
    }


    fun openServer(){

        //potrei chiamarlo da Mainactivity Onclick button per esempio oppure come faccio qui chiamarlo una sola volta all avvio dell'applicazione tramite il metodo init!
        val context = getApplication<Application>().applicationContext

        //create a request queue
        val requestQueue = Volley.newRequestQueue(context)
        //define a request.
        val request = StringRequest(
            Request.Method.GET, "https://api.open-meteo.com/v1/forecast?latitude=47.37&\n" +
                    "longitude=8.55&daily=temperature_2m_max,\n" +
                    "temperature_2m_min&current_weather=true&timezone=Europe%2FZurich",
                { response ->
                    //find the response string in "response"
                    Log.i("Volley"
                        , response)
                },
                Response.ErrorListener {
                    Log.e("Volley"
                        ,
                        "Error loading data: $it")
                })
        //execute the request
        requestQueue.add(request)
    }

}
