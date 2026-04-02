#include <Arduino.h>
class CCapteurPression
{
  private:
  //donnée capteur
  float valPression;//valeur trame
  float pin;
 /* uint8_t _pin;
  const float _vMin = 0.5;     // Offset du capteur (0 MPa)
  const float _vMax = 4.5;     // Tension max
  const float _rangeMPa = 1.6; // Plage du capteur (MPa)
 */
  public:
  CCapteurPression();
  ~CCapteurPression();/*
  float readVoltage(); // lire la tension brute
  float readPressureMPa();*/ // calculer MPa
  float getBar(float valPression); // convertion en bar

};