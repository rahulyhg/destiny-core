package destiny.core.chinese

import destiny.core.chinese.FiveElement.*
import destiny.tools.ArrayTools
import java.util.*

/** 天干系統  */
enum class Stem : Comparable<Stem>, IFiveElement, IYinYang {

  甲, 乙, 丙, 丁, 戊,
  己, 庚, 辛, 壬, 癸;


  /** 甲[0] ... 癸[9]  */
  val index: Int
    get() = getIndex(this)

  /** 甲[1] ... 癸[10]  */
  val indexFromOne: Int
    get() = getIndex(this) + 1


  /**
   * 取得下 n 個天干為何
   * n = 0 : 傳回自己
   */
  fun next(n: Int): Stem {
    return get(getIndex(this) + n)
  }

  /**
   * 取得前 n 個天干為何
   * n = 0 : 傳回自己
   */
  fun prev(n: Int): Stem {
    return next(0 - n)
  }

  /**
   * 取得此天干，領先另一個天干，多少距離. 其值一定為正值
   * 甲 領先 甲 0 步
   * 甲 領先 乙 9 步
   * ...
   * 甲 領先 癸 1 步
   */
  fun getAheadOf(other: Stem): Int {
    val steps = index - other.index
    return if (steps >= 0) steps else steps + 10
  }

  /**
   * 實作 FiveElementsIF 的 getFiveElements()
   */
  override fun getFiveElement(): FiveElement {
    return when (getIndex(this)) {
      0, 1 -> 木
      2, 3 -> 火
      4, 5 -> 土
      6, 7 -> 金
      8, 9 -> 水
      else -> throw AssertionError("HeavenlyStems Error : cannot getFiveElements() : " + toString())
    }
  } //getFiveElements()

  override fun getBooleanValue(): Boolean {
    return getIndex(this) % 2 == 0
  }

  companion object {

    private val ARRAY = arrayOf(甲, 乙, 丙, 丁, 戊, 己, 庚, 辛, 壬, 癸)

    /** 從五行 以及 陰陽 建立天干  */
    operator fun get(fiveElement: FiveElement, yinYang: Boolean): Stem {
      return when (fiveElement) {
        木 -> if (yinYang) 甲 else 乙
        火 -> if (yinYang) 丙 else 丁
        土 -> if (yinYang) 戊 else 己
        金 -> if (yinYang) 庚 else 辛
        水 -> if (yinYang) 壬 else 癸
      }
    }

    /**
     * 抓取天干的 index , 為 0-based <BR></BR>
     * 0 為 甲
     * 1 為 乙
     * ...
     * 9 為 癸
     * @param index
     * @return
     */
    operator fun get(index: Int): Stem {
      return ArrayTools[ARRAY, index]
    }

    operator fun get(c: Char): Stem? {
      return ARRAY.firstOrNull { it -> it.name == c.toString() }
    }

    /** 甲[0] ... 癸[9]  */
    fun getIndex(hs: Stem): Int {
      return Arrays.binarySearch(ARRAY, hs)
    }
  }

}