/*
 * @author smallufo
 * @date 2004/11/20
 * @time 上午 01:47:36
 */
package destiny.core.calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.TimeZone;

import static destiny.core.calendar.Time.getDstSecondOffset;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.*;

public class TimeTest
{
  private Logger logger = LoggerFactory.getLogger(getClass());

  private Time time;
  private Time origin;
  private Time actual ;
  private Time expected ;

  @Test
  public void testTime() {
    Time t = new Time();
    t.setSecond(12.1234);
    String s = String.format("%02d月%02d日 %02d時 %02d分 ", t.getMonth(), t.getDay(), t.getHour(), t.getMinute())
      + StringUtils.leftPad(String.format("%3.2f秒", t.getSecond()), 6, '0');
    System.out.println("time = " + s);
  }


  /**
   * 已知：
   * 民國63年至64年（西元1974-1975年）    日光節約時間    4月1日至9月30日
   */
  @Test
  public void testGetGmtFromLmt() {
    Location loc = new Location(121.30, 25.03, TimeZone.getTimeZone("Asia/Taipei"));
    LocalDateTime gmt, lmt;

    //日光節約時間前一秒
    lmt = LocalDateTime.of(1974, 3, 31, 23, 59, 59);
    gmt = Time.getGmtFromLmt(lmt, loc);
    assertEquals(LocalDateTime.of(1974, 3, 31, 15, 59, 59), gmt);

    //加上一秒，開始日光節約時間，時間調快一小時 , 變成 GMT+9
    lmt = LocalDateTime.of(1974, 4, 1, 0, 0, 0);
    gmt = Time.getGmtFromLmt(lmt, loc);
    assertEquals(LocalDateTime.of(1974, 3, 31, 16, 0, 0), gmt);

    //真正日光節約時間，開始於 1:00AM
    lmt = LocalDateTime.of(1974, 4, 1, 1, 0, 0);
    gmt = Time.getGmtFromLmt(lmt, loc);
    assertEquals(LocalDateTime.of(1974, 3, 31, 16, 0, 0), gmt); //真正銜接到「日光節約時間」前一秒

    // 日光節約時間前一秒 , 仍是 GMT+9
    lmt = LocalDateTime.of(1974, 9, 30, 23, 59, 59);
    gmt = Time.getGmtFromLmt(lmt, loc);
    assertEquals(LocalDateTime.of(1974, 9, 30, 14, 59, 59), gmt);

    lmt = Time.getLmtFromGmt(LocalDateTime.of(1974, 9, 30, 14, 0, 0), loc);
    System.err.println(lmt); //推估當時可能過了兩次 23:00-24:00 的時間，以調節和 GMT 的時差


    // 結束日光節約時間 , 調回 GMT+8
    lmt = LocalDateTime.of(1974, 10, 1, 0, 0, 0);
    gmt = Time.getGmtFromLmt(lmt, loc);
    assertEquals(LocalDateTime.of(1974, 9, 30, 16, 0, 0), gmt);
  }

  /**
   * 已知：
   * 民國63年至64年（西元1974-1975年）    日光節約時間    4月1日至9月30日
   */
  @Test
  public void testGetLmtFromGmt()
  {
    Location loc = new Location(121.30, 25.03, TimeZone.getTimeZone("Asia/Taipei"));
    LocalDateTime gmt, lmt;

    //日光節約時間前一秒
    gmt = LocalDateTime.of(1974, 3, 31, 15, 59, 0);
    lmt = Time.getLmtFromGmt(gmt, loc);
    System.out.println(lmt);
    assertEquals(LocalDateTime.of(1974, 3, 31, 23, 59, 0), lmt);

    //開始日光節約時間
    gmt = LocalDateTime.of(1974, 3, 31, 16, 0, 0);
    lmt = Time.getLmtFromGmt(gmt, loc);
    System.out.println(lmt);
    assertEquals(LocalDateTime.of(1974, 4, 1, 1, 0, 0), lmt); //跳躍一小時

    //日光節約時間結束前一秒
    gmt = LocalDateTime.of(1974, 9, 30, 14, 59, 59);
    lmt = Time.getLmtFromGmt(gmt, loc);
    System.out.println(lmt);
    assertEquals(LocalDateTime.of(1974, 9, 30, 23, 59, 59), lmt); //與下面重複

    //日光節約時間結束前一秒
    gmt = LocalDateTime.of(1974, 9, 30, 15, 59, 59);
    lmt = Time.getLmtFromGmt(gmt, loc);
    System.out.println(lmt);
    assertEquals(LocalDateTime.of(1974, 9, 30, 23, 59, 59), lmt); //與上面重複

    //日光節約時間結束
    gmt = LocalDateTime.of(1974, 9, 30, 16, 0, 0);
    lmt = Time.getLmtFromGmt(gmt, loc);
    System.out.println(lmt);
    assertEquals(LocalDateTime.of(1974, 10, 1, 0, 0, 0), lmt);
  }


