#include "Adafruit_TinyUSB.h" // Fix https://github.com/adafruit/Adafruit_nRF52_Arduino/issues/653
#include "Adafruit_SHT31.h"

const int buttonPin = 7; // pulsante utente a bordo
const int redLedPin = LED_RED; // LED rosso a bordo
const int blueLedPin = LED_BLUE; // LED blu a bordo

float threshold;
int t0;

int state = 0;

Adafruit_SHT31 sht31 = Adafruit_SHT31();

int pressed(int value) {
    return value == LOW; // invertito a causa del pull-up
}

void setup() {
  Serial.begin(115200);
  Serial.println("Humidity Alert");
  pinMode(redLedPin, OUTPUT);
  pinMode(blueLedPin, OUTPUT);
  digitalWrite(redLedPin, LOW);
  digitalWrite(blueLedPin, LOW);
  pinMode(buttonPin, INPUT_PULLUP); // pull-up
  sht31.begin(0x44);
  sht31.heater(true);
}

void loop() {
  int b = digitalRead(buttonPin);
  float h = sht31.readHumidity();
  
  Serial.print("State: ");
  Serial.println(state);
  Serial.print("Humidity: ");
  Serial.println(h);

  
  switch (state) {
    case 0: // Stato iniziale
      if (pressed(b)) {
        threshold = h + 10.0; // Imposta la soglia di umidità
        digitalWrite(redLedPin, HIGH); // Accende il LED rosso
        state = 1;
      }
      break;
      
    case 1: // Attende il rilascio del pulsante qiondi sto monitorando
      if (!pressed(b)) {
        state = 2;
      }
      break;
      
    case 2: // Monitoraggio dell'umidità
      if (!isnan(h) && h > threshold) {
        digitalWrite(blueLedPin, HIGH); // Accende il LED blu
        state = 3;
      }
      break;
      
    case 3: // Allarme attivo, attende la conferma del pulsante
      if (pressed(b)) {
        digitalWrite(redLedPin, HIGH); // Accende il LED rosso
        digitalWrite(blueLedPin, LOW); // Spegne il LED blu
        t0 = millis();
        state = 4;
      }
      break;
      
    case 4: // Attende il rilascio del pulsante
      if (!pressed(b)) {
        state = 5;
      }
      break;
      
    case 5: // LED rosso acceso per 1 secondo
      if ((millis() - t0) > 1000) {
        digitalWrite(redLedPin, LOW); // Spegne il LED rosso
        state = 0; // Ritorna allo stato iniziale
      }
      break;
  }
  
  delay(1000);
}