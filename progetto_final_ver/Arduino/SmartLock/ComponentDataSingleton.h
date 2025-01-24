#ifndef COMPONENTDATASINGLETON_H
#define COMPONENTDATASINGLETON_H

#include <bluefruit.h>

class ComponentDataSingleton {
public:
    static ComponentDataSingleton& getInstance() {
        static ComponentDataSingleton instance;
        return instance;
    }

    int sensorDoorPin = 13;
    int actuatorDoorPin = 2;
    bool isOpen = false;
    
    uint16_t mtu;
    BLEService uartService = { 0x9E, 0xCA, 0xDC, 0x24, 0x0E, 0xE5, 0xA9, 0xE0, 0x93, 0xF3, 0xA3, 0xB5, 0x01, 0x00, 0x40, 0x6E };
    BLECharacteristic rxCharacteristic{ 0x9E, 0xCA, 0xDC, 0x24, 0x0E, 0xE5, 0xA9, 0xE0, 0x93, 0xF3, 0xA3, 0xB5, 0x02, 0x00, 0x40, 0x6E };
    BLECharacteristic txCharacteristic{ 0x9E, 0xCA, 0xDC, 0x24, 0x0E, 0xE5, 0xA9, 0xE0, 0x93, 0xF3, 0xA3, 0xB5, 0x03, 0x00, 0x40, 0x6E };

private:
    ComponentDataSingleton() {}
    ComponentDataSingleton(const ComponentDataSingleton&) = delete;
    void operator=(const ComponentDataSingleton&) = delete;
};

#endif // COMPONENTDATASINGLETON_H
