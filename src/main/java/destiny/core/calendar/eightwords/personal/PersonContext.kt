/**
 * Created by smallufo on 2015-04-21.
 */
package destiny.core.calendar.eightwords.personal

import com.google.common.cache.CacheBuilder
import destiny.astrology.*
import destiny.astrology.Coordinate.ECLIPTIC
import destiny.astrology.Planet.SUN
import destiny.core.Gender
import destiny.core.IIntAge
import destiny.core.IntAgeNote
import destiny.core.calendar.ILocation
import destiny.core.calendar.SolarTerms
import destiny.core.calendar.TimeTools
import destiny.core.calendar.eightwords.EightWordsContext
import destiny.core.calendar.eightwords.IEightWordsContextModel
import destiny.core.chinese.Branch
import destiny.core.chinese.StemBranch
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDateTime
import java.util.concurrent.TimeUnit

/** to be replaced by [PersonContext2] */
@Deprecated("PersonContext2")
class PersonContext(
  val ewContext: EightWordsContext,
  /** 星體運行到某點的介面  */
  private val starTransitImpl: IStarTransit,
  /** 歲數實作  */
  private val intAgeImpl: IIntAge,
  /** 出生時刻  */
  val lmt: ChronoLocalDateTime<*>,
  /** 出生地點  */
  val location: ILocation,
  val place: String?,
  /** 性別  */
  val gender: Gender,
  /** 運 :「月」的 span 倍數，內定 120，即：一個月干支 擴展(乘以)120 倍，變成十年  */
  private val fortuneMonthSpan: Double,
  /** 大運的順逆，內定採用『陽男陰女順排；陰男陽女逆排』的演算法  */
  private val fortuneDirectionImpl: IFortuneDirection,
  /** 歲數註解實作  */
  val ageNoteImpls: List<IntAgeNote>) {

  private val logger = LoggerFactory.getLogger(javaClass)

  /** 運：「日」的 span 倍數，內定 365，即：一日走一年  */
  private val fortuneDaySpan = 365.0

  /** 運 :「時辰」的 span 倍數，內定 365x12，即：一時辰走一年  */
  /** 運 : 時干支的倍數  */
  private val fortuneHourSpan = (365 * 12).toDouble()

  /** 大運輸出格式  */
  //private final FortuneOutput fortuneOutput;

  private val cache = CacheBuilder.newBuilder()
    .maximumSize(100)
    .expireAfterAccess(10, TimeUnit.MINUTES)
    .build< Pair<Double , Gender>, MutableMap<Int, Double>>()

  val eightWordsModel : IEightWordsContextModel by lazy {
    ewContext.getEightWordsContextModel(lmt, location, place)
  }

  private val gmtJulDay : Double by lazy {
    TimeTools.getGmtJulDay(lmt , location)
  }


  val model: IPersonContextModel
    get() {
      return PersonContextModel(eightWordsModel, gender , getFortuneDatas(9) , getAgeMap(90))
    }

  /** 八字大運是否順行  */
  val isFortuneDirectionForward: Boolean
    get() = fortuneDirectionImpl.isForward(gender , eightWordsModel.eightWords)


  private fun getAgeMap(toAge: Int): Map<Int, Pair<Double, Double>> {
    val gmtJulDay = TimeTools.getGmtJulDay(lmt, location)

    return intAgeImpl.getRangesMap(gender, gmtJulDay, location, 1, toAge)
  }

  /**
   * 距離下 N 個「節」有幾秒 , 如果 index 為負，代表計算之前的「節」。 index 不能等於 0
   *
   * @return **如果 index 為正，則傳回正值; 如果 index 為負，則傳回負值**
   */
  private fun getTargetMajorSolarTermsSeconds(gmtJulDay : Double , index: Int): Double {
    if (index == 0)
      throw RuntimeException("index cannot be 0 !")

    var reverse = false
    if (index < 0)
      reverse = true

    val gmt: ChronoLocalDateTime<*> = TimeTools.getGmtFromLmt(lmt, location)
    //double stepGmt = TimeTools.getGmtJulDay(gmt);
    var stepGmtJulDay: Double = gmtJulDay
    //現在的 節氣
    var currentSolarTerms = ewContext.solarTermsImpl.getSolarTermsFromGMT(gmt)
    var stepMajorSolarTerms = SolarTerms.getNextMajorSolarTerms(currentSolarTerms, reverse)

    var i: Int
    i = if (!reverse)
      1
    else
      -1

    var hashMap: MutableMap<Int, Double>? = cache.getIfPresent(Pair(gmtJulDay , gender))


    if (hashMap == null) {
      hashMap = LinkedHashMap()
      cache.put(Pair(gmtJulDay , gender), hashMap)
    }

    var targetGmtJulDay: Double? = null
    if (hashMap.containsKey(index))
      targetGmtJulDay = hashMap[index]

    if (targetGmtJulDay == null) {
      if (!reverse) {
        //順推
        if (hashMap[index - 1] != null)
          i = index - 1

        while (i <= index) {
          // 推算到上一個/下一個「節」的秒數：陽男陰女順推，陰男陽女逆推
          if (hashMap[i] == null) {
            logger.debug("順推 cache.get({}) miss", i)
            //沒有計算過
            targetGmtJulDay = this.starTransitImpl.getNextTransitGmt(SUN, stepMajorSolarTerms.zodiacDegree.toDouble(), ECLIPTIC, stepGmtJulDay, true)
            //以隔天計算現在節氣
            stepGmtJulDay = targetGmtJulDay + 1  //LocalDateTime.from(targetGmt).plusSeconds(24 * 60 * 60);


            hashMap[i] = targetGmtJulDay
            cache.put(Pair(gmtJulDay , gender), hashMap)
          } else {
            //之前計算過
            logger.debug("順推 cache.get({}) hit", i)
            targetGmtJulDay = hashMap[i]
            stepGmtJulDay = targetGmtJulDay!! + 1// LocalDateTime.from(targetGmt).plusSeconds(24 * 60 * 60);
          }

          currentSolarTerms = ewContext.solarTermsImpl.getSolarTermsFromGMT(stepGmtJulDay)
          stepMajorSolarTerms = SolarTerms.getNextMajorSolarTerms(currentSolarTerms, false)
          i++
        } // while (i <= index)
      } //順推
      else {
        //逆推
        if (hashMap[index + 1] != null)
          i = index + 1

        while (i >= index) {
          // 推算到上一個/下一個「節」的秒數：陽男陰女順推，陰男陽女逆推

          if (hashMap[i] == null) {
            logger.debug("逆推 cache.get({}) miss", i)
            //沒有計算過

            targetGmtJulDay = this.starTransitImpl.getNextTransitGmt(SUN, stepMajorSolarTerms.zodiacDegree.toDouble(), ECLIPTIC, stepGmtJulDay, false)
            //以前一天計算現在節氣
            stepGmtJulDay = targetGmtJulDay - 1 // LocalDateTime.from(targetGmt).minusSeconds(24 * 60 * 60);
            hashMap[i] = targetGmtJulDay
            cache.put(Pair(gmtJulDay , gender), hashMap)
          } else {
            //之前計算過
            logger.debug("逆推 cache.get({}) hit", i)
            targetGmtJulDay = hashMap[i]
            stepGmtJulDay = targetGmtJulDay!! - 1 //LocalDateTime.from(targetGmt).minusSeconds(24 * 60 * 60);
          }

          currentSolarTerms = ewContext.solarTermsImpl.getSolarTermsFromGMT(stepGmtJulDay)
          stepMajorSolarTerms = SolarTerms.getNextMajorSolarTerms(currentSolarTerms, true)
          i--
        } //while (i >= index)
      } //逆推
    }

    assert(targetGmtJulDay != null)

    // 同義於 Duration.between(gmt , targetGmtJulDay)
    val durDays = targetGmtJulDay!! - TimeTools.getGmtJulDay(gmt)
    logger.trace("durDays = {} ", durDays)
    return durDays * 86400

  } // getTargetMajorSolarTermsSeconds(int)



  /**
   * @return 在此 gmtJulDay 時刻，座落於歲數的哪一歲當中
   * 可能歲數超出範圍之後，或是根本在出生之前，就會傳回 empty
   */
  private fun getAge(gmtJulDay: Double, ageMap: Map<Int, Pair<Double, Double>>): Int? {
    return ageMap.entries.firstOrNull { (age, pair) -> gmtJulDay > pair.first && pair.second > gmtJulDay }?.key
  }

  /**
   * @param count 計算 n柱 大運的資料
   */
  private fun getFortuneDatas(count: Int): List<FortuneData> {
    // forward : 大運是否順行
    val isForward = isFortuneDirectionForward

    //下個大運的干支
    var nextStemBranch = if (isForward) eightWordsModel.eightWords.month.next else eightWordsModel.eightWords.month.previous

    val fortuneDatas = ArrayList<FortuneData>(count)

    val gmtJulDay = gmtJulDay

    val ageMap = getAgeMap(120)

    // 計算九柱大運的相關資訊

    for (i in 1..count) {
      // 西元/民國/實歲/虛歲之值
      val startFortuneSeconds = getTargetMajorSolarTermsSeconds(gmtJulDay , i * if (isForward) 1 else -1)
      val   endFortuneSeconds = getTargetMajorSolarTermsSeconds(gmtJulDay , (i + 1) * if (isForward) 1 else -1)

      val startFortuneGmtJulDay = gmtJulDay + Math.abs(startFortuneSeconds) * fortuneMonthSpan / 86400.0
      val   endFortuneGmtJulDay = gmtJulDay + Math.abs(  endFortuneSeconds) * fortuneMonthSpan / 86400.0

      val startFortune = getAge(startFortuneGmtJulDay, ageMap) ?: 0
      val   endFortune = getAge(  endFortuneGmtJulDay, ageMap) ?: 0

      val startFortuneAgeNotes = ageNoteImpls.map { impl -> ageMap[startFortune]?.let { impl.getAgeNote(it) } }.filter { it != null }.map { it!! }.toList()
      val endFortuneAgeNotes   = ageNoteImpls.map { impl -> ageMap[  endFortune]?.let { impl.getAgeNote(it) } }.filter { it != null }.map { it!! }.toList()

      val fortuneData = FortuneData(nextStemBranch, startFortuneGmtJulDay, endFortuneGmtJulDay, startFortune, endFortune, startFortuneAgeNotes, endFortuneAgeNotes)
      fortuneDatas.add(fortuneData)

      nextStemBranch = if (isForward) nextStemBranch.next else nextStemBranch.previous
    } // for 1 ~ fortunes)
    return fortuneDatas
  }


  private fun getBranchOf(star: Star): Branch {
    val pos = ewContext.starPositionImpl.getPosition(star, lmt, location, Centric.GEO, Coordinate.ECLIPTIC)
    return ZodiacSign.getZodiacSign(pos.lng).branch
  }

  /**
   * 由 GMT 反推月大運
   *
   * @param targetGmt 目標時刻 (in GMT)
   * @return 月大運干支
   */
  fun getStemBranchOfFortuneMonth(targetGmt: LocalDateTime): StemBranch {
    return this.getStemBranchOfFortune(targetGmt, this.fortuneMonthSpan)
  }

  /**
   * 由 GMT 反推日大運
   *
   * @param targetGmt 目標時刻 (in GMT)
   * @return 日大運干支
   */
  fun getStemBranchOfFortuneDay(targetGmt: LocalDateTime): StemBranch {
    return this.getStemBranchOfFortune(targetGmt, this.fortuneDaySpan)
  }

  /**
   * 由 GMT 反推時大運
   *
   * @param targetGmt 目標時刻 (in GMT)
   * @return 時大運干支
   */
  fun getStemBranchOfFortuneHour(targetGmt: LocalDateTime): StemBranch {
    return this.getStemBranchOfFortune(targetGmt, this.fortuneHourSpan)
  }

  /**
   * 由 GMT 反推月大運，private method
   *
   * @param targetGmt 目標時刻（必須在出生時刻之後）
   * @param span      放大倍數
   * @return 干支
   */
  private fun getStemBranchOfFortune(targetGmt: LocalDateTime, span: Double): StemBranch {
    val gmt = TimeTools.getGmtFromLmt(lmt, location)
    var resultStemBranch =  eightWordsModel.eightWords.month

    val dur = Duration.between(targetGmt, gmt).abs()
    val diffSeconds = dur.seconds + dur.nano / 1_000_000_000.0

    if (targetGmt.isAfter(gmt) && isFortuneDirectionForward) {
      logger.debug("大運順行")
      var index = 1
      while (getTargetMajorSolarTermsSeconds(gmtJulDay , index) * span < diffSeconds) {
        resultStemBranch = resultStemBranch.next
        index++
      }
      return resultStemBranch
    }
    if (targetGmt.isAfter(gmt) && !isFortuneDirectionForward) {
      logger.debug("大運逆行")
      var index = -1
      while (Math.abs(getTargetMajorSolarTermsSeconds(gmtJulDay , index) * span) < diffSeconds) {
        resultStemBranch = resultStemBranch.previous
        index--
      }
      return resultStemBranch
    } else
      throw RuntimeException("Error while getStemBranchOfFortune($targetGmt)")
  }


  override fun toString(): String {
    return "EightWordsPersonContext [gender=$gender, lmt=$lmt, location=$location]"
  }



}
