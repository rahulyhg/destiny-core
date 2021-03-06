/**
 * Created by smallufo on 2017-04-13.
 */
package destiny.core.chinese.ziwei

import destiny.core.DayNight
import destiny.core.Gender
import destiny.core.calendar.ILocation
import destiny.core.calendar.chinese.ChineseDate
import destiny.core.chinese.Branch
import destiny.core.chinese.FiveElement
import destiny.core.chinese.StemBranch
import java.io.Serializable
import java.time.chrono.ChronoLocalDateTime
import java.util.*

interface IPlate {
  /** 名稱  */
  val name: String?

  /** 出生資料 , 陰曆  */
  val chineseDate: ChineseDate

  /** 出生資料 , 陽曆 , 精確到「分、秒」  */
  val localDateTime: ChronoLocalDateTime<*>?

  /** 出生年的干支 (可能是節氣、也可能是陰曆) */
  val year : StemBranch

  /** 出生地點  */
  val location: ILocation?

  /** 地點名稱  */
  val place: String?

  /** 日、夜？ */
  val dayNight : DayNight

  /** 性別  */
  val gender: Gender

  /** 命宮  */
  val mainHouse: StemBranch

  /** 身宮  */
  val bodyHouse: StemBranch

  /** 命主  */
  val mainStar: ZStar

  /** 身主  */
  val bodyStar: ZStar

  /** 五行  */
  val fiveElement: FiveElement

  /** 五行第幾局  */
  val state: Int

  /** 12個宮位，每個宮位內的資料  */
  val houseDataSet: Set<HouseData>

  /**
   * 四化星 的列表
   * 存放著「這顆星」在 [本命、大限、流年、...] 的四化 結果為何
   */
  val tranFours: Map<ZStar, Map<FlowType, ITransFour.Value>>

  /** 取得此地支，在各個流運類型， 宮位名稱 是什麼  */
  val branchFlowHouseMap: Map<Branch, Map<FlowType, House>>

  /** 取得此命盤，包含哪些流運資訊  */
  val flowBranchMap: Map<FlowType, StemBranch>

  /** 星體強弱表  */
  val starStrengthMap: Map<ZStar, Int>

  /** 註解列表  */
  val notes: List<String>

  /** 虛歲，每歲的起訖時分 (fromGmt , toGmt)  */
  val vageMap: Map<Int, Pair<Double, Double>>?


  // =========== 以上 ↑↑ fields for overridden ↑↑ ===========

  /** 宮位名稱 -> 宮位資料  */
  val houseMap: Map<House, HouseData>
    get() = houseDataSet.toList().map { hd -> hd.house to hd }.toMap()

  /** 星體 -> 宮位資料  */
  val starMap: Map<ZStar, HouseData>
    get() = houseDataSet
      .flatMap { hd -> hd.stars.map { star -> star to hd } }
      .toMap()

  /** 宮位地支 -> 星體s  */
  val branchStarMap: Map<Branch, Collection<ZStar>>
    get() = houseDataSet.groupBy { it.stemBranch.branch }.mapValues { it.value.flatMap { it.stars } }

  /** 宮位名稱 -> 星體s  */
  val houseStarMap: Map<House, Set<ZStar>>
    get() = houseDataSet.map { it -> it.house to it.stars }.toMap()

  /** 本命盤中，此地支的宮位名稱是什麼  */
  val branchHouseMap: Map<Branch, House>
    get() = branchFlowHouseMap.map { it -> it.key to it.value[FlowType.本命]!! }.toMap()


  // =========== 以上 ↑↑ functions ↑↑ ===========

  /** 取得這些星體所在宮位的地支 */
  fun getBranches(vararg stars: ZStar) : List<Branch> {
    return stars.map { star -> starMap[star]?.stemBranch?.branch }
      .filter { b -> b!= null }
      .map { b -> b!! }
  }

  /** 取得每個宮位、詳細資料 , 按照 [命宮 , 兄弟 , 夫妻...] 排序下來  */
  fun getSortedHouseDataSet(): Set<HouseData> {
    return TreeSet(houseDataSet)
  }

  /** 取得此地支的宮位資訊  */
  fun getHouseDataOf(branch: Branch): HouseData {
    return houseDataSet.first { houseData -> houseData.stemBranch.branch == branch }
  }

  fun getHouseDataOf(house: House): HouseData? {
    return houseDataSet.firstOrNull { it.house == house }
  }

  /** 這顆星在哪個宮位 */
  fun getHouseDataOf(star: ZStar): HouseData? {
    return houseDataSet.firstOrNull { it.stars.contains(star) }
  }

  /** 取得此顆星，的四化列表 */
  fun getTransFourOf(star: ZStar): List<Pair<FlowType, ITransFour.Value>> {
    return tranFours[star]?.map { (key, value) -> key to value }?.toList() ?: emptyList()
  }

