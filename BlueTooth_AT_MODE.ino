/* 
HC05 - Bluetooth AT-Command mode 
modified on 10 Feb 2019 
by Saeed Hosseini 
https://electropeak.com/learn/guides
*/ 
#include "SoftwareSerial.h"
SoftwareSerial MyBlue(10, 11); // RX | TX + CONNECT KEY PIN TO V5 
// SET MONITOR TO 384000 AND INSTEAD OF NEW LINE TO NL & CR
void setup() 
{ 
 Serial.begin(38400); 
 MyBlue.begin(38400);  //Baud Rate for AT-command Mode.  
 Serial.println("***AT commands mode***"); 
} 
void loop() 
{ 
//  Serial.println("Wating for Serial");
 //from bluetooth to Terminal. 
 while (Serial.available()) {
  delay(1);
//  Serial.println("bdika");
   MyBlue.write(Serial.read());
 }
//  Serial.println("wating for BT");
 //from termial to bluetooth 
 while (MyBlue.available()) {
//    Serial.println("bdika2");
    Serial.write(MyBlue.read());
 }
}
