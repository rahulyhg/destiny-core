/**
 * Created by smallufo on 2017-04-13.
 */
package destiny.core.chinese.ziwei;

import com.google.common.collect.ImmutableSet;
import destiny.core.Gender;
import destiny.core.calendar.SolarTerms;
import destiny.core.chinese.*;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.jooq.lambda.tuple.Tuple5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static destiny.core.chinese.ziwei.StarDoctor.*;
import static destiny.core.chinese.ziwei.StarGeneralFront.*;
import static destiny.core.chinese.ziwei.StarLongevity.*;
import static destiny.core.chinese.ziwei.StarLucky.Companion;
import static destiny.core.chinese.ziwei.StarMinor.*;
import static destiny.core.chinese.ziwei.StarMinor.fun咸池;
import static destiny.core.chinese.ziwei.StarMinor.fun天德;
import static destiny.core.chinese.ziwei.StarMinor.fun華蓋;
import static destiny.core.chinese.ziwei.StarMinor.咸池;
import static destiny.core.chinese.ziwei.StarMinor.天德;
import static destiny.core.chinese.ziwei.StarMinor.華蓋;
import static destiny.core.chinese.ziwei.StarUnlucky.*;
import static destiny.core.chinese.ziwei.StarYearFront.*;

@SuppressWarnings("Duplicates")
public class HouseFunctions {

  private static Logger logger = LoggerFactory.getLogger(HouseFunctions.class);

