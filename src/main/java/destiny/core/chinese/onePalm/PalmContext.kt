/**
 * Created by smallufo on 2018-04-30.
 */
package destiny.core.chinese.onePalm

import destiny.astrology.Coordinate
import destiny.astrology.HouseSystem
import destiny.core.Gender
import destiny.core.calendar.ILocation
import destiny.core.calendar.chinese.ChineseDateHour
import destiny.core.calendar.chinese.IChineseDate
import destiny.core.calendar.chinese.IFinalMonthNumber
import destiny.core.calendar.eightwords.*
import destiny.core.chinese.Branch
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.time.chrono.ChronoLocalDateTime


class PalmContext(override val positiveImpl: IPositive,
                  val chineseDateImpl: IChineseDate,
                  val dayImpl: IDay,
                  override val hourImpl: IHour,
                  override val midnightImpl: IMidnight,
                  val risingSignImpl: IRisingSign,
                  val yearMonthImpl: IYearMonth,
                  override val monthAlgo: IFinalMonthNumber.MonthAlgo,
                  override val changeDayAfterZi: Boolean,
                  override val trueRisingSign: Boolean,
                  override val clockwiseHouse: Boolean) : IPalmContext, Serializable {

  /** 沒帶入節氣資料 , 內定把月份計算採用 [IFinalMonthNumber.MonthAlgo.MONTH_LEAP_SPLIT15] 的演算法  */
  override fun getPalmWithoutSolarTerms(gender: Gender,
                                        yearBranch: Branch,
                                        leap: Boolean,
                                        monthNum: Int,
                                        dayNum: Int,
                                        hourBranch: Branch) : IPalmModel {
    val positive = if (positiveImpl.isPositive(gender, yearBranch)) 1 else -1

    logger.trace("positive = {}", positive)

    var finalMonthNum = monthNum
    if (leap && dayNum > 15)
    // 若為閏月，15日以後算下個月
      finalMonthNum++

    logger.trace("yearBranch = {}", yearBranch)

    // 年上起月
    val month = yearBranch.next((finalMonthNum - 1) * positive)

    // 月上起日
    val day = month.next((dayNum - 1) * positive)

    // 日上起時
    val hour = day.next(hourBranch.index * positive)

    // 命宮
    val steps = Branch.卯.getAheadOf(hourBranch)
    val main = hour.next(steps * positive)

    val houseMap = (0..11).map { i -> if (clockwiseHouse) main.next(i) else main.prev(i) }
      .zip(IPalmModel.House.values()).toMap()

    return PalmModel(gender, yearBranch, month, day, hour, houseMap)
  }



  /**
   * 本命盤：最完整的計算方式 , 包含時分秒、經緯度、時區
   */
  override fun getPalm(gender: Gender,
                       lmt: ChronoLocalDateTime<*>,
                       loc: ILocation,
                       place: String?,
                       name: String?): IPalmMetaModel {

    val cDate = chineseDateImpl.getChineseDate(lmt, loc, dayImpl, hourImpl, midnightImpl, changeDayAfterZi)
    val hourBranch = hourImpl.getHour(lmt, loc)
    val chineseDateHour = ChineseDateHour(cDate, hourBranch)

    val trueRising: Branch? = if (trueRisingSign) {
      // 真實上升星座
      risingSignImpl.getRisingSign(lmt, loc, HouseSystem.PLACIDUS, Coordinate.ECLIPTIC).branch
    } else {
      null
    }

    // 節氣的月支
    val monthBranch = yearMonthImpl.getMonth(lmt, loc).branch
    val palm = getPalm(gender, chineseDateHour, trueRising, monthBranch)
    return PalmMetaModel(palm, lmt, loc, place, name, chineseDateHour)
  }

  companion object {
    val logger = LoggerFactory.getLogger(PalmContext::class.java)!!
  }
}