  /** 測試從 "+20000102000000.00" 建立時間 */
  @Test
  public void testTimeFromString()
  {
    actual = new Time("+20000203040506.789");
    expected = new Time(true , 2000 , 2 , 3 , 4 , 5 , 6.789);
    assertEquals(expected , actual);

    actual = new Time("-20010203040506.789");
    expected = new Time(false , 2001 , 2 , 3 , 4 , 5 , 6.789);
    assertEquals(expected , actual);
  }

  @Test
  public void testTimeDebugString()
  {
    expected = new Time(true , 2001 , 2 , 3 , 4 , 5 , 6.789);
    actual = new Time(expected.getDebugString());
    assertEquals(expected , actual);

    expected = new Time(true , 1 , 2 , 3 , 4 , 5 , 6.789);
    actual = new Time(expected.getDebugString());
    assertEquals(expected , actual);
  }

  /**
   * 1582/10/4 之後跳到 1582/10/15 , 之前是 Julian Calendar , 之後是 Gregorian Calendar
   * 測試 10/5~10/14 之間的錯誤日期
   */
  @Test
  public void testInvalidTime()
  {
    try
    {
      origin = new Time(1582 , 10 , 5 , 0 , 0 , 0);
      fail("Should throw new RuntimeException");
    }
    catch(RuntimeException expected)
    {
      assertTrue(true);
    }

    try
    {
      origin = new Time(1582 , 10 , 14 , 0 , 0 , 0);
      fail("Should throw new RuntimeException");
    }
    catch(RuntimeException expected)
    {
      assertTrue(true);
    }
  }


  /** 測試 1582/10/4 --- 1582/10/15 的日期轉換 , 以「日」為單位來 diff , 先避掉 Round-off error */
  @Test
  public void testTimeDiffSeconds2()
  {
    origin = new Time(1582,10,15,0,0,0);
    actual = new Time(origin , -1*24*60*60);
    expected= new Time(1582,10,4,0,0,0);
    assertEquals(expected , actual);

    origin = new Time(1582,10,4,0,0,0);
    actual = new Time(origin , 1*24*60*60);
    expected= new Time(1582,10,15,0,0,0);
    assertEquals(expected , actual);
  }

  /** 測試 1582/10/15 子初的日期轉換 , 以「秒」為單位來 diff , 要能解決 Round-off error */
  @Test
  public void testTimeDiffSeconds3()
  {
    origin = new Time(1582,10,15,0,0,0);
    actual = new Time(origin , 1); //先往後加一秒，不會碰到切換點
    expected= new Time(1582,10,15,0,0,1);
    assertEquals(expected , actual);

    origin = new Time(1582,10,15,0,0,0);
    actual = new Time(origin , -1); //往前加一秒，會碰到 Gregorian/Julian 切換點
    expected= new Time(1582,10,4,23,59,59);
    assertEquals(expected.getGmtJulDay() , actual.getGmtJulDay() , 0.01);
  }

  /** 測試 1582/10/4 子夜 的日期轉換 , 以「秒」為單位來 diff , 要能解決 Round-off error */
  @Test
  public void testTimeDiffSeconds4()
  {
    origin = new Time(1582,10,4,23,59,59);
    actual = new Time(origin , -1); //先往前減一秒，不會碰到切換點
    expected= new Time(1582,10,4,23,59,58);
    assertEquals(expected , actual);

    origin = new Time(1582,10,4,23,59,59);
    actual = new Time(origin , 1); //往後加一秒，會碰到 Gregorian/Julian 切換點
    expected= new Time(1582,10,15,0,0,0);
    assertEquals(expected , actual);
  }

