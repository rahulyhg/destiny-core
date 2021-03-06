/*
 * @author smallufo
 * @date 2005/5/17
 * @time 上午 07:43:25
 */
package destiny.core.calendar.eightwords.personal

import destiny.core.Gender
import destiny.core.calendar.ILocation
import destiny.core.calendar.TimeTools
import java.time.chrono.ChronoLocalDateTime

/**
 * 取得大運的順逆
 */
interface IFortuneDirection {

  fun isForward(gmtJulDay : Double , loc : ILocation , gender: Gender) : Boolean

  fun isForward(lmt: ChronoLocalDateTime<*> , loc: ILocation , gender: Gender) : Boolean {
    val gmtJulDay = TimeTools.getGmtJulDay(lmt, loc)
    return isForward(gmtJulDay , loc, gender)
  }

  /** 大運是否順行  */
//  fun isForward(gender : Gender, eightWords: EightWords): Boolean
}
