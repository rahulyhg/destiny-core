/**
 * @author smallufo 
 * Created on 2007/12/31 at 下午 10:42:30
 */ 
package destiny.astrology.classical.rules.debilities;

import destiny.astrology.*;
import destiny.astrology.beans.BesiegedBean;
import destiny.astrology.classical.DebilitiesIF;
import destiny.astrology.classical.RefranationIF;
import destiny.astrology.classical.rules.RuleIF;
import org.jetbrains.annotations.NotNull;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DebilitiesBean implements DebilitiesIF , Serializable
{
  /** 計算兩星交角的介面 , 內定採用 SwissEph 的實作 */
  @Inject
  private RelativeTransitIF relativeTransitImpl;
  
  /** 計算白天黑夜的實作 , 內定採用 SwissEph 的實作 */
  @Inject
  private DayNightDifferentiator dayNightImpl;
  
  /** 判斷入相位，或是出相位 的實作 , 內定採用古典占星 */
  @Inject
  private AspectApplySeparateIF aspectApplySeparateImpl;

  @Inject
  private BesiegedBean besiegedBean;

  private final RefranationIF refranationImpl;

  private List<Applicable> rules = new ArrayList<Applicable>();


  public DebilitiesBean(RefranationIF refranationImpl)
  {
    this.refranationImpl = refranationImpl;
  }

  @PostConstruct
  public void init() {
    this.rules = getDefaultRules();
  }


  @NotNull
  @Override
  public List<RuleIF> getDebilities(Planet planet, HoroscopeContext horoscopeContext)
  {
    return rules.stream().filter(each -> each.isApplicable(planet, horoscopeContext)).collect(Collectors.toList());
  }

  
  @NotNull
  private List<Applicable> getDefaultRules()
  {
    List<Applicable> list = new ArrayList<Applicable>();
    list.add(new Detriment());
    list.add(new Fall());
    list.add(new Peregrine(dayNightImpl));
    list.add(new House_12());
    list.add(new House_6_8());
    list.add(new Retrograde());
    list.add(new Slower());
    list.add(new Occidental());
    list.add(new Oriental());
    list.add(new Moon_Decrease_Light());
    list.add(new Combustion());
    list.add(new Sunbeam());
    list.add(new Partile_Conj_Mars_Saturn());
    list.add(new Partile_Conj_South_Node());
    list.add(new Besieged_Mars_Saturn(besiegedBean));
    list.add(new Partile_Oppo_Mars_Saturn());
    list.add(new Partile_Square_Mars_Saturn());
    list.add(new Conj_Algol());
    list.add(new Out_of_Sect(dayNightImpl));
    list.add(new MutualDeception(dayNightImpl));
    list.add(new Refrain_from_Venus_Jupiter(refranationImpl));
    return list;
  }

  public List<Applicable> getRules()
  {
    return this.rules;
  }
  
  public void setRules(List<Applicable> rules)
  {
    this.rules = rules;
  }


}
