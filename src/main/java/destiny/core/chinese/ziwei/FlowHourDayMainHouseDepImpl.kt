/**
 * Created by smallufo on 2017-04-18.
 */
package destiny.core.chinese.ziwei

import destiny.core.chinese.Branch

import java.io.Serializable

/**
 * 計算流時命宮
 *
 * 流時依據流日而來，以流日本宮為子時，順時鐘方向前進。
 */
class FlowHourDayMainHouseDepImpl : IFlowHour, Serializable {

  override fun getFlowHour(hour: Branch, flowDayMainHour: Branch): Branch {
    return flowDayMainHour.next(hour.getAheadOf(Branch.子))
  }
}
