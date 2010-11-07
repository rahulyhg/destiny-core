/**
 * @author smallufo 
 * Created on 2007/11/24 at 下午 9:01:16
 */ 
package destiny.astrology;

import java.io.Serializable;
import java.util.Collection;

/**
 * <pre>
 * 現代占星的交角容許度
 * 參考資料
 * http://www.myastrologybook.com/aspects-and-orbs.htm
 * </pre>
 */
public class AspectEffectiveModern implements Serializable , AspectEffectiveIF
{
  /** 「不考慮行星」的交角容許度 , 內定採用 AspectOrbsDefaultImpl , 未來可以注入資料庫實作的版本 */
  private AspectOrbsIF aspectOrbsImpl = new AspectOrbsDefaultImpl();
  
  /** 「考量行星的」交角容許度實作，內定採用 AspectOrbsPlanetDefaultImpl , 未來可以注入資料庫實作的版本 */
  private AspectOrbsPlanetIF aspectOrbsPlanetImpl = new AspectOrbsPlanetDefaultImpl(); 
  
  public AspectEffectiveModern()
  {
  }
  
  public void setAspectOrbsImpl(AspectOrbsIF impl)
  {
    this.aspectOrbsImpl = impl;
  }
  
  public void setAspectOrbsPlanetImpl(AspectOrbsPlanetIF impl)
  {
    this.aspectOrbsPlanetImpl = impl;
  }
  
  /** 直接比對度數是否形成交角，不考慮星體 */
  public boolean isEffective(double deg1, double deg2 , Aspect aspect)
  {
    double angle = Horoscope.getAngle(deg1, deg2);
    if (Math.abs(angle - aspect.getDegree()) <= aspectOrbsImpl.getAspectOrb(aspect))
      return true;
    return false;
  }
  
  public static boolean isEffective(double deg1 , double deg2 , Aspect aspect , double orb)
  {
    double angle = Horoscope.getAngle(deg1, deg2);
    if (Math.abs(angle - aspect.getDegree()) <= orb)
      return true;
    return false;
  }

  
  /** 有些版本有考慮星體，例如：太陽月亮的交角，會有較高的容許度 */
  @Override
  public boolean isEffective(Point p1 , double deg1 , Point p2 , double deg2 , Aspect aspect)
  {
    double orb = aspectOrbsPlanetImpl.getPlanetAspectOrb( p1,  p2, aspect); //從「考量行星」的交角容許度實作找起
    if (orb < 0) //如果找不到，會傳回小於0的值
      orb = aspectOrbsImpl.getAspectOrb(aspect); //再從「不考慮行星」的交角容許度尋找
    double angle = Horoscope.getAngle(deg1, deg2);
    if (Math.abs(angle - aspect.getDegree()) <= orb)
      return true;
    return false;    
  }

  /** 考慮個別星體交角容許度的實作 */
  @Override
  public boolean isEffective(Point p1, double deg1, Point p2, double deg2, Aspect... aspects)
  {
    for(Aspect aspect : aspects)
    {
      if (isEffective(p1, deg1, p2, deg2, aspect))
        return true;
    }
    return false;
  }

  /** 考慮個別星體交角容許度的實作 */
  @Override
  public boolean isEffective(Point p1, double deg1, Point p2, double deg2, Collection<Aspect> aspects)
  {
    for(Aspect aspect : aspects)
    {
      if (isEffective(p1, deg1, p2, deg2, aspect))
        return true;
    }
    return false;
  }
}