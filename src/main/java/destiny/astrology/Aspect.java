/**
 * @author smallufo 
 * Created on 2007/11/24 at 上午 1:21:00
 */ 
package destiny.astrology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import destiny.utils.LocaleStringIF;

/** 交角 , Aspect */
public enum Aspect implements LocaleStringIF
{
  /** 0 , 合  */
  CONJUNCTION   ("Aspect.CONJUNCTION"   ,  0 , Importance.HIGH),
  /** 30 , 十二分相 , 半六合 */
  SEMISEXTILE   ("Aspect.SEMISEXTILE"   , 30 , Importance.MEDIUM),
  /** 36 , 十分相 */
  DECILE        ("Aspect.DECILE"        , 36 , Importance.LOW),
  /** 40 , 九分相 */
  NOVILE        ("Aspect.NOVILE"        , 40 , Importance.LOW),
  /** 45 , 半刑 , 八分相 , 半四分相 */
  SEMISQUARE    ("Aspect.SEMISQUARE"    , 45 , Importance.MEDIUM),
  /** 51.428 , 七分相 */
  SEPTILE       ("Aspect.SEPTILE"    , 360/7 , Importance.LOW),
  /** 60 , 六合 */
  SEXTILE       ("Aspect.SEXTILE"       , 60 , Importance.HIGH),
  /** 72 , 五分相 */
  QUINTILE      ("Aspect.QUINTILE"      , 72 , Importance.LOW),
  /** 80 , 倍九分相 */
  BINOVILE      ("Aspect.BINOVILE"      , 80 , Importance.LOW),
  /** 90 , 刑 */
  SQUARE        ("Aspect.SQUARE"        , 90 , Importance.HIGH),
  /** 102.857 , 倍七分相 */
  BISEPTILE     ("Aspect.BISEPTILE" ,360*2/7 , Importance.LOW),
  /** 108 , 補五分相 */
  SESQUIQUINTLE ("Aspect.SESQUIQUINTLE" ,108 , Importance.LOW),
  /** 120 , 三合 , 拱 */
  TRINE         ("Aspect.TRINE"         ,120 , Importance.HIGH),
  /** 135 , 補八分相 */
  SESQUIQUADRATE("Aspect.SESQUIQUADRATE",135 , Importance.MEDIUM),
  /** 144 , 倍五分相 */
  BIQUINTILE    ("Aspect.BIQUINTILE"    ,144 , Importance.LOW),
  /** 150 , 補十二分相 , 十二分之五相 */
  QUINCUNX      ("Aspect.QUINCUNX"      ,150 , Importance.MEDIUM),
  /** 154.285 , 七分之三分相 */
  TRISEPTILE    ("Aspect.TRISEPTILE",360*3/7 , Importance.LOW),
  /** 160 , 九分之四分相 */
  QUATRONOVILE  ("Aspect.QUATRONOVILE"  ,160 , Importance.LOW),
  /** 180 , 沖 */
  OPPOSITION    ("Aspect.OPPOSITION"    ,180 , Importance.HIGH);
  
  /** 重要度 : HIGH , MEDIUM , LOW */
  public enum Importance {HIGH,MEDIUM,LOW};
  
  private final static String resource = "destiny.astrology.Astrology";
  
  private String nameKey;
  private double degree;
  /** 重要度 */
  private Importance importance; 
  
  //原來是 Map<Importance , List<Aspect>> , 改以 ListMultimap 來做
  //FIXME : HoroscopeAspectsCalculatorModern.aspects [class=com.google.common.collect.LinkedListMultimap$1] <----- field that is not serializable

  /*
  private final static ListMultimap<Importance , Aspect> importanceAngles = LinkedListMultimap.create();
  static
  {
    for (Aspect eachAngle : Aspect.values())
      importanceAngles.get(eachAngle.getImportance()).add(eachAngle);
  }
  */
  
  private final static Map<Importance , List<Aspect>> importanceAngles = Collections.synchronizedMap(new HashMap<Importance , List<Aspect>>());
  static
  {
    for (Importance eachImportance : Importance.values())
      importanceAngles.put(eachImportance, new ArrayList<Aspect>());
    
    for (Aspect eachAngle : Aspect.values())
      importanceAngles.get(eachAngle.getImportance()).add(eachAngle);
  }
  
  private Aspect(String nameKey , double degree , Importance importance)
  {
    this.nameKey = nameKey;
    this.degree = degree;
    this.importance = importance;
  }
  
  /** 從「英文」的 aspect name 來反找 Aspect , 找不到則傳回 null */
  public static Aspect getAspect(String value)
  {
    for (Aspect aspect : Aspect.values())
    {
      if (aspect.toString(Locale.ENGLISH).equalsIgnoreCase(value))
        return aspect;
    }
    return null;
  }
  
  /** 取得度數 */
  public double getDegree()
  {
    return degree;
  }
  
  /** 取得重要度 */
  public Importance getImportance()
  {
    return importance;
  }
  
  /**
   * 取得某類重要度 (高/中/低) 的角度列表
   * <br/>
   * TODO : 這裡應該改傳回 Iterator 比較好？ */
  public static List<Aspect> getAngles(Importance importance)
  {
    return importanceAngles.get(importance);
  }
  
  
  /** 從 double 度數，找回符合的 Aspect */
  public static Aspect getAspect(double degree)
  {
    if (degree >=360 )
      return getAspect(degree - 360);
    
    if (degree < 0)
      return getAspect(degree + 360);
      
    if (degree > 180)
      return getAspect(360 - degree);
    
    for(Aspect aspect : values())
    {
      if (Math.abs(aspect.degree - degree) < 0.01)
        return aspect;
    }
    return null;
  }
  
  @Override
  public String toString()
  {
    return ResourceBundle.getBundle(resource , Locale.getDefault()).getString(nameKey);
  }
  
  @Override
  public String toString(Locale locale)
  {
    return ResourceBundle.getBundle(resource , locale).getString(nameKey);
  }
}