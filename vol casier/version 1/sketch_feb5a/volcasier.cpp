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
    


 valPression = pin /100000;

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
