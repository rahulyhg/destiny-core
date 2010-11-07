/**
 * @author smallufo 
 * Created on 2007/12/31 at 上午 3:30:05
 */ 
package destiny.astrology.classical.rules.debilities;

import java.util.Locale;

import destiny.astrology.Aspect;
import destiny.astrology.Horoscope;
import destiny.astrology.HoroscopeContext;
import destiny.astrology.LunarNode;
import destiny.astrology.NodeType;
import destiny.astrology.Planet;

/** Partile conjunction with Dragon's Tail (Moon's South Node). */
public final class Partile_Conj_South_Node extends Rule
{
  /** 內定採用 NodeType.MEAN */
  private NodeType nodeType = NodeType.MEAN;
  
  
  public Partile_Conj_South_Node()
  {
    super("Partile_Conj_South_Node");
  }

  @Override
  public boolean isApplicable(Planet planet, HoroscopeContext horoscopeContext)
  {
    double planetDegree = horoscopeContext.getPosition(planet).getLongitude();
    double southDeg;
    if (nodeType == NodeType.TRUE)
      southDeg = horoscopeContext.getPosition(LunarNode.SOUTH_TRUE).getLongitude();
    else
      southDeg = horoscopeContext.getPosition(LunarNode.SOUTH_MEAN).getLongitude();
    
    if ( Horoscope.getAngle(planetDegree , southDeg) <= 1)
    {
      if (nodeType == NodeType.TRUE)
      {
        addComment(Locale.TAIWAN , planet + " 與 " + LunarNode.SOUTH_TRUE + " 形成 " + Aspect.CONJUNCTION);
        return true;
      }
      else
      {
        addComment(Locale.TAIWAN , planet + " 與 " + LunarNode.SOUTH_MEAN + " 形成 " + Aspect.CONJUNCTION);
        return true;
      }
    }
    return false;
  }

  public NodeType getNodeType()
  {
    return nodeType;
  }

  public void setNodeType(NodeType nodeType)
  {
    this.nodeType = nodeType;
  }

}