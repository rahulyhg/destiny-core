/**
 * Created by smallufo on 2017-10-23.
 */
package destiny.core;

import destiny.core.calendar.JulDayResolver1582CutoverImpl;
import org.jooq.lambda.tuple.Tuple2;

import java.io.Serializable;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Optional;
import java.util.function.Function;

public class IntAgeNoteWestYearImpl implements IntAgeNote , Serializable {

  private final static Function<Double , ChronoLocalDateTime> revJulDayFunc = JulDayResolver1582CutoverImpl::getLocalDateTimeStatic;

  @Override
  public Optional<String> getAgeNote(Tuple2<Double, Double> startAndEnd) {
    ChronoLocalDateTime start = revJulDayFunc.apply(startAndEnd.v1());
    return Optional.of(String.valueOf(start.get(ChronoField.YEAR_OF_ERA)));
  }
}