/*
 * @author smallufo
 * @date 2004/12/6
 * @time 下午 01:59:49
 */
package destiny.core.calendar.eightwords;

import destiny.core.calendar.Location;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class MidnightLmtImplTest {

  @Test
  public void testGetNextMidnight() {
    MidnightLmtImpl impl = new MidnightLmtImpl();
    Location location = new Location(Location.EastWest.EAST, 121, 0, 0, Location.NorthSouth.NORTH, 25, 0, 0, TimeZone.getTimeZone("Asia/Taipei"));
    LocalDateTime expected, actual;

    LocalDateTime lmt = LocalDateTime.of(2004, 12, 6, 14, 10, 0);
    actual = impl.getNextMidnight(lmt, location);
    expected = LocalDateTime.of(2004, 12, 7, 0, 0, 0);
    assertEquals(expected, actual);

    lmt = LocalDateTime.of(2004, 12, 31, 0, 0, 0);
    actual = impl.getNextMidnight(lmt, location);
    expected = LocalDateTime.of(2005, 1, 1, 0, 0, 0);
    assertEquals(expected, actual);
  }

}
