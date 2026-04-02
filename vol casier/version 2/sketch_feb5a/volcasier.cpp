#include "volcasier.h"
#include <Arduino.h>

//constructeur
CCapteurPression::CCapteurPression()
{
 valPression=0;
 
}
//destructeur
CCapteurPression::~CCapteurPression()
{

}
/*// lire la tension brute
float CCapteurPression::readVoltage()
{
  int raw = analogRead(_pin);
  return raw * (5.0 / 1023.0);

}
//calculer MPa 
float CCapteurPression::readPressureMPa()
{
      float V = readVoltage();

    // Protection : tension en dessous de l’offset
    if (V < _vMin) return 0.0;

    // Conversion linéaire donnée par la documentation SEN0257
    float pressure = (V - _vMin) * (_rangeMPa / (_vMax - _vMin));

    return pressure; // en MPa

}*/
//obtenire valeur
float CCapteurPression::getBar(float valPression)
{ 
 /* return readPressureMPa() * 10.0;} // 1 MPa = 10 bar*/

 valPression = pin /100000; // 1bar = 100 000 Pa

  if(valPression>1)
  {
   return valPression;
  }
  else
  {
    Serial.println("le casier est remonter à la surface");
    return valPression;
  }
}