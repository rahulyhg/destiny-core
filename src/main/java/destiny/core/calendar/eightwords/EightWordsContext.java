package destiny.core.calendar.eightwords;

import destiny.astrology.*;
import destiny.core.calendar.Location;
import destiny.core.calendar.SolarTerms;
import destiny.core.calendar.SolarTermsImpl;
import destiny.core.calendar.Time;
import destiny.core.calendar.chinese.ChineseDate;
import destiny.core.calendar.chinese.ChineseDateIF;
import destiny.core.chinese.Branch;
import destiny.core.chinese.Stem;
import destiny.core.chinese.StemBranch;
import destiny.core.chinese.StemBranchUtils;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 除了計算八字，另外新增輸出農曆以及命宮的方法
 */
public class EightWordsContext implements Serializable {

  protected final EightWords eightWords;

  protected final LocalDateTime lmt;

  protected final Location location;

  protected final EightWordsIF eightWordsImpl;

  protected final YearMonthIF yearMonthImpl;

  protected final DayIF dayImpl;

  protected final HourIF hourImpl;

  protected final MidnightIF midnightImpl;

  protected final boolean changeDayAfterZi;

  protected final ChineseDateIF chineseDateImpl;      // 農曆計算

  protected final RisingSignIF risingSignImpl;        // 命宮 (上升星座)

  protected final StarPositionIF starPositionImpl;    // 星體位置

  public EightWordsContext(LocalDateTime lmt, Location location, EightWordsIF eightWordsImpl, YearMonthIF yearMonthImpl, ChineseDateIF chineseDateImpl, DayIF dayImpl, HourIF hourImpl, MidnightIF midnightImpl, boolean changeDayAfterZi, RisingSignIF risingSignImpl, StarPositionIF starPositionImpl) {
    this.yearMonthImpl = yearMonthImpl;
    this.eightWords = eightWordsImpl.getEightWords(lmt , location);
    this.lmt = lmt;
    this.location = location;
    this.eightWordsImpl = eightWordsImpl;
    this.chineseDateImpl = chineseDateImpl;
    this.dayImpl = dayImpl;
    this.hourImpl = hourImpl;
    this.midnightImpl = midnightImpl;
    this.changeDayAfterZi = changeDayAfterZi;
    this.starPositionImpl = starPositionImpl;
    this.risingSignImpl = risingSignImpl;
  }

  public EightWordsContextModel getModel(LocalDateTime lmt, Location location) {
    //EightWords eightWords = eightWordsImpl.getEightWords(lmt , location);
    int gmtMinuteOffset = Time.getDstSecondOffset(lmt, location).v2() / 60;
    boolean dst = Time.getDstSecondOffset(lmt, location).v1();

    Tuple2<SolarTerms , SolarTerms> prevNextMajorSolarTerms = getPrevNextMajorSolarTerms(lmt , location);

    ChineseDate chineseDate = getChineseDate(lmt , location);

    StemBranch risingSign = getRisingStemBranch(lmt ,location);
    Branch sunBranch = getBranchOf(Planet.SUN , lmt , location);
    Branch moonBranch = getBranchOf(Planet.MOON , lmt , location);
    return new EightWordsContextModel(eightWords , lmt , location , "LOCATION", gmtMinuteOffset , dst , chineseDate, prevNextMajorSolarTerms.v1() ,
      prevNextMajorSolarTerms.v2() ,
      risingSign ,
      sunBranch , moonBranch);
  }

  public EightWords getEightWords() {
    return eightWords;
  }

  /**
   * 節氣
   * TODO 演算法重複 {@link SolarTermsImpl#getSolarTermsFromGMT}
   */
  public SolarTerms getCurrentSolarTerms(LocalDateTime lmt , Location location) {
    LocalDateTime gmt = Time.getGmtFromLmt(lmt , location);
    double gmtJulDay = Time.getGmtJulDay(gmt);
    Position sp = starPositionImpl.getPosition(Planet.SUN, gmtJulDay, Centric.GEO, Coordinate.ECLIPTIC);
    return SolarTerms.getFromDegree(sp.getLongitude());
  }


  /** 上一個「節」、下一個「節」
   * */
  public Tuple2<SolarTerms , SolarTerms> getPrevNextMajorSolarTerms(LocalDateTime lmt , Location location) {
    SolarTerms currentSolarTerms = getCurrentSolarTerms(lmt , location);
    int currentSolarTermsIndex = SolarTerms.getIndex(currentSolarTerms);
    SolarTerms prevMajorSolarTerms;
    SolarTerms nextMajorSolarTerms;
    if (currentSolarTermsIndex % 2 == 0)  //立春 , 驚蟄 , 清明 ...
    {
      prevMajorSolarTerms = currentSolarTerms;
      nextMajorSolarTerms = currentSolarTerms.next().next();
    }
    else
    {
      prevMajorSolarTerms = currentSolarTerms.previous();
      nextMajorSolarTerms = currentSolarTerms.next();
    }
    return Tuple.tuple(prevMajorSolarTerms , nextMajorSolarTerms);
  }

  /** 取得農曆 */
  public ChineseDate getChineseDate(LocalDateTime lmt, Location location) {
    return chineseDateImpl.getChineseDate(lmt, location, dayImpl, hourImpl, midnightImpl, changeDayAfterZi);
  }

  /** 承上 , time 版本 */
  public ChineseDate getChineseDate(Time lmt, Location location) {
    return getChineseDate(lmt.toLocalDateTime() , location);
  }

  public YearMonthIF getYearMonthImpl() {
    return yearMonthImpl;
  }

  public HourIF getHourImpl() {
    return hourImpl;
  }

  public MidnightIF getMidnightImpl() {
    return midnightImpl;
  }

  /**
   * 取得命宮的計算方法
   */
  public RisingSignIF getRisingSignImpl() {
    return risingSignImpl;
  }

  /** 取得陰陽曆轉換的實作 */
  public ChineseDateIF getChineseDateImpl() {
    return chineseDateImpl;
  }

  /**
   * 計算命宮干支
   */
  public StemBranch getRisingStemBranch(LocalDateTime lmt, Location location) {

    EightWords ew = eightWordsImpl.getEightWords(lmt , location);
    // 命宮地支
    Branch risingBranch = risingSignImpl.getRisingSign(lmt, location , HouseSystem.PLACIDUS , Coordinate.ECLIPTIC).getBranch();
    // 命宮天干：利用「五虎遁」起月 => 年干 + 命宮地支（當作月份），算出命宮的天干
    Stem risingStem = StemBranchUtils.getMonthStem(ew.getYearStem(), risingBranch);
    // 組合成干支
    return StemBranch.get(risingStem, risingBranch);
  }

  public Branch getBranchOf(Star star , LocalDateTime lmt , Location location) {
    Position pos = starPositionImpl.getPosition(star , lmt , location , Centric.GEO ,Coordinate.ECLIPTIC);
    return ZodiacSign.getZodiacSign(pos.getLongitude()).getBranch();
  }

  public boolean isChangeDayAfterZi() {
    return changeDayAfterZi;
  }
}
