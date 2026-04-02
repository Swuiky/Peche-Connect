#include "nbcrustacier.h"
#include <Arduino.h>
//constructeur
CCrustacier::CCrustacier()
{
 /* pin=0; */
 pin= LOW; // etat initial
  val=0; //compteur
}
//destructeur
CCrustacier::~CCrustacier()
{

}
//obtenire valeur
int CCrustacier::getVal(bool lectureCapteur)
{
  /*pin=analogRead(A0);*/
  // detection front descandant : low --> high
  if(lectureCapteur == HIGH && pin==LOW)
  {
    
   /* val= val+1;*/
   val++;
    if(val>=256) val = 0; // reset 0
  }
 pin = lectureCapteur; // memorise etat
  return val;
 
}