
class CCrustacier
{
 private:
  bool pin; // etat actuel du capteur
  byte val; // comptage de passage
 public:
  CCrustacier();
  ~CCrustacier();
  int getVal(bool lectureCapteur); // methode de lecture capteur
};