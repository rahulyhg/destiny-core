/** 2009/10/21 上午2:44:14 by smallufo  */
package destiny.tools.location

import destiny.core.calendar.EastWest
import destiny.core.calendar.Location
import destiny.core.calendar.NorthSouth
import java.util.*

/**
 * 從經緯度求 TimeZone
 */
interface ITimeZone {

  /** 從經緯度查詢 timezone  */
  fun getTimeZoneId(lat: Double, lng: Double): String?

  fun getTimeZoneId(ew: EastWest,
                    lngDeg: Int,
                    lngMin: Int,
                    lngSec: Double,
                    nw: NorthSouth,
                    latDeg: Int,
                    latMin: Int,
                    latSec: Double): String? {
    val lng = Location.getLongitude(ew, lngDeg, lngMin, lngSec)
    val lat = Location.getLatitude(nw, latDeg, latMin, latSec)
    return getTimeZoneId(lat, lng)
  }

  fun getTimeZone(lng: Double , lat:Double): TimeZone? {
    return getTimeZoneId(lat, lng)?.let { TimeZone.getTimeZone(it) }
  }
}

