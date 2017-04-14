/**
 * Created by smallufo on 2017-04-12.
 */
package destiny.core.chinese.ziwei;

public enum FuncType {
  SET_DAY_NUM,   // (局數,生日)
  HOUR_BRANCH,
  MONTH_BRANCH,
  YEAR_STEM,
  YEAR_BRANCH,
  YEAR_BRANCH_HOUR_BRANCH,  //  (年支,時支）
  YEAR_BRANCH_MONTH_BRANCH, //  (年支,月支) 天馬專用 (年馬 or 月馬)
  MONTH_BRANCH_DAY_NUM,     //  (月支,日數)
  DAY_NUM_HOUR_BRANCH,      //  (時支,日數)
  YEAR_BRANCH_MONTH_NUM_HOUR_BRANCH // (年支,月數,時支)
  ;

  FuncType() {}
}
