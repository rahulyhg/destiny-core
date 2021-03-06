/**
 * Created by smallufo on 2017-07-09.
 */
package destiny.astrology

import destiny.core.calendar.ILocation

open class PositionLunarPointImpl internal constructor(lunarPoint: LunarPoint) :
  AbstractPositionImpl<LunarPoint>(lunarPoint) {

  override fun getPosition(gmtJulDay: Double,
                           loc: ILocation,
                           centric: Centric,
                           coordinate: Coordinate,
                           starPositionImpl: IStarPosition<*>): IPos {
    return starPositionImpl.getPosition(point , gmtJulDay , loc.lng , loc.lat , loc.altitudeMeter?:0.0 , centric , coordinate , 0.0 , 1013.25)
  }
}
