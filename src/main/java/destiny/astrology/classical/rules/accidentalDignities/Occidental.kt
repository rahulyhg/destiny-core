/**
 * @author smallufo
 * Created on 2007/12/29 at 上午 5:20:28
 */
package destiny.astrology.classical.rules.accidentalDignities

import destiny.astrology.IHoroscopeModel
import destiny.astrology.Planet
import destiny.astrology.Planet.*

/** Mercury, or Venus occidental of (rising after) the Sun.  */
class Occidental : AccidentalRule() {

  override fun getResult(planet: Planet, h: IHoroscopeModel): Pair<String, Array<Any>>? {
    val planetDeg: Double? = planet.takeIf { it === MERCURY || it === VENUS }
      ?.let { h.getPosition(it) }?.lng
    val sunDeg: Double? = h.getPosition(SUN)?.lng
    return if (planetDeg != null && sunDeg != null && IHoroscopeModel.isOccidental(planetDeg, sunDeg)) {
      "comment" to arrayOf<Any>(planet)
    } else {
      null
    }

  }
}
