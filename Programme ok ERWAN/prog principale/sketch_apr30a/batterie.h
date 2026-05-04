#include "Arduino.h"
#include <Adafruit_INA219.h>
class Cbatterie
{
  private:
  Adafruit_INA219 ina219;
  float valPourcent;
  static const int tableSize = 13;
  float voltageTable[tableSize];
  int percentTable[tableSize];
  public:
  Cbatterie();
  ~Cbatterie();
  int getPour(float valPourcent); // poucentage calculer
};