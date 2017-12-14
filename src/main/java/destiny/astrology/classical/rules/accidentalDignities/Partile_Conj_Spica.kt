/**
 * @author smallufo
 * Created on 2007/12/30 at 上午 12:02:30
 */
package destiny.astrology.classical.rules.accidentalDignities

import destiny.astrology.*
import org.jooq.lambda.tuple.Tuple
import org.jooq.lambda.tuple.Tuple2
import java.util.*

/** Partile conjunct Spica at 23deg50' Libra in January 2000.  */
class Partile_Conj_Spica : Rule() {


  override fun getResult(planet: Planet, h: Horoscope): Optional<Tuple2<String, Array<Any>>> {

    val planetDegree = h.getPositionOpt(planet).map<Double>{ it.lng }
    val spicaDeg = h.getPositionOpt(FixedStar.SPICA).map<Double>{ it.lng }

    if (planetDegree.isPresent && spicaDeg.isPresent) {
      if (AspectEffectiveModern.isEffective(planetDegree.get(), spicaDeg.get(), Aspect.CONJUNCTION, 1.0)) {
        logger.debug("{} 與 {} 形成 {}", planet, FixedStar.SPICA, Aspect.CONJUNCTION)
        return Optional.of(Tuple.tuple("comment", arrayOf(planet, FixedStar.SPICA, Aspect.CONJUNCTION)))
      }
    }
    return Optional.empty()
  }

  override fun getResult2(planet: Planet, h: Horoscope): Pair<String, Array<Any>>? {
    val planetDeg: Double? = h.getPosition(planet)?.lng
    val spicaDeg : Double? = h.getPosition(FixedStar.SPICA)?.lng

    return if (planetDeg != null && spicaDeg != null && AspectEffectiveModern.isEffective(planetDeg , spicaDeg , Aspect.CONJUNCTION , 1.0)) {
      logger.debug("{} 與 {} 形成 {}", planet, FixedStar.SPICA, Aspect.CONJUNCTION)
      "comment" to arrayOf(planet , FixedStar.SPICA , Aspect.CONJUNCTION)
    } else
      null
  }
}