  public final static IHouse house紫微 = new HouseMainStarImpl(StarMain.Companion.get紫微()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun紫微().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house天機 = new HouseMainStarImpl(StarMain.Companion.get天機()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun天機().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house太陽 = new HouseMainStarImpl(StarMain.Companion.get太陽()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun太陽().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house武曲 = new HouseMainStarImpl(StarMain.Companion.get武曲()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun武曲().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house天同 = new HouseMainStarImpl(StarMain.Companion.get天同()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun天同().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house廉貞 = new HouseMainStarImpl(StarMain.Companion.get廉貞()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun廉貞().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house天府 = new HouseMainStarImpl(StarMain.Companion.get天府()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun天府().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };


  public final static IHouse house太陰 = new HouseMainStarImpl(StarMain.Companion.get太陰()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun太陰().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house貪狼 = new HouseMainStarImpl(StarMain.Companion.get貪狼()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun貪狼().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house巨門 = new HouseMainStarImpl(StarMain.Companion.get巨門()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun巨門().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house天相 = new HouseMainStarImpl(StarMain.Companion.get天相()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun天相().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house天梁 = new HouseMainStarImpl(StarMain.Companion.get天梁()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun天梁().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house七殺 = new HouseMainStarImpl(StarMain.Companion.get七殺()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun七殺().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };

  public final static IHouse house破軍 = new HouseMainStarImpl(StarMain.Companion.get破軍()) {
    @Override
    public Branch getBranch(Tuple5<Integer, Integer, Boolean, Integer , IPurpleStarBranch> t) {
      return StarMain.Companion.getFun破軍().invoke(t.v1() , t.v2() , t.v3() , t.v4() , t.v5());
    }
  };



  // =======↑↑↑======= 以上 14 顆主星 =======↑↑↑=======

  // =======↓↓↓======= 以下  8 顆吉星 =======↓↓↓=======
  
  public final static IHouse house文昌 = new HouseHourBranchImpl(Companion.get文昌()) {
    @Override
    public Branch getBranch(Branch branch) {
      return Companion.getFun文昌().invoke(branch);
    }
  };

  public final static IHouse house文曲 = new HouseHourBranchImpl(Companion.get文曲()) {
    @Override
    public Branch getBranch(Branch branch) {
      return Companion.getFun文曲().invoke(branch);
    }
  };

  public final static IHouse house左輔 = new HouseMonthImpl(StarLucky.Companion.get左輔()) {
    @Override
    public Branch getBranch(Integer finalMonthNum) {
      return StarLucky.Companion.getFun左輔_月數().invoke(finalMonthNum);
    }
  };

  public final static IHouse house右弼 = new HouseMonthImpl(Companion.get右弼()) {
    @Override
    public Branch getBranch(Integer finalMonthNum) {
      return Companion.getFun右弼_月數().invoke(finalMonthNum);
    }
  };

  public final static IHouse house天魁 = new HouseYearStemTianyiImpl(Companion.get天魁()) {
    @Override
    public Branch getBranch(Tuple2<Stem, TianyiIF> tuple) {
      return Companion.getFun天魁().invoke(tuple.v1() , tuple.v2());
    }
  };
  
  public final static IHouse house天鉞 = new HouseYearStemTianyiImpl(Companion.get天鉞()) {
    @Override
    public Branch getBranch(Tuple2<Stem, TianyiIF> tuple) {
      return Companion.getFun天鉞().invoke(tuple.v1() , tuple.v2());
    }
  };
  
  public final static IHouse house祿存 = new HouseYearStemImpl(Companion.get祿存()) {
    @Override
    public Branch getBranch(Stem stem) {
      return Companion.getFun祿存().invoke(stem);
    }
  };

  public final static IHouse house年馬 = new HouseYearBranchImpl(Companion.get年馬()) {
    @Override
    public Branch getBranch(Branch branch) {
      return Companion.getFun年馬_年支().invoke(branch);
    }
  };

  public final static IHouse house月馬 = new HouseMonthImpl(Companion.get月馬()) {
    @Override
    public Branch getBranch(Integer finalMonthNum) {
      return Companion.getFun月馬_月數().invoke(finalMonthNum);
    }
  };

  // =======↑↑↑======= 以上  8 顆吉星 =======↑↑↑=======
  
  // =======↓↓↓======= 以下  6 顆兇星 =======↓↓↓=======

  public final static IHouse house擎羊 = new HouseYearStemImpl(擎羊) {
    @Override
    public Branch getBranch(Stem stem) {
      return fun擎羊.apply(stem);
    }
  };

  public final static IHouse house陀羅 = new HouseYearStemImpl(陀羅) {
    @Override
    public Branch getBranch(Stem stem) {
      return fun陀羅.apply(stem);
    }
  };

  public final static IHouse house火星 = new HouseYearBranchHourBranchImpl(火星) {
    @Override
    public Branch getBranch(StemBranch lunarYear, StemBranch solarYear, Branch monthBranch, int finalMonthNumForMonthStars, SolarTerms solarTerms, int days, Branch hour, int set, Gender gender, boolean leap, int prevMonthDays, Optional<Branch> predefinedMainHouse, ZContext context) {
      Branch yearBranch = context.getYearType() == ZContext.YearType.YEAR_LUNAR ? lunarYear.getBranch() : solarYear.getBranch();
      switch (context.getFireBell()) {
        case FIREBELL_BOOK: return fun火星_全書.apply(yearBranch);
        case FIREBELL_COLLECT: return fun火星_全集.apply(yearBranch , hour);
        default: throw new AssertionError("error");
      }
    }
  };

  public final static IHouse house鈴星 = new HouseYearBranchHourBranchImpl(鈴星) {
    @Override
    public Branch getBranch(StemBranch lunarYear, StemBranch solarYear, Branch monthBranch, int finalMonthNumForMonthStars, SolarTerms solarTerms, int days, Branch hour, int set, Gender gender, boolean leap, int prevMonthDays, Optional<Branch> predefinedMainHouse, ZContext context) {
      Branch yearBranch = context.getYearType() == ZContext.YearType.YEAR_LUNAR ? lunarYear.getBranch() : solarYear.getBranch();
      switch (context.getFireBell()) {
        case FIREBELL_BOOK: return fun鈴星_全書.apply(yearBranch);
        case FIREBELL_COLLECT: return fun鈴星_全集.apply(yearBranch , hour);
        default: throw new AssertionError("error");
      }
    }
  };

  public final static IHouse house地劫 = new HouseHourBranchImpl(地劫) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun地劫.apply(branch);
    }
  };

  public final static IHouse house地空 = new HouseHourBranchImpl(地空) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun地空.apply(branch);
    }
  };


  // =======↑↑↑======= 以上  6 顆兇星 =======↑↑↑=======



  // =======↓↓↓======= 以下      雜曜 =======↓↓↓=======
  public final static IHouse house天官 = new HouseYearStemImpl(天官) {
    @Override
    public Branch getBranch(Stem stem) {
      return fun天官.apply(stem);
    }
  };

  public final static IHouse house天福 = new HouseYearStemImpl(天福) {
    @Override
    public Branch getBranch(Stem stem) {
      return fun天福.apply(stem);
    }
  };

  public final static IHouse house天廚 = new HouseYearStemImpl(天廚) {
    @Override
    public Branch getBranch(Stem stem) {
      return fun天廚.apply(stem);
    }
  };

  public final static IHouse house天刑 = new HouseMonthImpl(天刑) {
    @Override
    public Branch getBranch(Integer finalMonthNum) {
      return fun天刑_月數.apply(finalMonthNum);
    }
  };

  public final static IHouse house天姚 = new HouseMonthImpl(天姚) {
    @Override
    public Branch getBranch(Integer finalMonthNum) {
      return fun天姚_月數.apply(finalMonthNum);
    }
  };

  public final static IHouse house解神 = new HouseMonthImpl(解神) {
    @Override
    public Branch getBranch(Integer finalMonthNum) {
      return fun解神_月數.apply(finalMonthNum);
    }
  };

  public final static IHouse house天巫 = new HouseMonthImpl(天巫) {
    @Override
    public Branch getBranch(Integer finalMonthNum) {
      return fun天巫_月數.apply(finalMonthNum);
    }
  };

  public final static IHouse house天月 = new HouseMonthImpl(天月) {
    @Override
    public Branch getBranch(Integer finalMonthNum) {
      return fun天月_月數.apply(finalMonthNum);
    }
  };

  public final static IHouse house陰煞 = new HouseMonthImpl(陰煞) {
    @Override
    public Branch getBranch(Integer finalMonthNum) {
      return fun陰煞_月數.apply(finalMonthNum);
    }
  };

  public final static IHouse house台輔 = new HouseHourBranchImpl(台輔) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun台輔.apply(branch);
    }
  };

  public final static IHouse house封誥 = new HouseHourBranchImpl(封誥) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun封誥.apply(branch);
    }
  };

  public final static IHouse house天空 = new HouseYearBranchImpl(天空) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun天空.apply(branch);
    }
  };

  public final static IHouse house天哭 = new HouseYearBranchImpl(天哭) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun天哭.apply(branch);
    }
  };

  public final static IHouse house天虛 = new HouseYearBranchImpl(天虛) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun天虛.apply(branch);
    }
  };

  public final static IHouse house龍池 = new HouseYearBranchImpl(龍池) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun龍池.apply(branch);
    }
  };

  public final static IHouse house鳳閣 = new HouseYearBranchImpl(鳳閣) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun鳳閣.apply(branch);
    }
  };

  public final static IHouse house紅鸞 = new HouseYearBranchImpl(紅鸞) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun紅鸞.apply(branch);
    }
  };

  public final static IHouse house天喜 = new HouseYearBranchImpl(天喜) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun天喜.apply(branch);
    }
  };

  public final static IHouse house孤辰 = new HouseYearBranchImpl(孤辰) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun孤辰.apply(branch);
    }
  };

  public final static IHouse house寡宿 = new HouseYearBranchImpl(寡宿) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun寡宿.apply(branch);
    }
  };

  public final static IHouse house蜚廉 = new HouseYearBranchImpl(蜚廉) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun蜚廉.apply(branch);
    }
  };

  public final static IHouse house破碎 = new HouseYearBranchImpl(破碎) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun破碎.apply(branch);
    }
  };

  public final static IHouse house華蓋 = new HouseYearBranchImpl(華蓋) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun華蓋.apply(branch);
    }
  };

  public final static IHouse house咸池 = new HouseYearBranchImpl(咸池) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun咸池.apply(branch);
    }
  };

  public final static IHouse house天德 = new HouseYearBranchImpl(天德) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun天德.apply(branch);
    }
  };

  public final static IHouse house月德 = new HouseYearBranchImpl(月德) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun月德.apply(branch);
    }
  };

  public final static IHouse house天才 = new HouseYearBranchMonthNumHourBranchMainHouseImpl(天才) {
    @Override
    public Branch getBranch(Tuple3<Branch, Integer, Branch> t) {
      return fun天才.apply(t.v1() , t.v2() , t.v3());
    }
  };

  public final static IHouse house天壽 = new HouseYearBranchMonthNumHourBranchImpl(天壽) {
    @Override
    public Branch getBranch(Tuple3<Branch, Integer, Branch> t) {
      return fun天壽.apply(t.v1() , t.v2() , t.v3());
    }
  };

  public final static IHouse house三台 = new HouseMonthDayNumImpl(三台) {
    @Override
    public Branch getBranch(Tuple2<Integer, Integer> t) {
      return fun三台_月數.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house八座 = new HouseMonthDayNumImpl(八座) {
    @Override
    public Branch getBranch(Tuple2<Integer, Integer> t) {
      return fun八座_月數.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house恩光 = new HouseDayNumHourBranchImpl(恩光) {
    @Override
    public Branch getBranch(Tuple2<Integer, Branch> t) {
      return fun恩光.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house天貴 = new HouseDayNumHourBranchImpl(天貴) {
    @Override
    public Branch getBranch(Tuple2<Integer, Branch> t) {
      return fun天貴.apply(t.v1() , t.v2());
    }
  };


  public final static IHouse house天傷 = new HouseHouseDepYearStemGenderImpl(天傷) {
    @Override
    public Branch getBranch(StemBranch lunarYear, StemBranch solarYear, Branch monthBranch, int finalMonthNumForMonthStars, SolarTerms solarTerms, int days, Branch hour, int set, Gender gender, boolean leap, int prevMonthDays, Optional<Branch> predefinedMainHouse, ZContext context) {
      // 太乙派，沒有遷移宮
      IHouseSeq houseSeqImpl = new HouseSeqDefaultImpl();
      int steps = houseSeqImpl.getAheadOf(House.遷移 , House.命宮);

      Branch 遷移宮地支 = predefinedMainHouse
        .map(mainHouse -> mainHouse.prev(steps))
        .orElse(IZiwei.getHouseBranch(finalMonthNumForMonthStars, hour, House.遷移, houseSeqImpl));

      switch (context.getHurtAngel()) {
        case HURT_ANGEL_FIXED: return fun天傷_fixed交友.apply(遷移宮地支);
        case HURT_ANGEL_YINYANG: return fun天傷_陽順陰逆.apply(遷移宮地支 , lunarYear.getStem() , gender);
        default:
          throw new AssertionError("error");
      }
    }
  };

  public final static IHouse house天使 = new HouseHouseDepYearStemGenderImpl(天使) {
    @Override
    public Branch getBranch(StemBranch lunarYear, StemBranch solarYear, Branch monthBranch, int finalMonthNumForMonthStars, SolarTerms solarTerms, int days, Branch hour, int set, Gender gender, boolean leap, int prevMonthDays, Optional<Branch> predefinedMainHouse, ZContext context) {
      // 太乙派，沒有遷移宮
      IHouseSeq houseSeqImpl = new HouseSeqDefaultImpl();
      int steps = houseSeqImpl.getAheadOf(House.遷移 , House.命宮);
      Branch 遷移宮地支 = predefinedMainHouse
        .map(mainHouse -> mainHouse.prev(steps))
        .orElse(IZiwei.getHouseBranch(finalMonthNumForMonthStars, hour, House.遷移, houseSeqImpl));

      switch (context.getHurtAngel()) {
        case HURT_ANGEL_FIXED: return fun天使_fixed疾厄.apply(遷移宮地支);
        case HURT_ANGEL_YINYANG: return fun天使_陽順陰逆.apply(遷移宮地支 , lunarYear.getStem() , gender);
        default:
          throw new AssertionError("error");
      }
    }
  };

  public final static IHouse house陽空 = new HouseYearImpl(陽空) {
    @Override
    public Branch getBranch(StemBranch sb) {
      return fun陽空.apply(sb);
    }
  };

  public final static IHouse house陰空 = new HouseYearImpl(陰空) {
    @Override
    public Branch getBranch(StemBranch sb) {
      return fun陰空.apply(sb);
    }
  };

  /** 截空 : 正空 (截路) */
  public final static IHouse house正空 = new HouseYearStemImpl(正空) {
    @Override
    public Branch getBranch(Stem stem) {
      return fun正空_A.apply(stem);
    }
  };

  /** 截空 : 傍空 (空亡) */
  public final static IHouse house傍空 = new HouseYearStemImpl(傍空) {
    @Override
    public Branch getBranch(Stem stem) {
      return fun傍空_A.apply(stem);
    }
  };

  /** 紅艷 */
  public final static IHouse house紅艷 = new HouseYearStemImpl(紅艷) {
    @Override
    public Branch getBranch(Stem stem) {
      throw new RuntimeException("Error");
    }

    @Override
    public Branch getBranch(StemBranch lunarYear, StemBranch solarYear, Branch monthBranch, int finalMonthNumForMonthStars, SolarTerms solarTerms, int days, Branch hour, int set, Gender gender, boolean leap, int prevMonthDays, Optional<Branch> predefinedMainHouse, ZContext context) {
      switch (context.getRedBeauty()) {
        case RED_BEAUTY_SAME: return fun紅艷_甲乙相同.apply(context.getYearType() == ZContext.YearType.YEAR_LUNAR ? lunarYear.getStem() : solarYear.getStem());
        case RED_BEAUTY_DIFF: return fun紅艷_甲乙相異.apply(context.getYearType() == ZContext.YearType.YEAR_LUNAR ? lunarYear.getStem() : solarYear.getStem());
        default: throw new AssertionError("Error : " + context.getRedBeauty());
      }
    }
  };

  // =======↑↑↑======= 以上      雜曜 =======↑↑↑=======

  // =======↓↓↓======= 以下 博士12神煞 =======↓↓↓=======

  public final static IHouse house博士 = new HouseYearStemGenderImpl(博士) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return fun博士.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house力士 = new HouseYearStemGenderImpl(力士) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return fun力士.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house青龍 = new HouseYearStemGenderImpl(青龍) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return fun青龍.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house小耗 = new HouseYearStemGenderImpl(StarDoctor.小耗) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return StarDoctor.fun小耗.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house將軍 = new HouseYearStemGenderImpl(將軍) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return fun將軍.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house奏書 = new HouseYearStemGenderImpl(奏書) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return fun奏書.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house飛廉 = new HouseYearStemGenderImpl(飛廉) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return fun飛廉.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house喜神 = new HouseYearStemGenderImpl(喜神) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return fun喜神.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house病符 = new HouseYearStemGenderImpl(StarDoctor.病符) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return StarDoctor.fun病符.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house大耗 = new HouseYearStemGenderImpl(大耗) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return fun大耗.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house伏兵 = new HouseYearStemGenderImpl(伏兵) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return fun伏兵.apply(t.v1() , t.v2());
    }
  };

  public final static IHouse house官府 = new HouseYearStemGenderImpl(官府) {
    @Override
    public Branch getBranch(Tuple2<Stem, Gender> t) {
      return fun官府.apply(t.v1() , t.v2());
    }
  };

  // =======↑↑↑======= 以上 博士12神煞 =======↑↑↑=======

  // =======↓↓↓======= 以下 長生12神煞 =======↓↓↓=======
  public final static IHouse house長生 = new HouseFiveGenderYinYangImpl(長生) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun長生.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };

  public final static IHouse house沐浴 = new HouseFiveGenderYinYangImpl(沐浴) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun沐浴.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };

  public final static IHouse house冠帶 = new HouseFiveGenderYinYangImpl(冠帶) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun冠帶.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };

  public final static IHouse house臨官 = new HouseFiveGenderYinYangImpl(臨官) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun臨官.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };

  public final static IHouse house帝旺 = new HouseFiveGenderYinYangImpl(帝旺) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun帝旺.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };

  public final static IHouse house衰 = new HouseFiveGenderYinYangImpl(衰) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun衰.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };

  public final static IHouse house病 = new HouseFiveGenderYinYangImpl(病) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun病.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };

  public final static IHouse house死 = new HouseFiveGenderYinYangImpl(死) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun死.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };

  public final static IHouse house墓 = new HouseFiveGenderYinYangImpl(墓) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun墓.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };

  public final static IHouse house絕 = new HouseFiveGenderYinYangImpl(絕) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun絕.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };

  public final static IHouse house胎 = new HouseFiveGenderYinYangImpl(胎) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun胎.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };

  public final static IHouse house養 = new HouseFiveGenderYinYangImpl(養) {
    @Override
    public Branch getBranch(Tuple3<FiveElement, Gender, YinYangIF> t3) {
      return fun養.apply(t3.v1() , t3.v2() , t3.v3());
    }
  };
  // =======↑↑↑======= 以上 長生12神煞 =======↑↑↑=======


  // =======↓↓↓======= 以下 將前12星 =======↓↓↓=======
  public final static IHouse house將前_將星 = new HouseYearBranchImpl(將星) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun將星.apply(branch);
    }
  };
  public final static IHouse house將前_攀鞍 = new HouseYearBranchImpl(攀鞍) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun攀鞍.apply(branch);
    }
  };
  public final static IHouse house將前_歲馹 = new HouseYearBranchImpl(歲馹) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun歲馹.apply(branch);
    }
  };
  public final static IHouse house將前_息神 = new HouseYearBranchImpl(息神) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun息神.apply(branch);
    }
  };
  public final static IHouse house將前_華蓋 = new HouseYearBranchImpl(StarGeneralFront.華蓋) {
    @Override
    public Branch getBranch(Branch branch) {
      return StarGeneralFront.fun華蓋.apply(branch);
    }
  };
  public final static IHouse house將前_劫煞 = new HouseYearBranchImpl(劫煞) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun劫煞.apply(branch);
    }
  };
  public final static IHouse house將前_災煞 = new HouseYearBranchImpl(災煞) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun災煞.apply(branch);
    }
  };
  public final static IHouse house將前_天煞 = new HouseYearBranchImpl(天煞) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun天煞.apply(branch);
    }
  };
  public final static IHouse house將前_指背 = new HouseYearBranchImpl(指背) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun指背.apply(branch);
    }
  };
  public final static IHouse house將前_咸池 = new HouseYearBranchImpl(StarGeneralFront.咸池) {
    @Override
    public Branch getBranch(Branch branch) {
      return StarGeneralFront.fun咸池.apply(branch);
    }
  };
  public final static IHouse house將前_月煞 = new HouseYearBranchImpl(月煞) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun月煞.apply(branch);
    }
  };
  public final static IHouse house將前_亡神 = new HouseYearBranchImpl(亡神) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun亡神.apply(branch);
    }
  };

  // =======↑↑↑======= 以上 將前12星 =======↑↑↑=======


  // =======↓↓↓======= 以下 歲前12星 =======↓↓↓=======
  public final static IHouse house歲前_歲建 = new HouseYearBranchImpl(歲建) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun歲建.apply(branch);
    }
  };

  public final static IHouse house歲前_晦氣 = new HouseYearBranchImpl(晦氣) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun晦氣.apply(branch);
    }
  };

  public final static IHouse house歲前_喪門 = new HouseYearBranchImpl(喪門) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun喪門.apply(branch);
    }
  };

  public final static IHouse house歲前_貫索 = new HouseYearBranchImpl(貫索) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun貫索.apply(branch);
    }
  };

  public final static IHouse house歲前_官符 = new HouseYearBranchImpl(官符) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun官符.apply(branch);
    }
  };

  public final static IHouse house歲前_小耗 = new HouseYearBranchImpl(StarYearFront.小耗) {
    @Override
    public Branch getBranch(Branch branch) {
      return StarYearFront.fun小耗.apply(branch);
    }
  };

  public final static IHouse house歲前_歲破 = new HouseYearBranchImpl(歲破) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun歲破.apply(branch);
    }
  };

  public final static IHouse house歲前_龍德 = new HouseYearBranchImpl(龍德) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun龍德.apply(branch);
    }
  };

  public final static IHouse house歲前_白虎 = new HouseYearBranchImpl(白虎) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun白虎.apply(branch);
    }
  };

  public final static IHouse house歲前_天德 = new HouseYearBranchImpl(StarYearFront.天德) {
    @Override
    public Branch getBranch(Branch branch) {
      return StarYearFront.fun天德.apply(branch);
    }
  };

  public final static IHouse house歲前_吊客 = new HouseYearBranchImpl(吊客) {
    @Override
    public Branch getBranch(Branch branch) {
      return fun吊客.apply(branch);
    }
  };

  public final static IHouse house歲前_病符 = new HouseYearBranchImpl(StarYearFront.病符) {
    @Override
    public Branch getBranch(Branch branch) {
      return StarYearFront.fun病符.apply(branch);
    }
  };

  // =======↑↑↑======= 以上 歲前12星 =======↑↑↑=======


  public final static Set<IHouse> set = new ImmutableSet.Builder<IHouse>()
    // 14主星
    .add(house紫微, house天機, house太陽, house武曲, house天同, house廉貞, house天府, house太陰, house貪狼, house巨門, house天相, house天梁, house七殺, house破軍)

    /** 八吉星 ( {@link #house年馬} 與 {@link #house月馬} 其實就是 {@link StarLucky#年馬} 及 {@link StarLucky#月馬} )  */
    .add(house文昌, house文曲, house左輔, house右弼, house天魁, house天鉞, house祿存, house年馬 , house月馬)

    // 六兇星
    .add(house擎羊, house陀羅, house火星, house鈴星, house地劫, house地空)

    // 雜曜
    .add(house天官, house天福, house天廚, house天刑, house天姚, house解神, house天巫, house天月)
    .add(house陰煞, house台輔, house封誥, house天空, house天哭, house天虛, house龍池, house鳳閣)
    .add(house紅鸞, house天喜, house孤辰, house寡宿, house蜚廉, house破碎, house華蓋, house咸池)
    .add(house天德, house月德, house天才, house天壽, house三台, house八座, house恩光, house天貴)
    .add(house天傷, house天使)
    .add(house陽空, house陰空)
    .add(house正空, house傍空)
    .add(house紅艷)

    // 博士12神煞
    .add(house博士, house力士, house青龍, house小耗, house將軍, house奏書,
         house飛廉, house喜神, house病符, house大耗, house伏兵, house官府)

    // 長生12神煞
    .add(house長生, house沐浴, house冠帶, house臨官, house帝旺, house衰,
         house病  , house死  , house墓 , house絕  , house胎 , house養)

    // 將前12星
    .add(house將前_將星 , house將前_攀鞍 , house將前_歲馹 , house將前_息神 , house將前_華蓋 , house將前_劫煞 ,
         house將前_災煞 , house將前_天煞 , house將前_指背 , house將前_咸池 , house將前_月煞 , house將前_亡神)

    // 歲前12星
    .add(
      house歲前_歲建, house歲前_晦氣, house歲前_喪門, house歲前_貫索, house歲前_官符, house歲前_小耗,
      house歲前_歲破, house歲前_龍德, house歲前_白虎, house歲前_天德, house歲前_吊客, house歲前_病符)
    .build();

  public final static Map<ZStar , IHouse> map = set.stream()
    .collect(Collectors.toMap(IHouse::getStar, iHouse -> iHouse));

}
