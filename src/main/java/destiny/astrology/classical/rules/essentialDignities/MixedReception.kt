/**
 * @author smallufo
 * Created on 2007/12/30 at 上午 3:56:55
 */
package destiny.astrology.classical.rules.essentialDignities

import destiny.astrology.DayNightDifferentiator
import destiny.astrology.Horoscope
import destiny.astrology.Planet
import destiny.astrology.classical.Dignity
import destiny.astrology.classical.EssentialUtils
import org.jooq.lambda.tuple.Tuple2
import java.util.*

/**
 * 廟旺互容 <br></br>
 * 舉例：水星到摩羯，火星到雙子 <br></br>
 * 摩羯為火星 Exaltation 之星座，雙子為水星 Ruler 之星座
 */
class MixedReception(dayNightDifferentiatorImpl: DayNightDifferentiator) : Rule() {
  private val utils: EssentialUtils = EssentialUtils(dayNightDifferentiatorImpl)

  public override fun getResult(planet: Planet, h: Horoscope): Optional<Tuple2<String, Array<Any>>> {
    return getResult2(planet , h).toOld()
  }

  override fun getResult2(planet: Planet, h: Horoscope): Pair<String, Array<Any>>? {
    utils.setEssentialImpl(essentialImpl)
    return rulerExaltMutualReception(h , planet)?: exaltRulerMutualReception(h , planet)
  }

  /**
   * 旺廟互容
   * RULER / EXALTATION 互容
   * planet 在此 horoscope 中，座落到 sign1 星座
   * 而 sign1 星座的 RULER           星，飛到 sign2 星座
   * 而 sign2 星座的 EXALTATION (planet2) 剛好等於 planet
   */
  private fun rulerExaltMutualReception(h: Horoscope, planet: Planet): Pair<String, Array<Any>>? {
    return h.getZodiacSign(planet)?.let { sign1 ->
      essentialImpl.getPoint(sign1 , Dignity.RULER)?.let { signRuler ->
        h.getZodiacSign(signRuler)?.let { sign2 ->
          essentialImpl.getPoint(sign2 , Dignity.EXALTATION)?.let { planet2 ->
            if (planet === planet2 && !utils.isBothInBadSituation(planet , sign1 , signRuler , sign2)) {
              logger.debug("{} 位於 {} , 與其 {} {} 飛至 {} , 形成 旺廟互容", planet, sign1, Dignity.RULER, signRuler, sign2)
              "commentRuler" to arrayOf(planet, sign1, signRuler, sign2)
            } else
              null
          }
        }
      }
    }
  }


  /**
   * 廟旺互容
   * EXALTATION / RULER 互容
   * planet 在此 horoscope 中，座落到 sign1 星座
   * 而 sign1 星座的 EXALTATION   星，飛到 sign2 星座
   * 而 sign2 星座的 RULER (planet2) 剛好等於 planet
   */
  private fun exaltRulerMutualReception(h: Horoscope, planet: Planet): Pair<String, Array<Any>>? {
    return h.getZodiacSign(planet)?.let { sign1 ->
      essentialImpl.getPoint(sign1 , Dignity.EXALTATION)?.let { signExalt ->
        h.getZodiacSign(signExalt)?.let { sign2 ->
          essentialImpl.getPoint(sign2 , Dignity.RULER)?.let { planet2 ->
            if (planet === planet2) {
              //已確定互容，要排除互陷
              //只要兩顆星都不是陷落，就算互容。其中一顆星陷落無妨
              if (!utils.isBothInBadSituation(planet , sign1 , signExalt , sign2)) {
                logger.debug("{} 位於 {} , 與其 {} {} 飛至 {} , 形成 廟旺互容", planet, sign1, Dignity.EXALTATION, signExalt, sign2)
                return@let "commentExaltation" to arrayOf(planet, sign1, signExalt, sign2)
              }
            }
            return@let null
          }
        }
      }
    }
  }

}
