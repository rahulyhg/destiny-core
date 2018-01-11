/** 2009/7/13 上午 3:40:47 by smallufo  */
package destiny.iching.contentProviders

import destiny.iching.IHexagram
import java.util.*

/** 取得全名，例如「乾為天」  */
interface IHexagramNameFull {
  /** 取得全名，例如「乾為天」  */
  fun getNameFull(hexagram: IHexagram, locale: Locale): String
}

