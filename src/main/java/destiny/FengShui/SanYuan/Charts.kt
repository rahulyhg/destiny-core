/**
 * Created by smallufo on 2018-02-28.
 */
package destiny.fengshui.sanyuan

import destiny.iching.Symbol
import java.io.Serializable


interface IPeriod {
  // 元運 , 其值只能為 1~9
  val period: Int
}

interface IChartMnt : IPeriod {
  // 座山
  val mnt: Mountain
  // 是否用替
  val replacement: Boolean
  /** 9個 [ChartBlock] */
  val blocks : List<ChartBlock>
}

interface IChartDegree : IChartMnt {
  // 詳細的度數 ,  可用來排兼向替卦
  val degree: Double
}

enum class Position {
  B,   // 底
  LB,  // 左下
  L, // 左
  LU,  // 左上
  U, // 上
  RU, // 右上
  R, // 右
  RB, // 右下
  C; // 中間

  /** 順時針 */
  fun clockWise(): Position? {
    return when (this) {
      C -> null
      B -> LB
      LB -> L
      L -> LU
      LU -> U
      U -> RU
      RU -> R
      R -> RB
      RB -> B
    }
  }
}

/** 具備描述九宮格的資料 , 九宮格內，每個 block 存放哪個 [ChartBlock] */
interface IChartPresenter {
  val posMap: Map<Position, Symbol?>
}

interface IChartMntPresenter : IChartMnt, IChartPresenter {
  fun getChartBlock(position: Position) : ChartBlock {
    val symbol:Symbol? = posMap[position]
    return blocks.first { it.symbol === symbol }
  }
}

/** 元運 + 何山（何向）+ 是否用替 */
data class ChartMnt(override val period: Int,
                    override val mnt: Mountain,
                    override val replacement: Boolean,
                    override val blocks: List<ChartBlock>) : IChartMnt, Serializable

/** 元運 + 座山的度數 （可推導出 座山)  */
data class ChartDegree(override val period: Int,
                       override val degree: Double,
                       override val replacement: Boolean,
                       override val blocks: List<ChartBlock>) : IChartDegree, Serializable {

  override val mnt: Mountain
    get() = EarthlyCompass().getMnt(degree)
}


data class ChartMntPresenter(override val period: Int,
                             override val mnt: Mountain,
                             val view: Symbol,
                             override val replacement: Boolean,
                             override val blocks: List<ChartBlock>,
                             override val posMap: Map<Position, Symbol?>) : Serializable , IChartMntPresenter

