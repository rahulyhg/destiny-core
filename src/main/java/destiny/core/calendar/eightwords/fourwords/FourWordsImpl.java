/** 2009/10/12 上午3:22:12 by smallufo */
package destiny.core.calendar.eightwords.fourwords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import destiny.core.calendar.eightwords.EightWords;
import destiny.core.chinese.EarthlyBranches;
import destiny.core.chinese.HeavenlyStems;

/** 讀取 1440.txt */
public class FourWordsImpl implements FourWordsIF , Serializable 
{
  private final static Map<FourWordsKey , String> map = Collections.synchronizedMap(new HashMap<FourWordsKey , String>());
  static
  {
    InputStream is = FourWordsImpl.class.getResourceAsStream("1440.txt");
    Reader reader;
    BufferedReader bReader = null;
    try
    {
      reader = new InputStreamReader(is , "UTF-8");
      bReader = new BufferedReader(reader);
      
      String line = null;
      while ((line = bReader.readLine()) != null)
      {
        StringTokenizer st = new StringTokenizer(line , " ");
        String first = st.nextToken();
        
        // TODO : 去掉 BOM Header , 完整的做法應該參考這裡 http://samsharehome.blogspot.com/2009/01/utf-8-bominputstreamreader.html
        HeavenlyStems dayStem = HeavenlyStems.getHeavenlyStems(first.length() == 1 ? first.charAt(0) : first.charAt(1));
        EarthlyBranches monthBranch = EarthlyBranches.valueOf(st.nextToken());
        EarthlyBranches hourBranch = EarthlyBranches.valueOf(st.nextToken());
        String value = st.nextToken();
        
        FourWordsKey key = new FourWordsKey(dayStem, monthBranch, hourBranch);
        map.put(key, value);
      }
    }
    catch (UnsupportedEncodingException e1)
    {
      e1.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      try
      {
        bReader.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  } // static
  
  public FourWordsImpl()
  {
  }

  @Override
  public String getResult(EightWords ew)
  {
    FourWordsKey key = new FourWordsKey(ew.getDayStem(), ew.getMonthBranch(), ew.getHourBranch());
    return map.get(key);
  }

}
