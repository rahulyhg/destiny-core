/**
 * @author smallufo 
 * Created on 2008/1/17 at 上午 1:21:46
 */ 
package destiny.core.calendar;

import destiny.core.calendar.Location.EastWest;
import destiny.core.calendar.Location.NorthSouth;
import destiny.utils.Decorator;
import org.jetbrains.annotations.NotNull;

public class LocationDecoratorTaiwan implements Decorator<Location>
{
  @NotNull
  @Override
  public String getOutputString(@NotNull Location location)
  {
    StringBuffer sb = new StringBuffer();
    sb.append(location.getEastWest() == EastWest.EAST ? "東經" : "西經").append(" ");
    sb.append(location.getLongitudeDegree()).append("度 ");
    sb.append(location.getLongitudeMinute()).append("分 ");
    sb.append(location.getLongitudeSecond()).append("秒 , ");

    sb.append(location.getNorthSouth() == NorthSouth.NORTH ? "北緯" : "南緯").append(" ");
    sb.append(location.getLatitudeDegree()).append("度 ");
    sb.append(location.getLatitudeMinute()).append("分 ");
    sb.append(location.getLatitudeSecond()).append("秒.");
    sb.append("高度 ").append(location.getAltitudeMeter()).append(" 公尺.");
    sb.append(" 時區 ").append(location.getTimeZone().getID());
    if (location.isMinuteOffsetSet())
      sb.append(" 時差 ").append(location.getMinuteOffset()).append(" 分鐘.");
      //sb.append(" 時差 " + (location.getTimeZone().getRawOffset() / 60000) + " 分鐘  ");
    
    return sb.toString();
  }

}
