#include "nbcrustacier.h"

const int pinILS=2; // 5v
CCrustacier crust;// objet



void setup() {
  // put your setup code here, to run once:
Serial.begin(9600);
pinMode(pinILS, INPUT_PULLUP); // input_pullup invers la logic
/*pin=analogRead(A0);*/
}

void loop() {
  // put your main code here, to run repeatedly:

bool etatILS =digitalRead(pinILS); // lit
int nombre=crust.getVal(etatILS);
Serial.print("passage detecter : ");
Serial.print(nombre);
Serial.print("");


}
