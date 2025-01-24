#include <Arduino.h>
#include "Adafruit_TinyUSB.h"
#include "BTController.h"

BTController& btController = BTController::getInstance();

void setup() {
    Serial.begin(115200);
    while (!Serial) { delay(10); }
    Serial.println("Setup");

    btController.setup();
}

void loop() {
    btController.check();
    delay(50);
}