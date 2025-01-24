[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/dshj-7kx)
# MSE TSM MobCom Project Bluetooth Smart Door System

## Project description: 
Enhance your home security with a Bluetooth-enabled smart door system that combines convenience and reliability.

see more [here](./docs/README.md)

## Agile
* [Add a new issue](../../issues/new) for each task.
* [Add a new board](../../projects/new) once, template: basic kanban.
* [Use the project board](../../projects/1) to keep track of who does which task.

## Team
* [Trinh Win-Hon](https://github.com/Winni00)
* [Alessio Tommasi](https://github.com/AlessioTommasi-supsi)

## Docs
* [Slides](Docs/Slides.pdf) (PDF)
* [Video](Docs/Video.mp4) (MP4)

## Code
* [Android](Android)
* [Arduino](Arduino)


<br>

--- 
# List 

## Achievements
- Lock/unlock via Bluetooth
- Read state via Bluetooth
- Create log or remote database
- Retrieve logs and device data, and parse JSON response
- view Logs
- show near device which i can connect

## Technical Issues
- Bluetooth
	- Communication
		- Discover correct services and characteristics
		- Handle errors: 
            - if i press open / close the message is not display correctly from the start, i need to refresh the page!
        
- Message
    - parsing correctly the message send /recived
        - UTF8 
	

- Sync compose with dynamic page
   - Volley
	- Handle errors

## Outlook
   - Fingerprint integration
   - User settings
   - If a module with built-in WiFi exists, create a remote open/close feature