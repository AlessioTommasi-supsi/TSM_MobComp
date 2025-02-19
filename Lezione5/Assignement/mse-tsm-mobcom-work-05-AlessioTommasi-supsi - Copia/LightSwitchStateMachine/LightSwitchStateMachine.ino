/*
STATE MACHINE: 
Button-triggered LED State Machine
        
            b:  user button
            l:  LED
        
            +----+                                         +----+
            | S0 +---pressed(b)|l=on---------------------->+ S1 |
            +--+-+                                         +-+--+
             ^                                             |
             |                                         !pressed(b)
             |                                             |
             |                                             v
            +----+                                         +----+
            | S1 |                                         | S0 |
            +----+                                         +----+



*/

#include "Adafruit_TinyUSB.h" // Fix https://github.com/adafruit/Adafruit_nRF52_Arduino/issues/653

int buttonPin = 10; // Grove port D4, button
int ledPin = 5; // Grove port D2, LED

int s = 0; // state

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
        s = 1;
      }
      break;
      
    case 1: // LED acceso, attende il rilascio del pulsante
      if (!pressed(b)) {
        s = 0; // Ritorna allo stato iniziale
      }
      break;
  }
  delay(1);
}