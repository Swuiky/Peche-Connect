#include "nbcrustacier.h"
#include <Arduino.h>

#define ECHOPIN A0 // Pin to receive echo pulse
#define TRIGPIN A1 // Pin to send trigger pulse
CCrustacier capteur;

void setup() {
  // put your setup code here, to run once:
Serial.begin(9600);
capteur = CCrustacier(TRIGPIN, ECHOPIN);
}

void loop() {
// calcule de la distance en cm
  float distance = capteur.getEcho( distance);
  Serial.print("Distance : ");
  Serial.print(distance);
  Serial.println(" cm");
  delay(100);
// incrementation de valeur
int valeur = capteur.getval(distance);
 Serial.print("valeur : ");
  Serial.print(valeur);
  Serial.println(" .");
  delay(100);
}
