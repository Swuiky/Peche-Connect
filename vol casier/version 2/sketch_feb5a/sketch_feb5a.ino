#include "volcasier.h"

CCapteurPression capteur;
const float OffSet=0.483; //valeur par defaut de calibration 
  //donnée capteur
float P,V;


void setup() {

 Serial.begin(9600);        // open serial port, set the baud rate to 9600 bps
  Serial.println("/** mesure de presion  **/");
}

void loop() {
 // put your main code here, to run repeatedly:
 float pin=analogRead(A0);
 V = pin * 5.00 / 1024;//Sensor output, volt = valeur comprise entre 0 et 1023, correspond a une tension entre 0 et 5 
// tension capteur
  P = (V - OffSet) * 250; // Calculate pression water/ correction 
//pression en Pascal

 //pression calculer a partir de la tension
/*  Serial.println("Water Pressure :");
  Serial.print(P, 1); // affiche P avec 1 chiffre apres la virgule
  Serial.println(" bar"); // bar
  Serial.println(); // resultat final*/

  // calcule pression via methode classe c++
  float bar = capteur.getBar(P); //apelle la methode pour calcule
  Serial.println("Bar calculé : ");// affiche le resultat
  Serial.print(bar, 2); // affiche P avec 2 chiffre apres la virgule
  delay(500);
 Serial.println("");// remise a la ligne

/*
  float voltage = capteur.readVoltage();
  float pressureBar = capteur.getBar();

  Serial.print("Tension : ");
  Serial.print(voltage);
  Serial.print(" V   |   Pression : ");
  Serial.print(pressureBar);
  Serial.println(" bar");

  delay(500);
*/
  
}
