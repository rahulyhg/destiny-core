/**
 * @author smallufo
 * Created on 2008/1/27 at 上午 2:19:44
 */
package destiny.core.calendar.eightwords

import destiny.core.Descriptive
import destiny.tools.ILocaleString
import java.util.*

/** 界定南北半球的方法 , 赤道 還是 赤緯  */
enum class HemisphereBy(private val nameKey: String) : ILocaleString , Descriptive {
  /** 赤道  */
  EQUATOR("HemisphereBy.EQUATOR"),
  /** 赤緯  */
  DECLINATION("HemisphereBy.DECLINATION");

  override fun toString(): String {
    return ResourceBundle.getBundle(resource, Locale.getDefault()).getString(nameKey)
  }

  override fun toString(locale: Locale): String {
    return ResourceBundle.getBundle(resource, locale).getString(nameKey)
  }

  override fun getTitle(locale: Locale): String {
    return toString(locale)
  }

  override fun getDescription(locale: Locale): String {
    return getTitle(locale)
  }

  companion object {

    private val resource = EightWords::class.java.name
  }
}
