#ifndef BTCONTROLLER_H
#define BTCONTROLLER_H

#include <bluefruit.h>

class BTController {
public:
    static BTController& getInstance();
    void setup();
    void check();

private:
    BTController();
    void setupUartService();
    void startAdvertising();

    static void connectedCallback(uint16_t connHandle);
    static void disconnectedCallback(uint16_t connHandle, uint8_t reason);
    static void cccdCallback(uint16_t connHandle, BLECharacteristic* characteristic, uint16_t cccdValue);
    static void writeCallback(uint16_t connHandle, BLECharacteristic* characteristic, uint8_t* rxData, uint16_t len);

    uint16_t mtu;
    BLEService uartService;
    BLECharacteristic rxCharacteristic;
    BLECharacteristic txCharacteristic;

    bool isOpen = false;
};

#endif // BTCONTROLLER_H