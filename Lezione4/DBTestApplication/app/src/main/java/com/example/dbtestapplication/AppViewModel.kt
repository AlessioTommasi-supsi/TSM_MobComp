package com.example.dbtestapplication

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) :
    AndroidViewModel(application) {

    var userName = mutableStateOf<String>("undefined")

    lateinit var db : AppDatabase

    var users = mutableStateListOf<User>()

    fun createDatabase() {
        val context =
            getApplication<Application>().applicationContext

        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    init {
        createDatabase()
       //insertUsers() //inserisci i primi 2 utenti poi commenta se no a ogni restart inserisce 2 user in db uguali!
    }

    fun insertUsers() {
        viewModelScope.launch(Dispatchers.IO) { // faccio corutine poiche non posso fare operazioni di db nel main thread read o write da un db perche e marcata come suspend
            suspInsertUsers()
        }
    }



    suspend fun suspInsertUsers() {
        db.userDao().insertAll(
            User(firstName="Peter", lastName="Muster"),
            User(firstName="Paul", lastName="Muster"))
    }

    fun readUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            suspReadUsers()
        }
    }

    suspend fun suspReadUsers() {
        val userList : List<User> =
            db.userDao().getAll().toMutableList()
        //note that you should ensure that
        //a state variable from your model is
        //only updated in a Main context
      //  withContext(Dispatchers.Main){
        users.addAll(userList)
      //  }
    }








    fun storeSettings() {
        val context =
            getApplication<Application>().applicationContext

        val settings = context.getSharedPreferences("prefsfile",
            Context.MODE_PRIVATE)

//it is important that you get an editor reference!
        val editor = settings.edit()

//save strings, booleans, floats, ints, longs, stringsets
        editor.putString("USER_NAME", "john.ford@hollywood.com")

//and commit your changes
        editor.commit()
    }

    fun readSettings() {
        val context =
            getApplication<Application>().applicationContext

        val settings = context.getSharedPreferences("prefsfile",
            Context.MODE_PRIVATE)

        userName.value = settings.getString("USER_NAME",
            "no-user@no-domain.com") ?: "--"
    }
}