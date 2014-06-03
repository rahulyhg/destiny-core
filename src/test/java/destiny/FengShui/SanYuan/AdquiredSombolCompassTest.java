/**
 * @author smallufo
 * @date 2002/9/23
 * @time 下午 07:52:20
 */
package destiny.FengShui.SanYuan;

import junit.framework.TestCase;
import destiny.iching.Symbol;

public class AdquiredSombolCompassTest extends TestCase
{
  public void testAdquiredSymbolCompass()
  {
    AdquiredSymbolCompass asc = new AdquiredSymbolCompass();

    assertEquals(337.5 , asc.getStartDegree(Symbol.坎));
    assertEquals(22.5 , asc.getStartDegree(Symbol.艮));

    assertSame(Symbol.坎 , asc.getSymbol(0));
    assertSame(Symbol.艮 , asc.getSymbol(30));
    assertSame(Symbol.離 , asc.getSymbol(180));
    assertSame(Symbol.兌 , asc.getSymbol(263));
    assertSame(Symbol.乾 , asc.getSymbol(337));
    assertSame(Symbol.坎 , asc.getSymbol(345));
    assertSame(Symbol.坎 , asc.getSymbol(359));

  }
}