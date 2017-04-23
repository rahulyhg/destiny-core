/**
 * Created by smallufo on 2017-04-23.
 */
package destiny.core.calendar;

import com.google.common.collect.ImmutableMap;
import destiny.tools.Decorator;
import destiny.tools.LocaleUtils;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Locale;

/** 輸出到「秒」 */
public class TimeSecDecorator {
  private final static ImmutableMap<Locale , Decorator<LocalDateTime>> implMap = new ImmutableMap.Builder<Locale , Decorator<LocalDateTime>>()
    .put(Locale.TAIWAN , new TimeSecDecoratorChinese())
    .put(Locale.ENGLISH , new TimeSecDecoratorEnglish())
    .put(Locale.JAPAN, new TimeSecDecoratorJapanese())
    .build();

  @NotNull
  public static String getOutputString(LocalDateTime time , Locale locale) {
    return implMap.get(
      LocaleUtils.getBestMatchingLocale(locale, implMap.keySet()).orElse((Locale) implMap.keySet().toArray()[0])
    ).getOutputString(time);
  }
}
