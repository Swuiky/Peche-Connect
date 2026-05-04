#include "nbcrustacier.h"
#include <Arduino.h>
//constructeur
CCrustacier::CCrustacier()
{
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
  // detection front descandant : low --> high
  if(lectureCapteur == HIGH && pin==LOW)
  {
    val++;
    if(val>=256) val = 0; // reset 0
  }
 pin = lectureCapteur; // memorise etat
  return val;
}