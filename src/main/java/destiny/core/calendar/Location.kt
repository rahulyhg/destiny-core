/**
 * Created by smallufo on 2018-03-15.
 */
package destiny.core.calendar

import destiny.tools.LocaleTools
import destiny.tools.location.TimeZoneUtils
import java.io.Serializable
import java.time.ZoneId
import java.util.*
import kotlin.math.abs

/** 純粹經緯度座標，沒有時區 [TimeZone] [ZoneId] 或是 時差 (minuteOffset) 等資訊 */
interface ILatLng {
  val lng: Double
  val lat: Double

  val eastWest: EastWest
    get() =
      if (lng >= 0)
        EastWest.EAST
      else
        EastWest.WEST

  val lngDeg: Int
    get() = abs(lng).toInt()

  val lngMin: Int
    get() = ((abs(lng) - lngDeg) * 60).toInt()

  val lngSec: Double
    get() = abs(lng) * 3600 - (lngDeg * 3600).toDouble() - (lngMin * 60).toDouble()

  val northSouth: NorthSouth
    get() =
      if (lat >= 0)
        NorthSouth.NORTH
      else
        NorthSouth.SOUTH


  val latDeg: Int
    get() = abs(lat).toInt()

  val latMin: Int
    get() = ((abs(lat) - latDeg) * 60).toInt()

  val latSec: Double
    get() = abs(lat) * 3600 - (latDeg * 3600).toDouble() - (latMin * 60).toDouble()

  /** 取得經緯度的十進位表示法，先緯度、再經度 */
  val decimal: String
    get() = with(StringBuffer()) {
      append(lat)
      append(',')
      append(lng)
    }.toString()
}

interface ILocation : ILatLng {

  val tzid: String?

  /** 強制覆蓋與 GMT 的時差 , 優先權高於 [tzid]  */
  val minuteOffset: Int?

  val hasMinuteOffset: Boolean
    get() = minuteOffset != null

  val finalMinuteOffset: Int
    get() = minuteOffset ?: TimeZone.getTimeZone(tzid).rawOffset / (60 * 1000)

  /** 高度（公尺） */
  val altitudeMeter: Double?

  val timeZone: TimeZone
    get() = tzid?.let {
      TimeZone.getTimeZone(it)
    } ?: minuteOffset?.let {
      TimeZoneUtils.getTimeZone(it)
    } ?: TimeZone.getTimeZone("GMT")

  val zoneId: ZoneId
    get() = tzid?.let {
      ZoneId.of(it)
    } ?: minuteOffset?.let {
      TimeZoneUtils.getTimeZone(it).toZoneId()
    } ?: ZoneId.of("GMT")

} // ILocation

interface IPlace {
  val place: String
}


