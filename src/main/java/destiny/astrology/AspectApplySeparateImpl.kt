/**
 * Created by smallufo at 2008/11/6 下午 6:21:59
 */
package destiny.astrology

import java.io.Serializable
import java.time.temporal.ChronoUnit

class AspectApplySeparateImpl(
  /** 可以注入現代占星 ( AspectEffectiveModern ) 或是古典占星 ( AspectEffectiveClassical ) 的實作  */
  private val aspectEffectiveImpl: IAspectEffective,
  private val starPositionWithAzimuthImpl: IStarPositionWithAzimuth,
  private val houseCuspImpl : IHouseCusp
                             ) : IAspectApplySeparate, Serializable {



  /**
   * 判斷兩顆星體是否形成某交角 , 如果是的話 , 傳回 入相位或是出相位 ; 如果沒有形成交角 , 傳回 null
   * 計算方式：這兩顆星的交角，與 Aspect 的誤差，是否越來越少
   */
  override fun getAspectType(h: IHoroscopeModel,
                             p1: Point,
                             p2: Point,
                             aspect: Aspect): IAspectApplySeparate.AspectType? {
    val deg1 = h.getPositionWithAzimuth(p1).lng
    val deg2 = h.getPositionWithAzimuth(p2).lng

    if (aspectEffectiveImpl.isEffective(p1, deg1, p2, deg2, aspect)) {
      val planetsAngle = IHoroscopeModel.getAngle(deg1, deg2)
      val error = Math.abs(planetsAngle - aspect.degree) //目前與 aspect 的誤差

      val lmt = h.lmt //目前時間
      val oneSecondLater = lmt.plus(1, ChronoUnit.SECONDS) // 一秒之後

      val hContext : IHoroscopeContext = HoroscopeContext(h.points, h.houseSystem, h.coordinate,
                                                          h.centric, starPositionWithAzimuthImpl, houseCuspImpl)
      val h2 = hContext.getHoroscope(lmt = oneSecondLater, loc = h.location, place = h.place,
                                     points = IHoroscopeContext.defaultPoints,
                                     houseSystem = null, coordinate = null, centric = null, temperature = h.temperature,
                                     pressure = h.pressure)

//      val h2 = horoscopeImpl.getHoroscope(oneSecondLater, h.location, null, h.points, h.houseSystem, h.centric, h.coordinate, h.temperature, h.pressure)

      val deg1_next = h2.getPositionWithAzimuth(p1).lng
      val deg2_next = h2.getPositionWithAzimuth(p2).lng
      val planetsAngle_next = IHoroscopeModel.getAngle(deg1_next, deg2_next)
      val error_next = Math.abs(planetsAngle_next - aspect.degree)

      //System.out.println(p1 + " 與 " + p2 + " 形成 " + aspect + " , 誤差 " + error_next + " 度");

      return if (error_next <= error)
        IAspectApplySeparate.AspectType.APPLYING
      else
        IAspectApplySeparate.AspectType.SEPARATING
    } else
      return null //這兩顆星沒有形成交角
  }

  override fun getAspectType(h: IHoroscopeModel,
                             p1: Point,
                             p2: Point,
                             aspects: Collection<Aspect>): IAspectApplySeparate.AspectType? {
    var aspectType: IAspectApplySeparate.AspectType? = null
    for (aspect in aspects) {
      aspectType = getAspectType(h, p1, p2, aspect)
      if (aspectType != null)
        break
    }
    return aspectType
  }

}
