#include "batterie.h"
#include "Arduino.h"
#include <Adafruit_INA219.h>
#include <Wire.h>
//constructeur
Cbatterie::Cbatterie()
{
    // Table de décharge Samsung 18650 Li-ion
  float vTable[tableSize] = {
    4.20, 4.10, 4.00, 3.90, 3.80,
    3.70, 3.60, 3.50, 3.40,
    3.30, 3.20, 3.10, 3.00
  };

  int pTable[tableSize] = {
    100, 95, 90, 80, 70,
    60, 50, 30, 20,
    10, 5, 2, 0
  };
  // sauvegarde de la valeur dans un tableau classe
  for (int i = 0; i < tableSize; i++) {
   voltageTable[i] = vTable[i];
   percentTable[i] = pTable[i];
  }
}
//destructeur
Cbatterie::~Cbatterie()
{}
int Cbatterie::getPour(float valPourcent) {
  // limite 
  if (valPourcent >= voltageTable[0]) return 100;
  if (valPourcent <= voltageTable[tableSize - 1]) return 0;
// calcule depuis les tableaux
  for (int i = 0; i < tableSize - 1; i++) { // parcour la table
    if (valPourcent <= voltageTable[i] && valPourcent > voltageTable[i + 1]) { // trouve le bon intevale
      float dv = voltageTable[i] - voltageTable[i + 1]; //ecart de tension
      float dp = percentTable[i] - percentTable[i + 1]; // ecart de pourcentage
      float ratio = (voltageTable[i] - valPourcent) / dv; // position relative
      return (int)(percentTable[i] - (ratio * dp)); // convertion pour affichage
    }
  }
  return 0; // arret
}

