/**
 * Created by smallufo on 2017-10-23.
 */
package destiny.core;

import org.jooq.lambda.tuple.Tuple2;

import java.util.Optional;

/**
 * 將 {@link IntAge} 計算出來的結果 Tuple2[GMT , GMT] 附註年份
 * 例如，西元年份、或是民國年份、或是中國歷史紀元
 */
public interface IntAgeNote {

  Optional<String> getAgeNote(Tuple2<Double , Double> startAndEnd);
}