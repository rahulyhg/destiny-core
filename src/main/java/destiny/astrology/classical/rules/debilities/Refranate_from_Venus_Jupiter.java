/**
 * Created by smallufo at 2008/11/11 下午 10:09:47
 */
package destiny.astrology.classical.rules.debilities;

import java.util.Locale;

import destiny.astrology.AspectApplySeparateIF;
import destiny.astrology.HoroscopeContext;
import destiny.astrology.Planet;
import destiny.astrology.Point;
import destiny.astrology.RelativeTransitIF;
import destiny.astrology.RetrogradeIF;
import destiny.astrology.beans.RefranationBean;

public class Refranate_from_Venus_Jupiter extends Rule
{
  /** 判斷入相位，或是出相位 的實作 , 內定採用古典占星 */
  private final AspectApplySeparateIF aspectApplySeparateImpl;// = new AspectApplySeparateImpl(new AspectEffectiveClassical());
  
  /** 計算兩星呈現某交角的時間 , 內定是 Swiss ephemeris 的實作 */
  private final RelativeTransitIF relativeTransitImpl;
  
  /** 計算星體逆行的介面，目前只支援 Planet , 目前的實作只支援 Planet , Asteroid , Moon's Node (只有 True Node。 Mean 不會逆行！) */
  private final RetrogradeIF retrogradeImpl;// = new RetrogradeImpl();
  
  public Refranate_from_Venus_Jupiter(AspectApplySeparateIF aspectApplySeparateImpl , RelativeTransitIF relativeTransitImpl , RetrogradeIF retrogradeImpl)
  {
    super("Refranate_from_Venus_Jupiter");
    this.aspectApplySeparateImpl = aspectApplySeparateImpl;
    this.relativeTransitImpl = relativeTransitImpl;
    this.retrogradeImpl = retrogradeImpl;
  }

  @Override
  public boolean isApplicable(Planet planet, HoroscopeContext horoscopeContext)
  {
    //太陽 / 月亮不會逆行
    if (planet == Planet.MOON || planet == Planet.SUN)
      return false;
    
    Point otherPoint;
    RefranationBean bean;
    
    //只要其中一個成立，就將其設為 true
    boolean oneOk = false;

    if (planet != Planet.VENUS)
    {
      otherPoint = Planet.VENUS;
      bean = new RefranationBean(horoscopeContext , planet , otherPoint , aspectApplySeparateImpl , relativeTransitImpl , retrogradeImpl);
      if (bean.isRefranate())
      {
        addComment(Locale.TAIWAN, planet + " 在與 " + otherPoint + " 形成 " + bean.getApplyingAspect() + " 之前臨陣退縮(Refranation)");
        oneOk = true;
      }
    }
    
    if ( planet != Planet.JUPITER)
    {
      otherPoint = Planet.JUPITER;
      bean = new RefranationBean(horoscopeContext , planet , otherPoint , aspectApplySeparateImpl , relativeTransitImpl , retrogradeImpl);
      if (bean.isRefranate())
      {
        addComment(Locale.TAIWAN, planet + " 在與 " + otherPoint + " 形成 " + bean.getApplyingAspect() + " 之前臨陣退縮(Refranation)");
        oneOk = true;
      }
    }
    return oneOk;
  }

}