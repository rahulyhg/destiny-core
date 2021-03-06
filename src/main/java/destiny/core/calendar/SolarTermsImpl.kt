/*
 * @author smallufo
 * @date 2004/10/23
 * @time 上午 03:36:11
 */
package destiny.core.calendar

import destiny.astrology.*
import destiny.astrology.Coordinate.ECLIPTIC
import destiny.astrology.Planet.SUN
import destiny.astrology.Utils.getNormalizeDegree
import java.io.Serializable
import java.time.chrono.ChronoLocalDateTime
import java.util.*

/**
 * 節氣實作
 */
class SolarTermsImpl(
  private val starTransitImpl: IStarTransit,
  private val starPositionImpl: IStarPosition<*>
                    ) : ISolarTerms, Serializable {


  /**
   * 計算某時刻當下的節氣
   * 步驟：
   * 1. 計算太陽在黃道面的度數
   * 2. 比對此度數 , 將此度數除以 15 取整數
   * 3. 將以上的值代入 SolarTerms 即是答案
   */
  override fun getSolarTermsFromGMT(gmtJulDay: Double): SolarTerms {
    // Step 1: Calculate the Longitude of SUN
    val sp = starPositionImpl.getPosition(SUN, gmtJulDay, Centric.GEO, ECLIPTIC)
    // Step 2
    var index = (sp.lng / 15).toInt() + 3
    if (index >= 24)
      index -= 24
    return SolarTerms[index]
  }

  /**
   * 計算從某時(fromGmtTime) 到某時(toGmtTime) 之間的節氣 , in GMT
   *
   * @return List <SolarTermsTime>
  </SolarTermsTime> */
  override fun getPeriodSolarTermsGMTs(fromGmt: Double, toGmt: Double): List<SolarTermsTime> {
    var fromGmt = fromGmt
    var nowST = getSolarTermsFromGMT(fromGmt)

    var nextZodiacDegree = getNormalizeDegree((nowST.zodiacDegree + 15).toDouble())

    val resultList = ArrayList<SolarTermsTime>()

    while (fromGmt < toGmt) {
      val solarTermsTime: SolarTermsTime

      val fromGmtTime = starTransitImpl.getNextTransitGmtDateTime(SUN, nextZodiacDegree, ECLIPTIC, fromGmt, true)
      fromGmt = TimeTools.getGmtJulDay(fromGmtTime)

      if (fromGmt > toGmt)
        break
      nowST = nowST.next()
      solarTermsTime = SolarTermsTime(nowST, fromGmtTime)
      resultList.add(solarTermsTime)
      nextZodiacDegree = getNormalizeDegree((nextZodiacDegree + 15))
    }
    return resultList
  }

  /**
   * @return 計算，從 某時刻開始，的下一個（或上一個）節氣的時間點為何
   */
  override fun getSolarTermsTime(solarTerms: SolarTerms, fromGmtJulDay: Double, forward: Boolean): Double {
    val zodiacDegree = solarTerms.zodiacDegree
    return starTransitImpl.getNextTransitGmt(SUN, zodiacDegree.toDouble(), ECLIPTIC, fromGmtJulDay, forward)
  }

  /**
   * 計算此時刻的...
   * 上一個「節」是什麼，其 GMT JulDay 為何
   * 下一個「節」是什麼，其 GMT JulDay 為何
   */
  override fun getMajorSolarTermsGmtBetween(lmt: ChronoLocalDateTime<*>,
                                            location: ILocation): Pair<Pair<SolarTerms, Double>, Pair<SolarTerms, Double>> {

    val gmt = TimeTools.getGmtFromLmt(lmt, location)
    val gmtJulDay = TimeTools.getGmtJulDay(lmt, location)
    var prevMajorSolarTerms = getSolarTermsFromGMT(gmt)
    if (!prevMajorSolarTerms.isMajor)
      prevMajorSolarTerms = prevMajorSolarTerms.previous()

    val prevGmtJulDay = starTransitImpl.getNextTransitGmt(Planet.SUN, prevMajorSolarTerms.zodiacDegree.toDouble(), Coordinate.ECLIPTIC, gmtJulDay, false)

    val nextMajorSolarTerms = SolarTerms.getNextMajorSolarTerms(prevMajorSolarTerms, false)
    val nextGmtJulDay = starTransitImpl.getNextTransitGmt(Planet.SUN, nextMajorSolarTerms.zodiacDegree.toDouble(), Coordinate.ECLIPTIC, gmtJulDay, true)
    return Pair(Pair(prevMajorSolarTerms, prevGmtJulDay), Pair(nextMajorSolarTerms, nextGmtJulDay))
  }

}
