/**
 * @author smallufo 
 * Created on 2007/10/1 at 上午 6:39:15
 */ 
package destiny.astrology;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * Horoscope 只包含資料，不包含計算的介面 , 計算的介面交由 HoroscopeContext 處理
 * <pre>
 * 描述一個星盤的資料結構 , 包含 :
 * 每顆點 (Point) 的 位置 , 含地平方位角 ( PositionWithAzimuth ) , 資料結構存放於 positionMap
 * 每個宮的宮首 (HouseCusp) 位於黃道幾度 , 資料結構存放於 cusps[13]
 * </pre>
 */
@Deprecated
public class Horoscope implements Serializable, HoroscopeIF {

  private final HoroscopeContext context;
  
  /** 星 (Star) 與 位置(Position)＋地平方位角(Azimuth) ==>  (PositionWithAzimuth) 的 Map , 作為 cache 層 */
  //private Map<Point , PositionWithAzimuth> positionMap = new HashMap<Point , PositionWithAzimuth>();
  
  /**
   * 地盤 12個宮在黃道上的度數 , cusps[0]==0 不用
   */
  private double[] cusps = new double[13]; 

  /** constructor */
  public Horoscope(HoroscopeContext context , double[] cusps)
  {
    this.context = context;
    this.cusps = cusps;
  }

  @Override
  public Set<Point> getPoints() {
    Set<Point> points = new HashSet<>();
    points.addAll(Arrays.asList(Planet.values));
    points.add(LunarNode.NORTH_MEAN);
    return points;
  }

  /**
   * 取得第幾宮的宮首落於黃道幾度。 1 <= cusp <= 12
   */
  @Override
  public double getCuspDegree(int cusp)
  {
    if (cusp > 12)
      return getCuspDegree(cusp - 12);
    else if (cusp < 1)
      return getCuspDegree(cusp + 12);
    return cusps[cusp];
  }
  
  /**
   * 取得單一 Horoscope 中 , 任兩顆星的交角
   */
  @Override
  public double getAngle(Point fromPoint, Point toPoint)
  {
    return Horoscope2.getAngle(context.getPosition(fromPoint).getLng() , context.getPosition(toPoint).getLng());
  }
  
  /**
   * 取得此兩顆星，對於此交角 Aspect 的誤差是幾度
   * 例如兩星交角 175 度 , Aspect = 沖 (180) , 則 誤差 5 度
   */  
  @Override
  public double getAspectError(Point p1, Point p2, @NotNull Aspect aspect)
  {
    double angle = getAngle(p1 , p2); //其值必定小於等於 180度
    return Math.abs( aspect.getDegree() - angle);
  }
  
  /** 取得一顆星體 Point / Star 在星盤上的角度 */
  @Override
  public PositionWithAzimuth getPositionWithAzimuth(Point point)
  {
    return context.getPosition(point);
  }
  
  /**
   * 取得一連串星體的位置（含地平方位角）
   */
  @Override
  public Map<Star , PositionWithAzimuth> getPositionWithAzimuth(@NotNull List<Star> stars)
  {
    Map<Star , PositionWithAzimuth> resultMap = Collections.synchronizedMap(new HashMap<>());
    for(Star star : stars)
      resultMap.put(star, context.getPosition(star));
      //resultMap.put(star, positionMap.get(star));
    return resultMap;
  }
  
  /**
   * 取得所有行星 (Planet) 的位置
   */
  @Override
  public Map<Planet , PositionWithAzimuth> getPlanetPositionWithAzimuth()
  {
    Map<Planet , PositionWithAzimuth> resultMap = Collections.synchronizedMap(new HashMap<>());
    for(Planet planet : Planet.values)
      resultMap.put(planet , context.getPosition(planet));
      //resultMap.put(planet, positionMap.get(planet));
    return resultMap;
  }
  
  /**
   * 取得所有小行星 (Asteroid) 的位置
   */
  @Override
  public Map<Asteroid , PositionWithAzimuth> getAsteroidPositionWithAzimuth()
  {
    Map<Asteroid , PositionWithAzimuth> resultMap = Collections.synchronizedMap(new HashMap<>());
    for(Asteroid asteroid : Asteroid.values)
      resultMap.put(asteroid , context.getPosition(asteroid));
      //resultMap.put(asteroid, positionMap.get(asteroid));
    return resultMap;
  }
  
  /**
   * 取得八個漢堡學派虛星 (Hamburger) 的位置
   */
  @Override
  public Map<Hamburger , PositionWithAzimuth> getHamburgerPositionWithAzimuth()
  {
    Map<Hamburger , PositionWithAzimuth> resultMap = Collections.synchronizedMap(new HashMap<>());
    for(Hamburger hamburger : Hamburger.values)
      resultMap.put(hamburger , context.getPosition(hamburger));
      //resultMap.put(hamburger, positionMap.get(hamburger));
    return resultMap;
  }
  
  
  /**
   * 黃道幾度，落於第幾宮 ( 1 <= house <= 12 )
   */
  @Override
  public int getHouse(double degree) {
    for (int i = 1; i <= 11; i++) {
      if (Math.abs(cusps[i + 1] - cusps[i]) < 180) {
        //沒有切換360度的問題
        if (cusps[i] <= degree && degree < cusps[i + 1])
          return i;
      }
      else {
        //切換360度
        if ((cusps[i] <= degree && degree < (cusps[i + 1] + 360)) || (cusps[i] <= (degree + 360) && degree < cusps[i + 1]))
          return i;
      }
    }
    return 12;
  } //getHouse()
  

}
