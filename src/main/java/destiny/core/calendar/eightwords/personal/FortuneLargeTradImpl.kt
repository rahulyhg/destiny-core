/**
 * Created by smallufo on 2018-04-28.
 */
package destiny.core.calendar.eightwords.personal

import destiny.core.Gender
import destiny.core.calendar.ILocation
import destiny.core.chinese.IStemBranch
import java.io.Serializable
import java.time.chrono.ChronoLocalDateTime
import java.util.*

/**
 * 傳統八字起大運法
 * 每條大運，都是固定 10 年
 *
 * 參考
 * https://sites.google.com/site/laughing8word/home/horoscope_figure
 */
class FortuneLargeTradImpl : IPersonFortuneLarge , Serializable {
  override fun getFortuneDataList(lmt: ChronoLocalDateTime<*>,
                                  location: ILocation,
                                  gender: Gender,
                                  count: Int): List<FortuneData> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getStemBranch(lmt: ChronoLocalDateTime<*>,
                             location: ILocation,
                             gender: Gender,
                             targetGmt: ChronoLocalDateTime<*>): IStemBranch {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getTitle(locale: Locale): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getDescription(locale: Locale): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}