/** 2009/7/13 上午 3:40:47 by smallufo */
package destiny.IChing.contentProviders;

import java.util.Locale;

import destiny.IChing.HexagramIF;

/** 取得全名，例如「乾為天」 */
public interface HexagramNameFullIF
{
  /** 取得全名，例如「乾為天」 */
  public String getNameFull(HexagramIF hexagram , Locale locale);
}

