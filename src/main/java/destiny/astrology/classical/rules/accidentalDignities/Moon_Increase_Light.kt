/**
 * @author smallufo
 * Created on 2007/12/29 at 上午 5:22:44
 */
package destiny.astrology.classical.rules.accidentalDignities

import destiny.astrology.IHoroscopeModel
import destiny.astrology.Planet
import destiny.astrology.Planet.MOON
import destiny.astrology.Planet.SUN

/** Moon increasing in light (月增光/上弦月) , or occidental of the Sun.  */
class Moon_Increase_Light : AccidentalRule() {

  override fun getResult(planet: Planet, h: IHoroscopeModel): Pair<String, Array<Any>>? {
    val moonDeg = planet.takeIf { it === MOON }
      ?.let { h.getPosition(it) }?.lng
    val sunDeg = h.getPosition(SUN)?.lng
    return if (moonDeg != null && sunDeg != null && IHoroscopeModel.isOccidental(moonDeg, sunDeg)) {
      "comment" to arrayOf<Any>(planet)
    } else {
      null
    }
  }
}
