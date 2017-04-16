/**
 * Created by smallufo on 2017-04-15.
 */
package destiny.core.chinese.ziwei;

import destiny.core.Gender;
import destiny.core.chinese.Branch;
import destiny.core.chinese.Stem;
import org.jooq.lambda.tuple.Tuple3;

/**
 * 與某宮位形成固定關係 的排列方式
 * 目前只有 {@link StarMinor#天傷} , {@link StarMinor#天使} 會用到
 * 實作於 {@link StarMinor#fun天傷_陽順陰逆} , {@link StarMinor#fun天使_陽順陰逆}
 * */
public abstract class HouseHouseDepYearStemGenderImpl extends HouseAbstractImpl<Tuple3<Branch , Stem, Gender>> {

  protected HouseHouseDepYearStemGenderImpl(ZStar star) {
    super(star);
  }

  @Override
  public FuncType getFuncType() {
    return FuncType.HOUSE_DEP;
  }

  @Override
  public Branch getBranch(Tuple3<Branch , Stem, Gender> o) {
    throw new RuntimeException("error");
  }

}