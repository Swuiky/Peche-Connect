class CCapteurPression
{
  private:
  //donnée capteur
  float valPression;//valeur trame
  float pin;
  public:
  CCapteurPression();
  ~CCapteurPression();
  float getBar(float valPression);

};