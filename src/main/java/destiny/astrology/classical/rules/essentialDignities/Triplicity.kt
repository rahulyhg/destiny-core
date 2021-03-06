/**
 * @author smallufo
 * Created on 2007/12/30 at 上午 12:19:56
 */
package destiny.astrology.classical.rules.essentialDignities

import destiny.core.DayNight
import destiny.astrology.DayNightDifferentiator
import destiny.astrology.IHoroscopeModel
import destiny.astrology.Planet
import destiny.astrology.classical.ITriplicity

/** A planet in its own day or night triplicity (not to be confused with the modern triplicities).  */
class Triplicity(
  private val triplicityImpl : ITriplicity,
  private val dayNightImpl: DayNightDifferentiator) : EssentialRule() {

  override fun getResult(planet: Planet, h: IHoroscopeModel): Pair<String, Array<Any>>? {
    val sign = h.getZodiacSign(planet)
    return sign?.let {
      val dayNight = dayNightImpl.getDayNight(h.lmt, h.location)
      if (dayNight == DayNight.DAY && planet ===  triplicityImpl.getPoint(sign, DayNight.DAY) ||
          dayNight == DayNight.NIGHT && planet === triplicityImpl.getPoint(sign, DayNight.NIGHT) ) {
        logger.debug("{} 位於 {} 為其 {} 之 Triplicity" , planet , sign , dayNight)
        return@let "comment" to arrayOf(planet, sign, dayNight)
      } else
        return@let null
    }
  }
}
