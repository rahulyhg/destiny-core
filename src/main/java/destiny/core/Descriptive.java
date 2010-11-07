/**
 * @author smallufo 
 * Created on 2006/5/5 at 上午 04:20:08
 */ 
package destiny.core;

import java.util.Locale;

public interface Descriptive
{
  /** 取得名稱 */
  public String getTitle(Locale locale);
  
  /** 詳細描述 */
  public String getDescription(Locale locale);
}