  /** 取得此星，的四化值 (maybe null) */
  fun getTransFourValue(star: ZStar , type: FlowType = FlowType.本命): ITransFour.Value? {
    return tranFours[star]?.let { m -> m[type] }
  }

  /**
   * 取得此四化星，在哪一宮位
   * */
  fun getTransFourHouseOf(value : ITransFour.Value , type: FlowType = FlowType.本命) : HouseData {
    val star = tranFours.entries.first { (_, map) ->
      map.any { (t,v) -> t == type && v == value }
    }.key
    return getHouseDataOf(star)!!
  }

  /** 取得在此地支宮位的主星 */
  fun getMainStarsIn(branch: Branch): List<ZStar> {
    return houseDataSet.filter { it.stemBranch.branch == branch }
      .flatMap { it.stars }
      .filter { it is StarMain }
  }

  /** 吉星 */
  fun getLuckyStarsIn(branch: Branch): List<ZStar> {
    return houseDataSet.filter { it.stemBranch.branch == branch }
      .flatMap { it.stars }
      .filter { it is StarLucky }
  }

  /** 凶星 */
  fun getUnluckyStarsIn(branch: Branch): List<ZStar> {
    return houseDataSet.filter { it.stemBranch.branch == branch }
      .flatMap { it.stars }
      .filter { it is StarUnlucky }
  }

  /** 雜曜 */
  fun getMinorStarsIn(branch: Branch): List<ZStar> {
    return houseDataSet.filter { it.stemBranch.branch == branch }
      .flatMap { it.stars }
      .filter { it is StarMinor }
  }

  /** 博士12神煞 */
  fun getDoctorStarIn(branch: Branch): ZStar? {
    return houseDataSet.filter { it.stemBranch.branch == branch }
      .flatMap { it.stars }
      .firstOrNull { it is StarDoctor }
  }

  /** 長生12神煞 */
  fun getLongevityStarIn(branch: Branch): ZStar? {
    return houseDataSet
      .filter { it.stemBranch.branch == branch }
      .flatMap { it.stars }
      .firstOrNull { it is StarLongevity }
  }

  /** 將前 12星 */
  fun getGeneralFrontStarIn(branch: Branch): ZStar? {
    return houseDataSet.filter { it.stemBranch.branch == branch }
      .flatMap { it.stars }
      .firstOrNull { it is StarGeneralFront }
  }

  /** 歲前 12星 */
  fun getYearFrontStarIn(branch: Branch): ZStar? {
    return houseDataSet.filter { it.stemBranch.branch == branch }
      .flatMap { it.stars }
      .firstOrNull { it is StarYearFront }
  }
}

/** 排盤結果 , 作為 DTO  */
data class Plate(
  /** 名稱  */
  override val name: String?,

  /** 出生資料 , 陰曆  */
  override val chineseDate: ChineseDate,

  /** 出生資料 , 陽曆 , 精確到「分、秒」  */
  override val localDateTime: ChronoLocalDateTime<*>?,

  /** 出生年的干支 (可能是節氣、也可能是陰曆) */
  override val year : StemBranch,

  /** 出生地點  */
  override val location: ILocation?,

  /** 地點名稱  */
  override val place: String?,

  /** 日、夜？ */
  override val dayNight: DayNight,

  /** 性別  */
  override val gender: Gender,

  /** 命宮  */
  override val mainHouse: StemBranch,

  /** 身宮  */
  override val bodyHouse: StemBranch,

  /** 命主  */
  override val mainStar: ZStar,

  /** 身主  */
  override val bodyStar: ZStar,

  /** 五行  */
  override val fiveElement: FiveElement,

  /** 五行第幾局  */
  override val state: Int,

  /** 12個宮位，每個宮位內的資料  */
  override val houseDataSet: Set<HouseData>,

  /**
   * 四化星 的列表
   * 存放著「這顆星」在 [本命、大限、流年、...] 的四化 結果為何
   */
  override val tranFours: Map<ZStar, Map<FlowType, ITransFour.Value>>,

  /** 取得此地支，在各個流運類型， 宮位名稱 是什麼  */
  override val branchFlowHouseMap: Map<Branch, Map<FlowType, House>>,

  /** 取得此命盤，包含哪些流運資訊  */
  override val flowBranchMap: Map<FlowType, StemBranch>,

  /** 星體強弱表  */
  override val starStrengthMap: Map<ZStar, Int>,

  /** 註解列表  */
  override val notes: List<String>,

  /** 虛歲，每歲的起訖時分 (fromGmt , toGmt)  */
  override val vageMap: Map<Int, Pair<Double, Double>>?) : IPlate, Serializable
