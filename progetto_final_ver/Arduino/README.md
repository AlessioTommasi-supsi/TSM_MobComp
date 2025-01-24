# PROJECT_NAME Arduino Program

## Prerequisites
* [Feather nRF52840 Sense](https://github.com/tamberg/mse-tsm-mobcom/wiki/Feather-nRF52840-Sense) device
* ONBOARD_SENSOR_X via I2C
* ONBOARD_ACTUATOR_Y on pin PIN_Y 
* EXTERNAL_SENSOR_Z on pin PIN_Z

## Adding libraries
* Arduino IDE > Sketch > Include Library > Manage Libraries ...
* Search for "LIBRARY1_NAME" > Install
* Search for "LIBRARY2_NAME" > Install

## Uploading the program
* Connect the Arduino board via USB
* Arduino IDE > Tools > Board: Adafruit Feather Bluefruit Sense
* Arduino IDE > Tools > Port: (Adafruit Feather Bluefruit Sense)
* Arduino IDE > Upload


Smart Key Finder and Usage Tracker 
Problem: Misplacing keys is a common issue, and there’s no easy way to check who used shared keys or when.
Solution: A device that tracks the location and usage of physical keys using BLE technology and an integrated accelerometer.
Features:
Hardware: A small BLE-enabled key fob that attaches to keyrings. It logs movement when keys are picked up and tracks their last known location.
App: Displays usage history (e.g., who last took the keys via profile linkage) and the real-time location of the keys within a certain radius.
Unique Touch: Ideal for shared environments (offices, roommates). The app can integrate with user accounts for accountability and even log keys left behind via geofencing.



# Note:

per usare notifiche:

```cpp
    vheartRateMeasurementCharacteristic.setCccdWriteCallback(cccdCallback);
```

