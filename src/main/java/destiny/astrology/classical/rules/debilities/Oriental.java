/**
 * @author smallufo 
 * Created on 2007/12/31 at 上午 3:16:57
 */ 
package destiny.astrology.classical.rules.debilities;

import java.util.Locale;

import destiny.astrology.Horoscope;
import destiny.astrology.HoroscopeContext;
import destiny.astrology.Planet;

/** Mercury, or Venus oriental to the Sun. */
public final class Oriental extends Rule
{

  public Oriental()
  {
    super("Oriental");
  }

  @Override
  public boolean isApplicable(Planet planet, HoroscopeContext horoscopeContext)
  {
    if (planet == Planet.MERCURY || planet == Planet.VENUS)
    {
      double planetDegree = horoscopeContext.getPosition(planet).getLongitude();
      double sunDegree    = horoscopeContext.getPosition(Planet.SUN).getLongitude();
      
      if (Horoscope.isOriental(planetDegree , sunDegree))
      {
        addComment(Locale.TAIWAN , planet + " 在太陽東邊");
        return true;
      }
    }
    return false;
  }

}