data class Location(override val lng: Double,
                    override val lat: Double,
                    override val tzid: String?,
                    override val minuteOffset: Int?,
                    override val altitudeMeter: Double?) : ILocation, Serializable {

  constructor(lng: Double, lat: Double, tzid: String?, minuteOffset: Int?) :
    this(lng, lat, tzid, minuteOffset, null)

  constructor(lng: Double, lat: Double, tzid: String?) :
    this(lng, lat, tzid, null)

  constructor(lng: Double, lat: Double) :
    this(lng, lat, null)

  /**
   * 最詳盡的 constructor
   *
   * 2012/03 格式：
   * 012345678901234567890123456789012345678901234567890
   * +DDDMMSSSSS+DDMMSSSSS Alt~ TimeZone~ [minuteOffset]
   * */
  constructor(eastWest: EastWest,
              lngDeg: Int,
              lngMin: Int,
              lngSec: Double? = 0.0,
              northSouth: NorthSouth,
              latDeg: Int,
              latMin: Int,
              latSec: Double? = 0.0,
              tzid: String?,
              minuteOffset: Int? = null,
              altitudeMeter: Double? = null) : this(
    (lngDeg.toDouble() + lngMin.toDouble() / 60.0 + lngSec!! / 3600.0).let { if (eastWest == EastWest.WEST) 0 - it else it },
    (latDeg.toDouble() + latMin.toDouble() / 60.0 + latSec!! / 3600.0).let { if (northSouth == NorthSouth.SOUTH) 0 - it else it },
    tzid, minuteOffset, altitudeMeter)


  /** 大家比較常用的，只有「度、分」。省略「秒」以及「高度」 */
  constructor(eastWest: EastWest, lngDeg: Int, lngMin: Int,
              northSouth: NorthSouth, latDeg: Int, latMin: Int,
              tzid: String) :
    this(eastWest, lngDeg, lngMin, 0.0, northSouth, latDeg, latMin, 0.0, tzid)


  /** 比較省略的 constructor  , 去除東西經、南北緯 , 其值由 經度/緯度的正負去判斷 */
  constructor(lngDeg: Int, lngMin: Int, lngSec: Double,
              latDeg: Int, latMin: Int, latSec: Double,
              tzid: String) : this(
    (abs(lngDeg).toDouble() + lngMin.toDouble() / 60.0 + lngSec / 3600.0).let { if (lngDeg < 0) 0 - it else it },
    (abs(latDeg).toDouble() + latMin.toDouble() / 60.0 + latSec / 3600.0).let { if (latDeg < 0) 0 - it else it },
    tzid, null, null)


  /** 較省略的 constructor , 度數以 double 取代 */
  constructor(eastWest: EastWest, lng: Double,
              northSouth: NorthSouth, lat: Double,
              tzid: String, minuteOffset: Int? = null, altitudeMeter: Double? = null) : this(
    lng.let { if (eastWest == EastWest.WEST) 0 - it else it },
    lat.let { if (northSouth == NorthSouth.SOUTH) 0 - it else it },
    tzid, minuteOffset, altitudeMeter)


  companion object {



    private val locMap = mapOf(
      // de , 柏林
      Locale.GERMAN to Location(EastWest.EAST, 13, 24, NorthSouth.NORTH, 52, 31, "Europe/Berlin"),
      // de_DE , 柏林
      Locale.GERMANY to Location(EastWest.EAST, 13, 24, NorthSouth.NORTH, 52, 31, "Europe/Berlin"),
      // en , 紐約 , 40.758899, -73.985131 , 時報廣場
      Locale.ENGLISH to Location(-73.985131, 40.758899, "America/New_York"),
      // en_US , 紐約
      Locale.US to Location(-73.985131, 40.758899, "America/New_York"),
      // en_AU , 雪梨
      Locale("en", "AU") to Location(EastWest.EAST, 151, 12, 40.0, NorthSouth.SOUTH, 33, 51, 36.0, "Australia/Sydney"),
      // en_BW , 波札那 Botswana
      Locale("en", "BW") to Location(EastWest.EAST, 25, 55, NorthSouth.SOUTH, 24, 40, "Africa/Gaborone"),
      // en_CA , 多倫多
      Locale.CANADA to Location(EastWest.WEST, 79, 24, NorthSouth.NORTH, 43, 40, "America/Toronto"),
      // en_DK , 丹麥 哥本哈根 Copenhagen
      Locale("en", "DK") to Location(EastWest.EAST, 12, 34, NorthSouth.NORTH, 55, 43, "Europe/Copenhagen"),
      // en_GB , 倫敦
      Locale.UK to Location(EastWest.WEST, 0, 7, NorthSouth.NORTH, 51, 30, "Europe/London"),
      // en_HK , 香港
      Locale("en", "HK") to Location(114.1735865, 22.2798721, "Asia/Hong_Kong"),
      // en_IE , 愛爾蘭 Ireland , 都柏林 Dublin
      Locale("en", "IE") to Location(EastWest.WEST, 6.2592, NorthSouth.NORTH, 53.3472, "Europe/Dublin"),
      // en_MY , 馬來西亞 , 吉隆坡
      Locale("en", "MY") to Location(EastWest.EAST, 101, 42, NorthSouth.NORTH, 3, 8, "Asia/Kuala_Lumpur"),
      // en_NZ , 紐西蘭 , 奧克蘭 Auckland (最大城市)
      Locale("en", "NZ") to Location(EastWest.EAST, 174, 45, NorthSouth.SOUTH, 36, 52, "Pacific/Auckland"),
      // en_PH , 菲律賓 , 馬尼拉
      Locale("en", "PH") to Location(EastWest.EAST, 121, 0, NorthSouth.NORTH, 14, 35, "Asia/Manila"),
      // en_SG , 新加坡
      Locale("en", "SG") to Location(EastWest.EAST, 103, 51, NorthSouth.NORTH, 1, 17, "Asia/Singapore"),
      // en_ZA , 南非 , 約翰尼斯堡
      Locale("en", "ZA") to Location(EastWest.EAST, 27, 54, NorthSouth.SOUTH, 26, 8, "Africa/Johannesburg"),
      // en_ZW , 辛巴威 , 哈拉雷
      Locale("en", "ZW") to Location(EastWest.EAST, 31, 3, NorthSouth.SOUTH, 17, 50, "Africa/Harare"),
      // fr , 巴黎
      Locale.FRENCH to Location(EastWest.EAST, 2, 20, NorthSouth.NORTH, 48, 52, "Europe/Paris"),
      // fr_FR , 巴黎
      Locale.FRANCE to Location(EastWest.EAST, 2, 20, NorthSouth.NORTH, 48, 52, "Europe/Paris"),
      // it , 羅馬
      Locale.ITALIAN to Location(EastWest.EAST, 12, 29, NorthSouth.NORTH, 41, 54, "Europe/Rome"),
      // it_IT , 羅馬
      Locale.ITALY to Location(EastWest.EAST, 12, 29, NorthSouth.NORTH, 41, 54, "Europe/Rome"),
      // ja , 東京
      Locale.JAPANESE to Location(EastWest.EAST, 139, 46, 0.0, NorthSouth.NORTH, 35, 40, 50.0, "Asia/Tokyo"),
      // ja_JP , 東京
      Locale.JAPAN to Location(EastWest.EAST, 139, 45, NorthSouth.NORTH, 35, 40, "Asia/Tokyo"),
      // ko , 首爾
      Locale.KOREAN to Location(EastWest.EAST, 127, 0, NorthSouth.NORTH, 37, 32, "Asia/Seoul"),
      // ko_KR , 首爾
      Locale.KOREA to Location(EastWest.EAST, 127, 0, NorthSouth.NORTH, 37, 32, "Asia/Seoul"),
      // zh , 北京
      Locale.CHINESE to Location(116.397, 39.9075, "Asia/Harbin"),
      // zh_CN , PRC == CHINA == SIMPLIFIED_CHINESE , 北京
      Locale.CHINA to Location(EastWest.EAST, 116, 23, NorthSouth.NORTH, 39, 55, "Asia/Shanghai"),
      // zh_HK , 香港
      Locale("zh", "HK") to Location(114.1735865, 22.2798721, "Asia/Hong_Kong"),
      // zh_MO , 澳門
      Locale("zh", "MO") to Location(EastWest.EAST, 113, 35, NorthSouth.NORTH, 22, 14, "Asia/Macao"),
      // zh_SG , 新加坡
      Locale("zh", "SG") to Location(EastWest.EAST, 103, 51, NorthSouth.NORTH, 1, 17, "Asia/Singapore"),
      // zh_TW , TAIWAN == TRADITIONAL_CHINESE , 台北市 景福門 (25.039059 , 121.517675) ==> 25°02'20.5"N 121°31'03.6"E
      Locale.TAIWAN to Location(121.517668, 25.039030, "Asia/Taipei")
                              )


    fun of(locale: Locale): Location {
      val matchedLocale = LocaleTools.getBestMatchingLocale(locale, locMap.keys) ?: Locale.getDefault()
      val loc = locMap[matchedLocale]!!
      return Location(loc.lng, loc.lat, loc.tzid, loc.minuteOffset, loc.altitudeMeter)
    }

    fun getLongitude(ew: EastWest, lngDeg: Int, lngMin: Int, lngSec: Double): Double {
      return (lngDeg.toDouble() + lngMin.toDouble() / 60.0 + lngSec / 3600.0).let {
        if (ew == EastWest.WEST)
          0 - it
        else
          it
      }
    }

    fun getLatitude(nw: NorthSouth, latDeg: Int, latMin: Int, latSec: Double): Double {
      return (latDeg.toDouble() + latMin.toDouble() / 60.0 + latSec / 3600.0).let {
        if (nw == NorthSouth.SOUTH)
          0 - it
        else
          it
      }
    }
  }

  fun toString(locale: Locale): String {
    return LocationDecorator.getOutputString(this, locale)
  }

  override fun toString(): String {
    return toString(Locale.getDefault())
  }
} // Location


interface ILocationPlace : ILocation, IPlace

data class LocationPlace(val location: ILocation, override val place: String) : ILocationPlace, ILocation by location,
  Serializable {
  fun toString(locale: Locale): String {
    return LocationDecorator.getOutputString(this, locale)
  }

  override fun toString(): String {
    return toString(Locale.getDefault())
  }
}