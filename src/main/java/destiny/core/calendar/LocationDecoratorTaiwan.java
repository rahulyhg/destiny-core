/**
 * @author smallufo 
 * Created on 2008/1/17 at 上午 1:21:46
 */ 
package destiny.core.calendar;

import destiny.core.calendar.Location.EastWest;
import destiny.core.calendar.Location.NorthSouth;
import destiny.utils.Decorator;

public class LocationDecoratorTaiwan implements Decorator<Location>
{
  @Override
  public String getOutputString(Location location)
  {
    StringBuffer sb = new StringBuffer();
    sb.append((location.getEastWest()==EastWest.EAST ? "東經" : "西經") +" ");
    sb.append(location.getLongitudeDegree() + "度 ");
    sb.append(location.getLongitudeMinute() + "分 ");
    sb.append(location.getLongitudeSecond() + "秒 , ");

    sb.append((location.getNorthSouth()==NorthSouth.NORTH ? "北緯" : "南緯") + " ");
    sb.append(location.getLatitudeDegree() + "度 ");
    sb.append(location.getLatitudeMinute() + "分 ");
    sb.append(location.getLatitudeSecond() + "秒.");
    sb.append(" 時差 " + (location.getTimeZone().getRawOffset() / 60000) + " 分鐘  ");
    sb.append("高度 " + location.getAltitudeMeter() + " 公尺.");
    return sb.toString();
  }

}