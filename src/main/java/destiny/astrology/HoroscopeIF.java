/*
 * @author smallufo
 * @date 2004/10/22
 * @time 下午 10:29:42
 */
package destiny.astrology;

import destiny.core.calendar.Location;
import destiny.core.calendar.Time;

public interface HoroscopeIF
{
  /**
   * 取得某時刻 在某地 的 星盤 , 裡面包含 Planet , Asteroid , Hamburger 三種星體的位置(含地平方位角 Azimuth)
   */
  public Horoscope getHoroscope(Time gmt , Location location , HouseSystem houseSystem , 
      Coordinate coordinate , double temperature , double pressure , 
      StarPositionWithAzimuthIF positionWithAzimuthImpl ) ;
}