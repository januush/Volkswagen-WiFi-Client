# Volkswagen-WiFi-Client
The application uses WiFi network to connect the android device with a customer functional control unit installed in Volkswagen Crafter. 

The app allows to monitor some specific vehicle data like engine speed, coolant temperature, door status etc.

What is more it can also controll up to 20 vehicle functions for instance starting the engine, closing the door or activating lights.

The communication between the App and the vehicle's control unit is designed using kfg-client libraries which are resposnsible for networking part (TCP/IP, UDP, EXLAP).

This library uses the EXLAP (Extensible Lightweight Asynchronous Protocol) for communication between control unit and the App.

