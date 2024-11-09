# MSE TSM MobCom
## Hands-on of lesson 5

### a) Button-triggered LED, 15'
* Use blue onboard LED, pin 4, and the button, pin 7.
* Combine the previous examples to switch the LED.
* Or wire a LED to Grove port D2 and a button to D4.
* Use the [pin mapping](https://github.com/tamberg/mse-tsm-mobcom/wiki/Grove-Adapters#mapping) to adapt the pin numbers.

- ### Solution:
    -    State Machine:
        
                Button-triggered LED 
            
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

    -    PINOUT TABLE:
    
        
    | Grove | nRF52840 | Function |
    |-------|---------|----------|
    | D2    | 5       | LED      |
    | D4    | 10       | Button   |


    code solution: [Here](./LightSwitchStateMachine/LightSwitchStateMachine.ino)
        

### b) State machine, 15'
* Copy and complete the code of the state machine.
* Make sure it works, with a button and LED setup.
* Change it to switch off only, if the 2nd press is long.
* Let's define long as > 1s, measure time with [millis()](https://www.arduino.cc/reference/en/language/functions/time/millis/).

- ### Solution:
    
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
    
code if solution: [Here](./LightSwitchStateMachineMillis/LightSwitchStateMachineMillis.ino)

### c) Humidity alert, 15'
* Design a state machine with this specification:
* Button press sets humidity += 10% as threshold.
* Red LED turns on, as long as monitoring is active.
* Once threshold has been crossed, blue LED turns on.
* Button confirms alert, red led turns on 1 s, then off.

- ### Solution:

        Humidity Alert State Machine

        b:  user button
        h:  humidity %
        bl: blue LED
        rl: red LED
        t0: time

        +----+                                         +----+
        | S0 +---pressed(b)|treshold=h+10%;rl=on------>+ S1 |
        +--+-+                                         +-+--+
           ^                                             |
           |                                         !pressed(b)
        millis()-t0>1000|rl=off                          |
           |                                             v
        +----+                                         +----+
        | S5 |                                         | S2 |
        +----+                                         +----+
           ^                                             |   
           |                         h>threshold|rl=off;bl=on
        !pressed(b)                                      |
           |                                             v   
        +--+-+                                         +-+--+
        | S4 |<--pressed(b)|rl=on;bl=off;t0=millis()---+ S3 |
        +----+                                         +----+


### d) Homework, max. 3h
* Implement the humidity alert you designed before.
* Document the device state machine (PDF or PNG).
* Commit the code and docs to the hands-on repo.
* Test your device in a humid environment<sup>*</sup>.

<sup>*</sup>Never submerge or sprinkle electronics.

- ### Solution:
    -   State Machine: displayed on (C) solution
    -   code solution: [text](HumidityAlert/HumidityAlert.ino)