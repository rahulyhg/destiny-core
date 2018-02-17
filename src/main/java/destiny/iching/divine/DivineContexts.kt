/**
 * Created by smallufo on 2018-02-17.
 */
package destiny.iching.divine

import destiny.core.chinese.SimpleBranch
import destiny.iching.Hexagram
import destiny.iching.HexagramName
import destiny.iching.IHexagram
import destiny.iching.contentProviders.IHexagramNameFull
import destiny.iching.contentProviders.IHexagramNameShort
import java.util.*


interface IResult<out T : ICombined> {
  fun getResult(): T
}

open class CombinedContext(val src: IHexagram, val dst: IHexagram) : IResult<ICombined> {
  override fun getResult(): ICombined {
    val s = Hexagram.getHexagram(src)
    val d = Hexagram.getHexagram(dst)
    return Combined(s, d)
  }
}

class CoreWithNameContext(val src: IHexagram,
                          val dst: IHexagram,
                          val locale: Locale = Locale.TAIWAN,
                          private val nameFullImpl: IHexagramNameFull,
                          private val nameShortImpl: IHexagramNameShort) : IResult<CombinedWithNames> {
  override fun getResult(): CombinedWithNames {
    val core: Combined = CombinedContext(src, dst).getResult() as Combined
    val coreNames =
      CombinedNames(HexagramName(nameShortImpl.getNameShort(src, locale), nameFullImpl.getNameFull(src, locale)),
                    HexagramName(nameShortImpl.getNameShort(dst, locale), nameFullImpl.getNameFull(dst, locale)))
    return CombinedWithNames(core, coreNames)
  }
}

class CombinedWithMetaContext(val src: IHexagram,
                              val dst: IHexagram,
                              val locale: Locale = Locale.TAIWAN,
                              val 納甲系統: ISettingsOfStemBranch = SettingsGingFang(),
                              val 伏神系統: IHiddenEnergy = HiddenEnergyWangImpl()) : IResult<ICombinedWithMeta> {
  override fun getResult(): ICombinedWithMeta {
    val meta = Meta(納甲系統.getTitle(locale), 伏神系統.getTitle(locale))

    val srcPlate = Divines.getSinglePlate(src, 納甲系統, 伏神系統)
    val dstPlate = Divines.getSinglePlate(dst, 納甲系統, 伏神系統)

    val 變卦對於本卦的六親: List<Relative> =
      (0..5).map { Divines.getRelative(SimpleBranch.getFiveElement( dstPlate.納甲[it].branch), srcPlate.symbol.fiveElement) }.toList()

    return CombinedWithMeta(srcPlate, dstPlate, 變卦對於本卦的六親 , meta)
  }

}

