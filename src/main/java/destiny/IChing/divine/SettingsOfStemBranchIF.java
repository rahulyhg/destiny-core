/**
 * @author smallufo
 * @date 2002/8/19
 * @time 下午 10:25:17
 */
package destiny.IChing.divine;

import destiny.IChing.HexagramIF;
import destiny.core.chinese.StemBranch;

/** 納甲法的設定介面 */
public interface SettingsOfStemBranchIF
{
  /**
   * 取得某個卦的某個爻的干支納甲
   * 1 <= LineIndex <=6
   */
  public StemBranch getStemBranch(HexagramIF hexagram , int LineIndex);
}
