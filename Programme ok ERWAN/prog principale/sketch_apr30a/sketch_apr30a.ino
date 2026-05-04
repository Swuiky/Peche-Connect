#include "batterie.h"
#include "nbcrustacier.h"
#include "volcasier.h"
#include <Adafruit_INA219.h>

Adafruit_INA219 ina219;

//donnée capteur pression
float P,V,distance;

// objet
CCrustacier crust;
CCapteurPression capteur;
Cbatterie batterie;

// pin 
const int pinILS = 2; // 5v
float pinPression = analogRead(0);
//valeur par defaut de calibration
const float OffSet = 0.69 ;  

// timers
unsigned long tBatterie = 0;
unsigned long tPression = 0;
unsigned long tILS = 0;

void setup() {  
  Serial.begin(9600);
  pinMode(pinILS, INPUT_PULLUP); // input_pullup invers la logic
//initialisation INA219
  if (!ina219.begin()) {
    Serial.println("INA219 non detecte !");
    while (1);
  }

}

void loop() {
  unsigned long now = millis();
  //batterie
  if (now - tBatterie >= 1000)
  {
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
  // Capteur de pression pour le vol
  if (now - tPression >= 1000)
  {
    // put your main code here, to run repeatedly:
    float pin=analogRead(0);
    V = pin * 5.00 / 1024;//Sensor output, volt = valeur comprise entre 0 et 1023, correspond a une tension entre 0 et 5 
    // tension capteur
    P = (OffSet - V) * 250; // Calculate pression de l'eau/ correction 
    //pression en Pascal

    //pression calculer a partir de la tension
    Serial.println("Water Pressure :");
    Serial.print(P, 1); // affiche P avec 1 chiffre apres la virgule
    Serial.println(" KPa"); // bar
    Serial.println(); // resultat final

    // calcule pression via methode classe c++
    float bar = capteur.getBar(P); //apelle la methode pour calcule
    Serial.println("Bar calculé : ");// affiche le resultat
    Serial.print(bar, 2); // affiche P avec 2 chiffre apres la virgule
    delay(500);
    Serial.println("");// remise a la ligne
    // affichage de la distance
    float m = capteur.getBar(distance); 
    Serial.println("le casier est à  ");// affiche le resultat
    Serial.print(m, 2); // affiche P avec 2 chiffre apres la virgule
    delay(500);
    Serial.println(" m");// remise a la ligne
  }
  // capteur ILS pour nb crustacier
  if (now - tILS >= 1000)
  {
    bool etatILS =digitalRead(pinILS); // lit
    int nombre=crust.getVal(etatILS);
    Serial.print("passage detecter : ");
    Serial.print(nombre);
    Serial.print("");

  }
}
