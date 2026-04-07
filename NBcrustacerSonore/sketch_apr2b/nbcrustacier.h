#include <Arduino.h>
class CCrustacier
{
 private:
  float trig; // pin trig
  float echo; // pin echo
  int val; // comptage de passage
 public:
  CCrustacier();
  CCrustacier(int t, int e);
  ~CCrustacier();
  float getEcho(long distance); // methode de lecture capteur
};