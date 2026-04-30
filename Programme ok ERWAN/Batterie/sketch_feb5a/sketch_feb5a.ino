#include "batterie.h"
#include <Adafruit_INA219.h>
Adafruit_INA219 ina219;
Cbatterie batterie;

void setup() {
  Serial.begin(9600);

  if (!ina219.begin()) {
    Serial.println("INA219 non detecte !");
    while (1);
  }

  Serial.println("Systeme initialise");
}

void loop() {
  float busVoltage = ina219.getBusVoltage_V();
  float shuntVoltage = ina219.getShuntVoltage_mV() / 1000.0; // convertion en mV --> volt
  float batteryVoltage = busVoltage + shuntVoltage; // tension sortie de pile + résistance
  float tension = batteryVoltage; // mesurée via INA219
  int percentage = batterie.getPour(batteryVoltage); // appelle de la methode 
  //affichage
  Serial.print("Tension batterie : ");
  Serial.print(batteryVoltage, 4);
  Serial.print(";    pourcentage batterie : ");
  Serial.print(percentage);
  Serial.println(" %");

  delay(2000);
}













/*
void setup() {
  Serial.begin(9600);
  while (!Serial) {
    delay(1);
  }
  Serial.println("Initialisation du capteur INA219...");
  if (!ina219.begin()) {
    Serial.println("Erreur : Impossible de trouver INA219 !");
    while (1) { delay(10); };
  }
  // Optionnel : régler la calibration pour des courants plus élevés
  // ina219.setCalibration_32V_2A();
  Serial.println("INA219 initialisé !");
 unsigned long lastMillis = millis();

}

void loop() {

  float tension_bus = ina219.getBusVoltage_V();      // Tension côté charge
  float tension_shunt = ina219.getShuntVoltage_mV(); // Tension shunt
  float courant_mA = ina219.getCurrent_mA();         // Courant
  float puissance_mW = ina219.getPower_mW();         // Puissance
  float mAh_utilises;
  float deltaHeures;
  float temps_restant_h;
  Serial.println("------ Mesures INA219 ------");
  Serial.print("Tension (bus)   : "); Serial.print(tension_bus); Serial.println(" V");
  Serial.print("Tension shunt   : "); Serial.print(tension_shunt); Serial.println(" mV");
  Serial.print("Courant         : "); Serial.print(courant_mA); Serial.println(" mA");
  Serial.print("Puissance       : "); Serial.print(puissance_mW); Serial.println(" mW");
  Serial.print("courrant utilisé   : "); Serial.print(mAh_utilises); Serial.println(" mAh");
  Serial.print("temp utilisé (delta)  : "); Serial.print(deltaHeures); Serial.println(" h");
  Serial.print("temp utilisé   : "); Serial.print(temps_restant_h); Serial.println(" h");
 
 Serial.println("---------------------------\n");

  float valPourcent;
  valPourcent = batterie.getPour();
  Serial.println("le pourcentage est : "); 
  Serial.print(valPourcent);
  Serial.println();
  delay(6000);

}*/
