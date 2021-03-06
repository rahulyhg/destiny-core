/**
 * @author smallufo
 * Created on 2007/12/29 at 上午 4:48:01
 */
package destiny.astrology.classical.rules.accidentalDignities

import destiny.astrology.IHoroscopeModel
import destiny.astrology.Planet

/** In the 9th house.  */
class House_9 : AccidentalRule() {

  override fun getResult(planet: Planet, h: IHoroscopeModel): Pair<String, Array<Any>>? {
    return h.getHouse(planet)
      ?.takeIf { it == 9 }
      ?.let { house -> Pair("comment", arrayOf(planet, house)) }
  }
}
