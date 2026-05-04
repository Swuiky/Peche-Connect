#include <Arduino.h>
class CCapteurPression
{
  private:
  //donnée capteur
  float valPression;//valeur trame
  float pin;
  float P;
  float distance;
  public:
  CCapteurPression();
  ~CCapteurPression();
  float getBar(float valPression); // convertion en bar

};