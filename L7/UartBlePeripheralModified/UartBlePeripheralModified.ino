// Based on https://github.com/adafruit/Adafruit_nRF52_Arduino
// /tree/master/libraries/Bluefruit52Lib/examples/Peripheral
// Copyright (c) Adafruit.com, all rights reserved.
 
// Based on https://github.com/adafruit/Adafruit_SHT31
// /blob/master/Adafruit_SHT31.cpp
// Copyright (c) Adafruit.com, all rights reserved.
 
// Licensed under the MIT license, see LICENSE or
// https://choosealicense.com/licenses/mit/
 
#include "Adafruit_TinyUSB.h" // Fix https://github.com/adafruit/Adafruit_nRF52_Arduino/issues/653
#include <bluefruit.h>
 
// 2e1f0001-7937-46d8-b54f-b44d4fbd13c3 Env Service
// 2e1f0002-7937-46d8-b54f-b44d4fbd13c3     Humi Chr [N], 0 - 100 %, 1 byte, uint8_t[1]
// 2e1f0003-7937-46d8-b54f-b44d4fbd13c3     Temp Chr [N], 0 - 60.00 C, (uint32_t) t * 100.0, uint8_t[4]
// 2e1f0004-7937-46d8-b54f-b44d4fbd13c3     Heat [R, W], 0x00 (off) or 0x01 (on), uint8_t[1]
 
//2e1f0001-7937-46d8-b54f-
//0xc3 0x13 0xbd 0x4f 0x4d 0xb4 ...
 
// The arrays below are ordered "least significant byte first":
uint8_t const envServiceUuid[] = { 0xc3 0x13 0xbd 0x4f 0x4d 0xb4 ... };
uint8_t const humiCharacteristicUuid[] = { 0xc3 0x13 0xbd 0x4f 0x4d 0xb4 ... };
uint8_t const tempCharacteristicUuid[] = { 0xc3 0x13 0xbd 0x4f 0x4d 0xb4 ... };
uint8_t const heatCharacteristicUuid[] = { 0xc3 0x13 0xbd 0x4f 0x4d 0xb4 ... };
 
Adafruit_SHT31 sht31 = Adafruit_SHT31();
 
BLEService uartService = BLEService(envServiceUuid);
BLECharacteristic humiCharacteristic = BLECharacteristic(humiCharacteristicUuid);
BLECharacteristic tempCharacteristic = BLECharacteristic(tempCharacteristicUuid);
BLECharacteristic heatCharacteristic = BLECharacteristic(heatCharacteristicUuid);
 
float heat = 0.0;
 
void writeCallback(uint16_t connHandle, BLECharacteristic* characteristic, uint8_t* data, uint16_t len) {
  if (characteristic->uuid == heatCharacteristic.uuid) {
    heater(data[0] == 0x01);
  }
}
 
// void readCallback() {
// ... = heat;
// }
 
void setupEnvService() {
  envService.begin(); // Must be called before calling .begin() on its characteristics
 
  humiCharacteristic.setProperties(CHR_PROPS_NOTIFY);
  humiCharacteristic.setPermission(SECMODE_OPEN, SECMODE_NO_ACCESS);
  humiCharacteristic.setLen(1);
  humiCharacteristic.begin();
 
  tempCharacteristic.setProperties(CHR_PROPS_NOTIFY);
  tempCharacteristic.setPermission(SECMODE_OPEN, SECMODE_NO_ACCESS);
  tempCharacteristic.setLen(4);
  tempCharacteristic.begin();
 
  heatCharacteristic.setProperties(CHR_PROPS_READ|CHR_PROPS_WRITE);
  heatCharacteristic.setPermission(SECMODE_OPEN, SECMODE_NO_ACCESS);
  heatCharacteristic.setLen(1);
  heatCharacteristic.begin();
}
 
void startAdvertising() {
  Bluefruit.Advertising.addFlags(BLE_GAP_ADV_FLAGS_LE_ONLY_GENERAL_DISC_MODE);
  Bluefruit.Advertising.addTxPower();
  Bluefruit.Advertising.addService(envService);
  Bluefruit.Advertising.addName();
 
  // See https://developer.apple.com/library/content/qa/qa1931/_index.html   
  const int fastModeInterval = 32; // * 0.625 ms = 20 ms
  const int slowModeInterval = 244; // * 0.625 ms = 152.5 ms
  const int fastModeTimeout = 30; // s
  Bluefruit.Advertising.restartOnDisconnect(true);
  Bluefruit.Advertising.setInterval(fastModeInterval, slowModeInterval);
  Bluefruit.Advertising.setFastTimeout(fastModeTimeout);
  // 0 = continue advertising after fast mode, until connected
  Bluefruit.Advertising.start(0);
  Serial.println("Advertising ...");
}
 
void setup() {
  Serial.begin(115200);
  while (!Serial) { delay(10); } // only if usb connected
  Serial.println("Setup");
 
  if (!sht31.begin(0x44)) {   // Set to 0x45 for alternate i2c addr
    Serial.println("Couldn't find SHT31");
    while (1) delay(1);
  }
 
  Bluefruit.begin();
  Bluefruit.setName("nRF52840");
 
  setupEnvService();
  startAdvertising();
}
 
void loop() {
  if (Bluefruit.connected()) {
    heat = sht31.isHeaterEnabled();
    float humi;
    float temp;
    sht31.readBoth(&humi, &temp);
    if (humi != NaN) {
      uint8_t h[] = { (uint8_t) humi };
      humiCharacteristic.notify(h, 1));
    }
    if (temp != NaN) {
      uint8_t h[] = { // msb
        (uint8_t) temp; // TODO: shift
        (uint8_t) temp; // TODO: shift
        (uint8_t) temp; // TODO: shift
        (uint8_t) temp;
      }
      tempCharacteristic.notify(h, 4));
    }
  }
  delay(200); // ms
}