/**
 * @author smallufo 
 * Created on 2008/6/19 at 上午 1:55:27
 */ 
package destiny.astrology;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** 現代占星術，計算一張星盤中，星體交角列表的實作 */
public class HoroscopeAspectsCalculatorModern implements HoroscopeAspectsCalculatorIF , Serializable
{
  private Horoscope horoscope;
  private AspectEffectiveModern modern;
  
  /** 現代占星術，內定只計算重要性為「高」的角度 */
  private Collection<Aspect> aspects = Aspect.getAngles(Aspect.Importance.HIGH);
  
  public HoroscopeAspectsCalculatorModern()
  {
    modern = new AspectEffectiveModern();
  }

  @Override
  public void setHoroscope(Horoscope horoscope)
  {
    this.horoscope = horoscope;
  }

  /** 設定要計算哪些角度 */
  public void setAspects(Collection<Aspect> aspects)
  {
    this.aspects = aspects;
  }

  @Override
  public Map<Point , Aspect> getPointAspect(Point point , Collection<Point> points)
  {
    if (this.horoscope == null)
      throw new RuntimeException(getClass().getName() + " : horoscope is null ! call setHoroscope(horoscope) first !");
    Map<Point , Aspect> result = Collections.synchronizedMap(new HashMap<Point , Aspect>());
    double starDeg = horoscope.getPositionWithAzimuth(point).getLongitude();
    
    for(Point eachPoint : points)
    {
      double eachDeg = horoscope.getPositionWithAzimuth(eachPoint).getLongitude();
      for(Aspect eachAspect : aspects)
      {
        /** 直接比對度數，不考慮星體 */
        if (point != eachPoint && modern.isEffective(starDeg , eachDeg , eachAspect))
        {
          result.put(eachPoint , eachAspect);
        }
      }
    }
    return result;
  }

  @Override
  public String getTitle(Locale locale)
  {
    return "現代占星術";
  }
  
  @Override
  public String getDescription(Locale locale)
  {
    return "現代占星術實作";
  }




}