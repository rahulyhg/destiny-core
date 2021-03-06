/**
 * @author smallufo
 * Created on 2007/5/27 at 上午 2:18:45
 */
package destiny.astrology

import destiny.tools.ILocaleString
import java.util.*

/**
 * 橢圓軌道的四個點：近點（Perihelion/Perigee）、遠點（Aphelion/Apogee），上升點（Ascending/North Node），下降點（Descending/South Node）
 */
enum class Apsis(private val nameKey: String) : ILocaleString {
  /** 近點  */
  PERIHELION("Apsis.PERIHELION"),
  /** 遠點  */
  APHELION("Apsis.APHELION"),
  /** 北交點/上升點  */
  ASCENDING("Apsis.ASCENDING"),
  /** 南交點/下降點  */
  DESCENDING("Apsis.DESCENDING");

  override fun toString(): String {
    return ResourceBundle.getBundle(resource, Locale.getDefault()).getString(nameKey)
  }

  override fun toString(locale: Locale): String {
    return ResourceBundle.getBundle(resource, locale).getString(nameKey)
  }

  companion object {

    private const val resource = "destiny.astrology.Star"
  }
}
