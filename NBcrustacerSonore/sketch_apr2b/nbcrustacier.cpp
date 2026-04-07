#include "nbcrustacier.h"
#include <Arduino.h>
//constructeur
CCrustacier::CCrustacier()
{
 trig=0;
 echo=0;
 val=0;
}
//enregistrement 
CCrustacier::CCrustacier(int t, int e)
{
 trig=t;
 echo=e;
 val=0; 
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);

}
//destructeur
CCrustacier::~CCrustacier()
{

}
// presence de temps crustacer 
float CCrustacier::getEcho(long distance)
{
 /* if (distance)
  {
    val++;
  }
  else
  {
    return val;
  }*/

// Envoi d'une impulsion TRIG de 10 µs
    digitalWrite(trig, LOW);
    delayMicroseconds(2);
    digitalWrite(trig, HIGH);
    delayMicroseconds(10);
    digitalWrite(trig, LOW);

 // Lecture de l'impulsion ECHO en microsecondes
long duration = pulseIn(echo, HIGH, 30000);    // Timeout ajusté (30ms = ~5m)
if (duration == 0) {
    return -1;
}
else
{
 distance = duration / 58.0; // conversion en cm
return distance;
}



}