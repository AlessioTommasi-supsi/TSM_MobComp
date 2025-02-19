
#include "Adafruit_TinyUSB.h"

int pin = A0; 

void setup() {
  Serial.begin(115200);
}

// the loop routine runs over and over again forever:
void loop() {
  // read the input on analog pin 0:
  int sensorValue = analogRead(pin);
  // print out the value you read:
  Serial.println(sensorValue);
  delay(1000);  // delay in between reads for stability
}
