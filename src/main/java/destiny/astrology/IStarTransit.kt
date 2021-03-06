/*
 * @author smallufo
 * @date 2004/10/23
 * @time 上午 04:14:07
 */
package destiny.astrology

import destiny.core.calendar.JulDayResolver1582CutoverImpl

import java.time.chrono.ChronoLocalDateTime
import java.util.function.Function

/**
 * 計算某星 Transit 的介面
 * 某星下次（或上次）行進到黃道/恆星 帶上某一點的時間 , 赤道座標系不支援!
 * SwissEph 內定實作是 StarTransitImpl
 *
 * TODO : 計算星體 Transit 到黃道某點的時間，僅限於 Planet , Asteroid , Moon's Node
 */
interface IStarTransit {

  /**
   * 傳回 GMT 時刻
   */
  fun getNextTransitGmt(star: Star, degree: Double, coordinate: Coordinate, fromGmt: Double, forward: Boolean): Double


  /**
   * 傳回 GMT
   */
  fun getNextTransitGmtDateTime(star: Star,
                                degree: Double,
                                coordinate: Coordinate,
                                fromGmt: Double,
                                forward: Boolean = true,
                                revJulDayFunc: Function<Double, ChronoLocalDateTime<*>> = Function {
                                  JulDayResolver1582CutoverImpl.getLocalDateTimeStatic(it)
                                }): ChronoLocalDateTime<*> {
    val gmtJulDay = getNextTransitGmt(star, degree, coordinate, fromGmt, forward)
    return revJulDayFunc.apply(gmtJulDay)
  }

}