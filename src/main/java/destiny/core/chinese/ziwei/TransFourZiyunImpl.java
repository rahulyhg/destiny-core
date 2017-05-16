/**
 * Created by smallufo on 2017-04-12.
 */
package destiny.core.chinese.ziwei;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import destiny.core.chinese.Stem;

import java.util.Locale;

import static destiny.core.chinese.Stem.*;
import static destiny.core.chinese.ziwei.ITransFour.Value.*;
import static destiny.core.chinese.ziwei.StarLucky.*;
import static destiny.core.chinese.ziwei.StarMain.*;

/**
 * 紫雲派
 */
public class TransFourZiyunImpl extends TransFourAbstractImpl {

  private final static Table<Stem , Value , ZStar> table = new ImmutableTable.Builder<Stem, Value, ZStar>()
    .put(甲 , 祿 , 廉貞)
    .put(甲 , 權 , 破軍)
    .put(甲 , 科 , 武曲)
    .put(甲 , 忌 , 太陽)

    .put(乙 , 祿 , 天機)
    .put(乙 , 權 , 天梁)
    .put(乙 , 科 , 紫微)
    .put(乙 , 忌 , 太陰)

    .put(丙 , 祿 , 天同)
    .put(丙 , 權 , 天機)
    .put(丙 , 科 , 文昌)
    .put(丙 , 忌 , 廉貞)

    .put(丁 , 祿 , 太陰)
    .put(丁 , 權 , 天同)
    .put(丁 , 科 , 天機)
    .put(丁 , 忌 , 巨門)

    // 戊 有差別
    .put(戊 , 祿 , 貪狼)
    .put(戊 , 權 , 太陰)
    .put(戊 , 科 , 右弼)
    .put(戊 , 忌 , 天機)

    .put(己 , 祿 , 武曲)
    .put(己 , 權 , 貪狼)
    .put(己 , 科 , 天梁)
    .put(己 , 忌 , 文曲)

    // 庚 有差別
    .put(庚 , 祿 , 太陽)
    .put(庚 , 權 , 武曲)
    .put(庚 , 科 , 太陰)
    .put(庚 , 忌 , 天同)

    .put(辛 , 祿 , 巨門)
    .put(辛 , 權 , 太陽)
    .put(辛 , 科 , 文曲)
    .put(辛 , 忌 , 文昌)

    // 壬 有差別
    .put(壬 , 祿 , 天梁)
    .put(壬 , 權 , 紫微)
    .put(壬 , 科 , 左輔)
    .put(壬 , 忌 , 武曲)

    // 「紫雲派」，與「全集」的差別，就只有「癸干：破巨陽貪」
    .put(癸 , 祿 , 破軍)
    .put(癸 , 權 , 巨門)
    .put(癸 , 科 , 太陽)  // 唯一與「全集」不同之處
    .put(癸 , 忌 , 貪狼)

    .build();

  @Override
  protected Table<Stem, Value, ZStar> getTable() {
    return table;
  }

  @Override
  public String getDescription(Locale locale) {
    return "紫雲派";
  }

}
