#include "BTController.h"

uint8_t const uartServiceUuid[] = { 0x9E, 0xCA, 0xDC, 0x24, 0x0E, 0xE5, 0xA9, 0xE0, 0x93, 0xF3, 0xA3, 0xB5, 0x01, 0x00, 0x40, 0x6E };
uint8_t const rxCharacteristicUuid[] = { 0x9E, 0xCA, 0xDC, 0x24, 0x0E, 0xE5, 0xA9, 0xE0, 0x93, 0xF3, 0xA3, 0xB5, 0x02, 0x00, 0x40, 0x6E };
uint8_t const txCharacteristicUuid[] = { 0x9E, 0xCA, 0xDC, 0x24, 0x0E, 0xE5, 0xA9, 0xE0, 0x93, 0xF3, 0xA3, 0xB5, 0x03, 0x00, 0x40, 0x6E };

char *recived_command;
int recived_command_len;

int sensorDoorPin = 13;
int actuatorDoorPin = 2;

BTController& BTController::getInstance() {
    static BTController instance;
    
    return instance;
}

BTController::BTController()
    : uartService(uartServiceUuid), rxCharacteristic(rxCharacteristicUuid), txCharacteristic(txCharacteristicUuid) {}

void BTController::setup() {
    pinMode(sensorDoorPin, INPUT);
    isOpen = digitalRead(sensorDoorPin) == LOW;

    Serial.print("the door is now: ");
    Serial.println(isOpen ? "Open" : "Close");

    Bluefruit.begin();
    Bluefruit.setName("nRF52840");
    Bluefruit.Periph.setConnectCallback(connectedCallback);
    Bluefruit.Periph.setDisconnectCallback(disconnectedCallback);

    mtu = Bluefruit.getMaxMtu(BLE_GAP_ROLE_PERIPH);

    setupUartService();
    startAdvertising();
}

void BTController::check() {
    pinMode(sensorDoorPin, INPUT);
    isOpen = digitalRead(sensorDoorPin) == LOW; //NOTA CON vcc su rele ho vcc su porta e dunque porta chiusa!

    if (Bluefruit.connected()) {
        pinMode(sensorDoorPin, INPUT);

        const char* sendingData = isOpen ? "door is open" : "door is closed";
        size_t sendingDataLen = strlen(sendingData);

        txCharacteristic.write((uint8_t*)sendingData, sendingDataLen);
    }
}

void BTController::setupUartService() {
    uartService.begin();

    txCharacteristic.setProperties(CHR_PROPS_NOTIFY | CHR_PROPS_READ);
    txCharacteristic.setPermission(SECMODE_OPEN, SECMODE_OPEN);
    txCharacteristic.setMaxLen(mtu);
    txCharacteristic.setCccdWriteCallback(cccdCallback);
    txCharacteristic.begin();

    rxCharacteristic.setProperties(CHR_PROPS_WRITE | CHR_PROPS_WRITE_WO_RESP);
    rxCharacteristic.setPermission(SECMODE_NO_ACCESS, SECMODE_OPEN);
    rxCharacteristic.setMaxLen(mtu);
    rxCharacteristic.setWriteCallback(writeCallback, true);
    rxCharacteristic.begin();
}

void BTController::startAdvertising() {
    Bluefruit.Advertising.addFlags(BLE_GAP_ADV_FLAGS_LE_ONLY_GENERAL_DISC_MODE);
    Bluefruit.Advertising.addTxPower();
    Bluefruit.Advertising.addService(uartService);
    Bluefruit.Advertising.addName();

    const int fastModeInterval = 32;
    const int slowModeInterval = 244;
    const int fastModeTimeout = 30;
    Bluefruit.Advertising.restartOnDisconnect(true);
    Bluefruit.Advertising.setInterval(fastModeInterval, slowModeInterval);
    Bluefruit.Advertising.setFastTimeout(fastModeTimeout);
    Bluefruit.Advertising.start(0);
    Serial.println("Advertising ...");
}

void BTController::connectedCallback(uint16_t connHandle) {
    char centralName[32] = { 0 };
    BLEConnection *connection = Bluefruit.Connection(connHandle);
    connection->getPeerName(centralName, sizeof(centralName));
    Serial.print(connHandle);
    Serial.print(", connected to ");
    Serial.print(centralName);
    Serial.println();
}

void BTController::disconnectedCallback(uint16_t connHandle, uint8_t reason) {
    Serial.print(connHandle);
    Serial.print(" disconnected, reason = ");
    Serial.println(reason);
    Serial.println("Advertising ...");
}

void BTController::cccdCallback(uint16_t connHandle, BLECharacteristic* characteristic, uint16_t cccdValue) {
    BTController& instance = BTController::getInstance();
    if (characteristic->uuid == instance.txCharacteristic.uuid) {
        Serial.print("UART 'Notify', ");
        if (characteristic->notifyEnabled()) {
            Serial.println("enabled");
        } else {
            Serial.println("disabled");
        }
    }
}

void BTController::writeCallback(uint16_t connHandle, BLECharacteristic* characteristic, uint8_t* rxData, uint16_t len) {
    BTController& instance = BTController::getInstance();
    if (characteristic->uuid == instance.rxCharacteristic.uuid) {
        Serial.print("rx: ");
        for (int i = 0; i < len; i++) {
            Serial.print((char) rxData[i]);
        }

        // Aggiungi un carattere null alla fine della stringa
        char tempBuffer[len + 1];
        memcpy(tempBuffer, rxData, len);
        tempBuffer[len] = '\0';
        recived_command = tempBuffer;
        recived_command_len = len;

        Serial.print("\n");

        if (strcmp(tempBuffer, "Open") == 0) {
            Serial.println("Open door in progress");
            pinMode(actuatorDoorPin, OUTPUT);
            digitalWrite(actuatorDoorPin, HIGH);
        } else if (strcmp(tempBuffer, "Close") == 0) {
            pinMode(actuatorDoorPin, OUTPUT);
            digitalWrite(actuatorDoorPin, LOW);
            Serial.println("strcmp Close true");
        } else {
            Serial.println("command not found!");
        }

        // Controllo del valore sul pin D13 e assegno isOpen se la porta è chiusa
        pinMode(sensorDoorPin, INPUT);
        instance.isOpen = digitalRead(sensorDoorPin) == LOW; //NOTA CON vcc su rele ho vcc su porta e dunque porta chiusa!

        Serial.println(instance.isOpen ? "lock open" : "lock closed");
    }
}
