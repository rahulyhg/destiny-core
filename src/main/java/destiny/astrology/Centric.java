/**
 * @author smallufo 
 * Created on 2007/12/10 at 上午 10:30:01
 */ 
package destiny.astrology;

import java.util.Locale;
import java.util.ResourceBundle;

import destiny.utils.LocaleStringIF;

/** 中心系統 */
public enum Centric implements LocaleStringIF
{
  /** 地心 */
  GEO("Centric.GEO"),
  /** 日心 */
  HELIO("Centric.HELIO"),
  /** 地表 */
  TOPO ("Centric.TOPO"),
  /** 質心 */
  BARY ("Centric.BARY");
  
  private final static String resource = "destiny.astrology.Astrology";
  
  private String nameKey;
  
  private Centric(String nameKey)
  {
    this.nameKey = nameKey;
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