/**
 * @author smallufo
 * Created on 2007/12/29 at 上午 5:20:28
 */
package destiny.astrology.classical.rules.accidentalDignities

import destiny.astrology.Horoscope
import destiny.astrology.Planet
import destiny.astrology.Planet.*
import org.jooq.lambda.tuple.Tuple2
import java.util.*

/** Mercury, or Venus occidental of (rising after) the Sun.  */
class Occidental : Rule() {

  override fun getResult(planet: Planet, h: Horoscope): Optional<Tuple2<String, Array<Any>>> {
    return getResult2(planet , h).toOld()
  }

  override fun getResult2(planet: Planet, h: Horoscope): Pair<String, Array<Any>>? {
    val planetDeg: Double? = planet.takeIf { it === MERCURY || it === VENUS }
      ?.let { h.getPosition(it) }?.lng
    val sunDeg: Double? = h.getPosition(SUN)?.lng
    return if (planetDeg != null && sunDeg != null && Horoscope.isOccidental(planetDeg , sunDeg)) {
      "comment" to arrayOf<Any>(planet)
    } else {
      null
    }

  }
}