  /**
   * 測試由 Julian Day 建立 Time
   * 可以藉由這裡比對 http://aa.usno.navy.mil/data/docs/JulianDate.php
   */
  @Test
  public void testTimeFromJulianDay()
  {
    //曆法交界日期

    // 測試 1582/10/4 --- 1582/10/15 的日期轉換
    actual = new Time(2299160.5 );
    expected = new Time(1582,10,15,0,0,0);
    assertEquals(expected , actual);

    // JD 往前一天 , 跳到 1582/10/4
    actual = new Time(2299159.5 );
    expected = new Time(1582,10,4,0,0,0);
    assertEquals(expected , actual);


    actual = new Time(2453330);
    expected = new Time(2004, 11,20 , 12 , 0 , 0);
    assertEquals(expected , actual);

    actual = new Time(2452549);
    expected = new Time(2002, 10, 1 , 12 , 0 , 0);
    assertEquals(expected , actual);

    //西元元年一月一號
    actual = new Time(1721423.5);
    expected = new Time(true , 1 , 1 , 1 , 0 , 0 , 0);
    assertEquals(expected , actual);

    //西元前一年十二月三十一號
    actual = new Time(1721422.5);
    expected = new Time(false , 1 , 12 , 31 , 0 , 0 , 0);
    assertEquals(expected , actual);

    //西元前一年一月一號
    actual = new Time(1721057.5);
    expected = new Time(false , 1 , 1 , 1 , 0 , 0 , 0);
    assertEquals(expected , actual);

    //西元前二年十二月三十一號
    actual = new Time(1721056.5);
    expected = new Time(false , 2 , 12 , 31 , 0 , 0 , 0);
    assertEquals(expected , actual);
  }

