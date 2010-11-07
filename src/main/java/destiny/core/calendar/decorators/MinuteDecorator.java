/**
 * @author smallufo 
 * Created on 2008/4/9 at 上午 11:56:28
 */ 
package destiny.core.calendar.decorators;

import java.util.Locale;

import com.google.common.collect.ImmutableMap;

import destiny.utils.Decorator;
import destiny.utils.LocaleUtils;

public class MinuteDecorator
{
  private final static ImmutableMap<Locale , Decorator<Integer>> implMap = new ImmutableMap.Builder<Locale , Decorator<Integer>>()
    .put(Locale.CHINESE , new MinuteDecoratorChinese())
    .put(Locale.ENGLISH , new MinuteDecoratorEnglish())
    .build();
  
  public static String getOutputString(int value ,  Locale locale)
  {
    Locale matched = LocaleUtils.getBestMatchingLocale(locale , implMap.keySet());
    if (matched == null)
      matched = (Locale) implMap.keySet().toArray()[0];
    return implMap.get(matched).getOutputString(value); 
  }
}