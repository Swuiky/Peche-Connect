#include <Arduino.h>
class CCrustacier
{
 private:
  float trig; // pin trig
  float echo; // pin echo
  int val; // comptage de passage
  long distance;
 public:
  CCrustacier();
  CCrustacier(int t, int e);
  ~CCrustacier();
  float getEcho(long distance); // methode de lecture capteur
  float getval(long distance);//incrementation
};