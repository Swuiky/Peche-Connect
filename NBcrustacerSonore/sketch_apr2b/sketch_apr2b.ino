#include "nbcrustacier.h"
#include <Arduino.h>

#define ECHOPIN A0 // Pin to receive echo pulse
#define TRIGPIN A1 // Pin to send trigger pulse
CCrustacier capteur;

void setup() {
  // put your setup code here, to run once:
Serial.begin(9600);
/*pinMode(ECHOPIN, INPUT); // recoie en entrer 
pinMode(TRIGPIN, OUTPUT); // envoie de l'impulsion en sortie*/
capteur = CCrustacier(TRIGPIN, ECHOPIN);
}

void loop() {
/*  // put your main code here, to run repeatedly:
digitalWrite(TRIGPIN, LOW); // Set the trigger pin to low for 2uS
delayMicroseconds(2);
digitalWrite(TRIGPIN, HIGH); // Send a 10uS high to trigger ranging
delayMicroseconds(10);
digitalWrite(TRIGPIN, LOW); // Send pin low again
int distance = pulseIn(ECHOPIN, HIGH,26000); // Read in times pulse 
distance= distance/5800; // convertion en metres
Serial.print(distance);
Serial.println(" m");
delay(50);// Wait 50mS before next ranging*/


  float distance = capteur.getEcho( distance);
  Serial.print("Distance : ");
  Serial.print(distance);
  Serial.println(" cm");
  delay(100);





}
