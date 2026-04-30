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
//obtenire valeur
float CCapteurPression::getBar(float valPression)
{ 
 P=analogRead(0);
 valPression = P /100; // 1bar = KPa/100
 distance = valPression * 10.197;
  if(distance >5)
  {
   return valPression;
  }
  else
  {
    Serial.println("le casier est remonter à la surface");
    return valPression;
  }
}
