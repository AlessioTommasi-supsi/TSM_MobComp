package com.example.stationboard.model

class SBBTransport( //livello piu alto
    val stationboard  /*deve essere lo stesso di json!*/ : MutableList<StationboardEntry>, //lista di treni in arrivo alla stazione
    val station : StationInfo
) {
}

class StationboardEntry( //2 livello
    val to: String, // destinazione potrei aggiungerne altri campi vedi json https://transport.opendata.ch/v1/stationboard?id=8503000
    val category :String ,
    val number : String /*perche ci sono stazioni senza numero*/
) {
}

class StationInfo( //2 livello
    val name: String, //nome della stazione
    val id: String //id della stazione
) {
}

class BakingObject {
}