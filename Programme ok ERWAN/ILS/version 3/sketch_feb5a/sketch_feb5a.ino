#include "nbcrustacier.h"
const int pinILS=2; // 5v
CCrustacier crust;// objet

void setup() {
Serial.begin(9600);
pinMode(pinILS, INPUT_PULLUP); // input_pullup invers la logic
}

void loop() {
bool etatILS =digitalRead(pinILS); // lit
int nombre=crust.getVal(etatILS);
Serial.print("passage detecter : ");
Serial.print(nombre);
Serial.print("");
}
