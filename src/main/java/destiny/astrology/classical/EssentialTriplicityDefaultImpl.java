/**
 * @author smallufo 
 * Created on 2007/12/11 at 下午 11:28:19
 */ 
package destiny.astrology.classical;

import java.io.Serializable;

import com.google.common.collect.ImmutableMap;

import destiny.astrology.DayNight;
import destiny.astrology.Element;
import destiny.astrology.Planet;
import destiny.astrology.Point;
import destiny.astrology.ZodiacSign;

/**
 * Essential Triplicity 實作 , 參考 Ptolemy's Table : <br>
 * <pre>
 *      | 白天 | 夜晚
 * -------------------
 * 火相 | 太陽 | 木星 
 * 土相 | 金星 | 月亮
 * 風相 | 土星 | 水星
 * 水相 | 火星 | 火星 
 * </pre>
 */
public class EssentialTriplicityDefaultImpl implements EssentialTriplicityIF , Serializable
{
  private final static ImmutableMap<Element,Planet> dayMap = new ImmutableMap.Builder<Element , Planet>()
    .put(Element.FIRE  , Planet.SUN)
    .put(Element.EARTH , Planet.VENUS)
    .put(Element.AIR   , Planet.SATURN)
    .put(Element.WATER , Planet.MARS)
    .build();
  
  private final static ImmutableMap<Element,Planet> nightMap = new ImmutableMap.Builder<Element , Planet>()
    .put(Element.FIRE  , Planet.JUPITER)
    .put(Element.EARTH , Planet.MOON)
    .put(Element.AIR   , Planet.MERCURY)
    .put(Element.WATER , Planet.MARS)
    .build();
  
  @Override
  /** 取得黃道帶上某星座，其 Triplicity 是什麼星  */ 
  public Point getTriplicityPoint(ZodiacSign sign, DayNight dayNight)
  {
    switch (dayNight)
    {
      case DAY : return dayMap.get(sign.getElement());
      case NIGHT : return nightMap.get(sign.getElement());
    }
    throw new RuntimeException("Cannot find DayNight : " + dayNight + " of Sign : " + sign);
  }
}