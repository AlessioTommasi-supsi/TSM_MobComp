/*
* STATE MACHINE
Button-triggered LED State Machine
    
            b:  user button
            l:  LED
            t0: time
    
            +----+                                         +----+
            | S0 +---pressed(b)|l=on---------------------->+ S1 |
            +--+-+                                         +-+--+
               ^                                             |
               |                                         !pressed(b)
             millis()-t0>1000|l=off                          |
               |                                             v
            +----+                                         +----+
            | S1 |                                         | S0 |
            +----+                                         +----+
*/



#include "Adafruit_TinyUSB.h" // Fix https://github.com/adafruit/Adafruit_nRF52_Arduino/issues/653

int buttonPin = 7; // onboard button
int ledPin = 4; // onboard, blue LED

int s = 0; // state
long t0;

int pressed(int value) {
  //return value == HIGH; // standard, active high
  return value == LOW; // inverted, active low
}

void setup() {
  pinMode(buttonPin, INPUT_PULLUP);
  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, LOW);
  Serial.begin(115200);
}

void loop() {
  int b = digitalRead(buttonPin);
  Serial.println(b);
  
  switch (s) {
    case 0: // Stato iniziale
      if (pressed(b)) {
        digitalWrite(ledPin, HIGH); // Accende il LED
        t0 = millis(); // Inizia a misurare il tempo
        s = 1;
      }
      break;
      
    case 1: // LED acceso, attende il rilascio del pulsante e misura il tempo
      if (!pressed(b)) {
        if ((millis() - t0) >= 1000) { // Se la pressione è stata lunga (> 1s)
          digitalWrite(ledPin, LOW); // Spegne il LED
        }
        s = 0; // Ritorna allo stato iniziale
      }
      break;
  }
  
  delay(1);
}