/*
 * @author smallufo
 * @date 2004/12/6
 * @time 下午 07:49:08
 */
package destiny.core.calendar.eightwords

import destiny.core.Descriptive
import destiny.core.calendar.ILocation
import destiny.core.calendar.TimeTools
import destiny.core.chinese.Branch
import java.time.LocalTime
import java.time.chrono.ChronoLocalDate
import java.time.chrono.ChronoLocalDateTime

/** 時辰的分界點實作 , SwissEph 的實作是 HourSolarTransImpl  */
interface IHour : Descriptive {

  fun getHour(gmtJulDay: Double, location: ILocation): Branch


  /**
   * @param lmt 傳入當地手錶時間
   * @param location 當地的經緯度等資料
   * @return 時辰（只有地支）
   */
  fun getHour(lmt: ChronoLocalDateTime<*>, location: ILocation): Branch {
    val gmtJulDay = TimeTools.getGmtJulDay(lmt, location)
    return getHour(gmtJulDay, location)
  }

  /**
   * @param gmtJulDay GMT 時間
   * @param location 地點
   * @param eb 下一個地支
   * @return 下一個地支的開始時刻
   */
  fun getGmtNextStartOf(gmtJulDay: Double, location: ILocation, eb: Branch): Double


  /**
   * @param lmt 傳入當地手錶時間
   * @param location 當地的經緯度等資料
   * @param eb 欲求之下一個地支開始時刻
   * @return 回傳 LMT 時刻
   */
  fun getLmtNextStartOf(lmt: ChronoLocalDateTime<*>, location: ILocation, eb: Branch, revJulDayFunc: Function1<Double , ChronoLocalDateTime<*>>): ChronoLocalDateTime<*> {
    val gmtJulDay = TimeTools.getGmtJulDay(lmt, location)
    val resultGmtJulDay = getGmtNextStartOf(gmtJulDay, location, eb)

    val resultGmt = revJulDayFunc.invoke(resultGmtJulDay)
    return TimeTools.getLmtFromGmt(resultGmt, location)
  }

  /**
   * accessory function , 傳回當地，一日內的時辰切割
   */
  fun getDailyMap(day : ChronoLocalDate, location : ILocation, revJulDayFunc: Function1<Double , ChronoLocalDateTime<*>>) : Map<Branch, ChronoLocalDateTime<*>> {
    val lmtStart = day.atTime(LocalTime.MIDNIGHT)

    return Branch.values().map { b ->
      val lmt: ChronoLocalDateTime<*> = getLmtNextStartOf(lmtStart, location, b, revJulDayFunc)
      b to lmt
    }.toList().sortedBy { (_ , lmt) -> lmt }.toMap()
  }
}