  /**
   * 測試 Time.normalize() 進位（退位）是否正確
   **/
  @Test
  public void testNormalize()
  {
    Time t1 , t2;

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , 60);
    t2 = new Time(2004 , 11 , 20 , 2 , 31 ,  0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , 120);
    t2 = new Time(2004 , 11 , 20 , 2 , 32 ,  0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , -1 );
    t2 = new Time(2004 , 11 , 20 , 2 , 29 ,  59);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , -59 );
    t2 = new Time(2004 , 11 , 20 , 2 , 29 ,  1);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , -61 );
    t2 = new Time(2004 , 11 , 20 , 2 , 28 ,  59);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , -60 );
    t2 = new Time(2004 , 11 , 20 , 2 , 29 ,  0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , -61 );
    t2 = new Time(2004 , 11 , 20 , 2 , 28 ,  59 );
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , -120);
    t2 = new Time(2004 , 11 , 20 , 2 , 28 ,  0  );
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , -1800);
    t2 = new Time(2004 , 11 , 20 , 2 ,  0 ,  0   );
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , -1801);
    t2 = new Time(2004 , 11 , 20 , 1 , 59 ,  59   );
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , 60);
    t2 = new Time(2004 , 11 , 20 , 2 , 31 ,  0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , 61);
    t2 = new Time(2004 , 11 , 20 , 2 , 31 ,  1);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , 119);
    t2 = new Time(2004 , 11 , 20 , 2 , 31 ,  59);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , 120);
    t2 = new Time(2004 , 11 , 20 , 2 , 32 ,   0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , 1800);
    t2 = new Time(2004 , 11 , 20 , 3 ,  0 ,    0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 30 , 1801);
    t2 = new Time(2004 , 11 , 20 , 3 ,  0 ,    1);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 59 , 60);
    t2 = new Time(2004 , 11 , 20 , 3 ,  0 ,  0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 59 , 120);
    t2 = new Time(2004 , 11 , 20 , 3 ,  1 ,   0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 60 , 60);
    t2 = new Time(2004 , 11 , 20 , 3 ,  1 ,  0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 60 , 3600);
    t2 = new Time(2004 , 11 , 20 , 4 ,  0 ,    0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 60 , -3600); //加一小時，減一小時
    t2 = new Time(2004 , 11 , 20 , 2 ,  0 ,    0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 60 , -7200); //加一小時，減兩小時
    t2 = new Time(2004 , 11 , 20 , 1 ,  0 ,    0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 2 , 60 , -7201); //加一小時，減兩小時又一秒
    t2 = new Time(2004 , 11 , 20 , 0 , 59 ,    59);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 24 , 0 , 0);
    t2 = new Time(2004 , 11 , 21 ,  0 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 25 , 0 , 0);
    t2 = new Time(2004 , 11 , 21 ,  1 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , 48 , 0 , 0);
    t2 = new Time(2004 , 11 , 22 ,  0 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 ,  -1 , 0 , 0);
    t2 = new Time(2004 , 11 , 19 ,  23 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , -13 , 0 , 0);
    t2 = new Time(2004 , 11 , 19 ,  11 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , -24 , 0 , 0);
    t2 = new Time(2004 , 11 , 19 ,   0 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , -25 , 0 , 0);
    t2 = new Time(2004 , 11 , 18 ,  23 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 20 , -25 , 0 , 0);
    t2 = new Time(2004 , 11 , 18 ,  23 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 30 ,  24 , 0 , 0);
    t2 = new Time(2004 , 12 ,  1 ,   0 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 30 ,  0 , 0 , 24*60*60);
    t2 = new Time(2004 , 12 ,  1 ,  0 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 30 ,  0 , 0 , 24*60*60*31);
    t2 = new Time(2004 , 12 , 31 ,  0 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 , 11 , 30 ,  0 , 0 , 24*60*60*32);
    t2 = new Time(2005 ,  1 ,  1 ,  0 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 ,  2 ,  1 ,  0  ,  0 , -60);
    t2 = new Time(2004 ,  1 , 31 ,  23 , 59 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 ,  2 ,  1 ,  0 , 0 , -60*60*24);
    t2 = new Time(2004 ,  1 , 31 ,  0 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 ,  2 ,  1 ,  0 , 0 , -60*60*24*31);
    t2 = new Time(2004 ,  1 ,  1 ,  0 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 ,  2  ,   1 ,  0 ,  0 , -60*60*24*31-1);
    t2 = new Time(2003 ,  12 ,  31 , 23 , 59 , 59);
    assertEquals(t2 , t1);

    t1 = new Time(2004 ,  2 ,  1 ,  0 , 0 , 60*60*24);
    t2 = new Time(2004 ,  2 ,  2 ,  0 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 ,  2 ,  1 ,   0 ,  0 , 60*60*24-1);
    t2 = new Time(2004 ,  2 ,  1 ,  23 , 59 , 59);
    assertEquals(t2 , t1);

    t1 = new Time(2004 ,  2 ,   1 ,  0 , 0 , 60*60*24*28);
    t2 = new Time(2004 ,  2 ,  29 ,  0 , 0 , 0);
    assertEquals(t2 , t1);

    t1 = new Time(2004 ,  2 ,   1 ,  0 , 0 , 60*60*24*29);
    t2 = new Time(2004 ,  3 ,   1 ,  0 , 0 , 0);
    assertEquals(t2 , t1);

    // ========================== 測試 julian gregorian cutover 的轉換 ==========================

    // cutover 當下
    t1 = new Time(true , 1582 , 10 ,  4 , 23 , 59 , 60);
    t2 = new Time(true , 1582 , 10 , 15 ,  0 ,  0 ,  0);
    assertEquals(t2 , t1);
    assertEquals(t2.getGmtJulDay() , t1.getGmtJulDay() , 0);

    t1 = new Time(true , 1582 , 10 , 15 ,  0 ,  0 ,  -1);
    t2 = new Time(true , 1582 , 10 ,  4 , 23 , 59 , 59);
    //assertEquals(t2 , t1);
    logger.info("t1 = {}" , t1);
    assertEquals(t2.getGmtJulDay() , t1.getGmtJulDay() , 0);
  }

  /**
   * https://docs.kde.org/trunk5/en/kdeedu/kstars/ai-julianday.html
   *
   * Julian day epoch 測試
   * 已知： epoch 位於
   * January 1, 4713 BC 中午 :  proleptic Julian calendar
   * November 24, 4714 BC    : proleptic Gregorian calendar
   */
  @Test
  public void testJulDayZero() {
    double startJul = Time.getGmtJulDay(false , false , 4713, 1 , 1 , 12 , 0 , 0);
    assertEquals(0 , startJul , 0.0);

    double startGre = Time.getGmtJulDay(false , true , 4714, 11 , 24 , 12 , 0 , 0);
    assertEquals(0 , startGre , 0.0);
  }

  /**
   * 已知
   * astro julian day number 開始於
   * November 24, 4714 BC 當天中午  : proleptic Gregorian calendar
   *
   * epoch (1970-01-01 0:00) 為 2440587.5
   */
  @Test
  public void test1970Epoch() {
    double value = Time.getGmtJulDay(true , true , 1970 , 1 , 1 , 0, 0, 0);
    assertEquals(2440587.5 , value , 0.0);

    value = Time.getGmtJulDay(LocalDateTime.of(1970 , 1 , 1 , 0 , 0 , 0));
    assertEquals(2440587.5 , value , 0.0);
  }

  /**
   * proleptic Gregorian
   */
  @Test
  public void test_getJulDayFromGregorian() {
    assertEquals(2457774, Time.getGmtJulDay(LocalDateTime.of(2017, 1, 20, 12, 0, 0)), 0.0);
    assertEquals(2457774, Time.getGmtJulDay(JulianDateTime.of(2017, 1, 7, 12, 0, 0)), 0.0);
  }

  /**
   * 測試 Julian Day 是否正確
   */
  @Test
  public void testGetGmtJulDay()
  {
    double actual;
    double expected;


    time = new Time(false , 4713 , 1 , 1 , 12 , 0 , 0);
    actual = time.getGmtJulDay();
    expected = 0;
    assertEquals(expected , actual , 0);

    time = new Time(true , 1900 , 1 , 1, 0 , 0 , 0);
    actual = time.getGmtJulDay();
    expected = 2415020.5;
    assertEquals(expected , actual , 0);

    time = new Time(2004, 11,20 , 12 , 0 , 0);
    actual = time.getGmtJulDay();
    expected = 2453330;
    assertEquals(expected , actual , 0);

    time = new Time(2004, 11,21 , 0 , 0 , 0);
    actual = time.getGmtJulDay();
    expected = 2453330.5;
    assertEquals(expected , actual , 0);

    time = new Time(2004, 11,21 , 12 , 0 , 0);
    actual = time.getGmtJulDay();
    expected = 2453331;
    assertEquals(expected , actual , 0);

    time = new Time(2002, 10, 1 , 12 , 0 , 0);
    actual = time.getGmtJulDay();
    expected = 2452549.0;
    assertEquals(expected , actual , 0);


    //=============================曆法分界期間 (Gregorian)==================
    time = new Time(1752, 9, 14 , 12 , 0 , 0);
    assertEquals(2361222 , time.getGmtJulDay() , 0);

    time = new Time(1752, 9, 13 , 12 , 0 , 0);
    assertEquals(2361221 , time.getGmtJulDay() , 0);

    time = new Time(1582, 10, 15 , 0 , 0 , 0);
    assertEquals(2299160.5 , time.getGmtJulDay() , 0);

    //=============================曆法分界期間 (Julian)==================
    time = new Time(true , 1752 , 9 , 3 , 0 , 0 , 0);
    assertEquals(2361210.5 , time.getGmtJulDay() , 0);

    time = new Time(true , 1752 , 9 , 2 , 0 , 0 , 0);
    assertEquals(2361209.5 , time.getGmtJulDay() , 0);

    time = new Time(true , 1582 , 10 , 15 , 0 , 0 , 0);
    assertEquals(2299160.5 , time.getGmtJulDay() , 0);

    //=========================== Julian 切換到 Gregorian ==============

    time = new Time(true , 1582 , 10 , 4 , 0 , 0 , 0);
    assertEquals(2299159.5 , time.getGmtJulDay() , 0);

    //=========================== 西元元年以及 前一年的分界 ===============
    time = new Time(true , 1 , 1 , 1 , 0 , 0 , 0);
    assertEquals(1721423.5 , time.getGmtJulDay() , 0);

    time = new Time(false , 1 , 12 , 31 , 0 , 0 , 0);
    assertEquals(1721422.5 , time.getGmtJulDay() , 0);

    time = new Time(false , 1 , 1 , 1 , 0 , 0 , 0);
    assertEquals(1721057.5 , time.getGmtJulDay() , 0);

    time = new Time(false , 1 , 1 , 1 , 0 , 0 , 0);
    assertEquals(1721057.5 , time.getGmtJulDay() , 0);

    time = new Time(false , 2 , 12 , 31 , 0 , 0 , 0);
    assertEquals(1721056.5 , time.getGmtJulDay() , 0);

    //===================西元 (J) 前 2637 年 2 月 2 日，甲子年？=============
    time = new Time( false , 2637 , 1 , 11 , 12 , 0 , 0);
    assertEquals(758269.0 , time.getGmtJulDay() , 0);

    time = new Time( false , 2637 , 2 , 2 , 12 , 0 , 0);
    assertEquals(758291.0 , time.getGmtJulDay() , 0);

    time = new Time(false , 2637 , 2 , 2 , 0 , 0 , 0);
    assertEquals(758290.5 , time.getGmtJulDay() , 0);


    time = new Time(false , 4713 , 1 , 1 , 12 , 0 , 0);
    assertEquals(0 , time.getGmtJulDay() , 0);
  }
  
  
  /**
   * 台灣
   * https://blog.yorkxin.org/2014/07/11/dst-in-taiwan-study
   */
  @Test
  public void getDstSecondOffset_Taiwan() throws Exception {
    Location loc = new Location(Locale.TAIWAN);

    // 民國41年（西元1952年）	日光節約時間	3月1日至10月31日
    int year = 1952;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 2, 28, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 3, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 31, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 11, 1, 12, 0), loc));

    //民國42年至43年（西元1953-1954年）	日光節約時間	4月1日至10月31日
    year = 1953;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 3, 31, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 4, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 31, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 11, 1, 12, 0), loc));

    year = 1954;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 3, 31, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 4, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 31, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 11, 1, 12, 0), loc));

    //民國44年至45年（西元1955-1956年）	日光節約時間	4月1日至9月30日
    year = 1955;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 3, 31, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 4, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 9, 30, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 1, 12, 0), loc));

    year = 1956;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 3, 31, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 4, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 9, 30, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 1, 12, 0), loc));

    // 民國46年至48年（西元1957-1959年）	夏令時間	4月1日至9月30日
    year = 1957;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 3, 31, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 4, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 9, 30, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 1, 12, 0), loc));

    year = 1958;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 3, 31, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 4, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 9, 30, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 1, 12, 0), loc));

    year = 1959;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 3, 31, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 4, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 9, 30, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 1, 12, 0), loc));


    // 民國49年至50年（西元1960-1961年）	夏令時間	6月1日至9月30日
    year = 1960;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 5, 31, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 6, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 9, 30, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 1, 12, 0), loc));

    year = 1961;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 5, 31, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 6, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 9, 30, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 1, 12, 0), loc));


    // 民國63年至64年（西元1974-1975年）	日光節約時間	4月1日至9月30日
    year = 1974;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 3, 30, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 4, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 9, 30, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 1, 12, 0), loc));

    year = 1975;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 3, 30, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 4, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 9, 30, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 1, 12, 0), loc));

    // 民國68年（西元1979年）	日光節約時間	7月1日至9月30日
    year = 1979;
    // 日光節約時間，前一天中午 , GMT+8
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 6, 30, 12, 0), loc));
    // 日光節約時間開始，當天中午 , 時區調快一小時
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 7, 1, 12, 0), loc));
    // 日光節約時間結束當天中午，時區仍是 +9
    assertEquals(Pair.of(TRUE, 9 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 9, 30, 12, 0), loc));
    // 日光節約時間結束 , 隔天中午，時區回到 +9
    assertEquals(Pair.of(FALSE, 8 * 60 * 60), getDstSecondOffset(LocalDateTime.of(year, 10, 1, 12, 0), loc));
  }
}
