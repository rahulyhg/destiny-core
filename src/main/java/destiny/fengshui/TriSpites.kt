/**
 * Created by smallufo on 2018-03-05.
 */
package destiny.fengshui

import destiny.core.chinese.Branch
import destiny.core.chinese.SimpleBranch
import destiny.core.chinese.StemBranch
import destiny.iching.Hexagram
import destiny.iching.Symbol
import destiny.iching.SymbolPositions
import destiny.iching.divine.*

/** spite : 惡意 、 刁難 */
enum class SpiteType {
  正,
  地, // 坐山的後天八曜煞. 後天的乾位，為先天的艮卦 . 艮的曜煞在寅. ==> 寅為乾卦的地曜煞。
  天  // 坐山先天的八曜煞. 先天的乾位，為後天的離卦 . 離的曜煞在亥. ==> 亥為乾卦的天曜煞。
}

/** 八曜煞 , 或稱「三曜煞」 */
object TriSpites {

  /** 正 曜煞 */
  private fun getStandardSpites(symbol: Symbol, settings: ISettingsOfStemBranch): List<Branch> {
    val hexagram = Hexagram.Companion.getHexagram(symbol, symbol)
    return (1..6).filter {
      val sb: StemBranch = settings.getStemBranch(hexagram, it)
      val 外在五行 = SimpleBranch.getFiveElement(sb.branch)
      DivineContext.getRelative(外在五行, symbol.fiveElement) === Relative.官鬼
    }.map { settings.getStemBranch(hexagram, it).branch }
  }

  fun getSpites(symbol: Symbol, type: SpiteType, settings: ISettingsOfStemBranch): List<Branch> {
    return when (type) {
      SpiteType.正 -> getStandardSpites(symbol, settings)
      SpiteType.地 -> getStandardSpites(SymbolPositions.acquiredToCongenital(symbol), settings)
      SpiteType.天 -> getStandardSpites(SymbolPositions.congenitalToAcquired(symbol), settings)
    }
  }

  fun getSpite(symbol: Symbol,
               type: SpiteType? = SpiteType.正,
               settings: ISettingsOfStemBranch? = SettingsGingFang()): Branch {
    return getSpites(symbol, type?:SpiteType.正, settings ?: SettingsGingFang()).first()
  }

}