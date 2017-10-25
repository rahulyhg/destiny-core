/**
 * Created by smallufo on 2014-11-30.
 */
package destiny.core.calendar.eightwords.personal;

import destiny.core.calendar.JulDayResolver1582CutoverImpl;
import destiny.core.calendar.SolarTerms;
import destiny.core.calendar.TimeSecDecoratorChinese;
import destiny.core.calendar.TimeTools;
import destiny.core.calendar.eightwords.ContextColorCanvasWrapper;
import destiny.core.calendar.eightwords.Direction;
import destiny.core.calendar.eightwords.EightWords;
import destiny.core.calendar.eightwords.Reactions;
import destiny.core.chinese.StemBranch;
import destiny.tools.ColorCanvas.AlignUtil;
import destiny.tools.ColorCanvas.ColorCanvas;
import destiny.tools.Decorator;
import org.jetbrains.annotations.NotNull;
import org.jooq.lambda.tuple.Tuple2;

import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class PersonContextColorCanvasWrapper extends ContextColorCanvasWrapper {

  private final PersonContext personContext;

  /** 預先儲存已經計算好的結果 */
  private final PersonContextModel model;

  /** 地支藏干的實作，內定採用標準設定 */
  private final HiddenStemsIF hiddenStemsImpl;

  public enum OutputMode {HTML , TEXT}

  private OutputMode outputMode = OutputMode.HTML;

  private final Decorator<ChronoLocalDateTime> timeDecorator = new TimeSecDecoratorChinese();

  private final Direction direction;

  private final static Function<Double , ChronoLocalDateTime> revJulDayFunc = JulDayResolver1582CutoverImpl::getLocalDateTimeStatic;

  public PersonContextColorCanvasWrapper(PersonContext personContext, @NotNull PersonContextModel model, String locationName, HiddenStemsIF hiddenStemsImpl, String linkUrl, Direction direction) {
    super(personContext, model.getLmt() , model.getLocation() , locationName , hiddenStemsImpl , linkUrl, direction);
    this.personContext = personContext;
    this.model = model;
    this.hiddenStemsImpl = hiddenStemsImpl;
    this.direction = direction;
  }

  public void setOutputMode(OutputMode mode)
  {
    this.outputMode = mode;
  }

  /** 取得八字命盤 */
  @NotNull
  @Override
  public String toString() {
    ColorCanvas cc = new ColorCanvas(32, 70, "　");

    ColorCanvas metaDataColorCanvas = getMetaDataColorCanvas();

    cc.add(metaDataColorCanvas , 1 , 1); // 國曆 農曆 經度 緯度 短網址 等 MetaData

    cc.setText("性別：", 1, 59);
    cc.setText(model.getGender().toString() , 1, 65); // '男' or '女'
    cc.setText("性" , 1 , 67);

    cc.setText("八字：" , 10 , 1);

    EightWords eightWords = model.getEightWords();

    ReactionsUtil reactionsUtil = new ReactionsUtil(this.hiddenStemsImpl);

    cc.add(getEightWordsColorCanvas() , 11 , 9); // 純粹八字盤


    ColorCanvas 右方大運直 = new ColorCanvas(9,24,"　" );
    ColorCanvas 下方大運橫 = new ColorCanvas(8,70,"　" , Optional.empty() , Optional.empty());

    List<FortuneData> dataList = new ArrayList<>(model.getFortuneDatas());

    for (int i=1 ; i <= dataList.size() ; i++) {
      FortuneData fortuneData = dataList.get(i-1);
      int startFortune = fortuneData.getStartFortuneAge();
      int   endFortune = fortuneData.getEndFortuneAge();
      StemBranch stemBranch = fortuneData.getStemBranch();

      ChronoLocalDateTime startFortuneLmt = TimeTools.getLmtFromGmt(fortuneData.getStartFortuneGmtJulDay(),  personContext.getLocation() , revJulDayFunc);
      ChronoLocalDateTime   endFortuneLmt = TimeTools.getLmtFromGmt(  fortuneData.getEndFortuneGmtJulDay() , personContext.getLocation() , revJulDayFunc);

      右方大運直.setText(AlignUtil.alignRight(startFortune, 6) , i , 1 , "green" , null , "起運時刻：" + timeDecorator.getOutputString(startFortuneLmt));
      右方大運直.setText("→" , i , 9 , "green" );
      右方大運直.setText(AlignUtil.alignRight(endFortune , 6) , i , 13 , "green" , null , "終運時刻：" + timeDecorator.getOutputString(endFortuneLmt));
      右方大運直.setText(stemBranch.toString() , i , 21 , "green");
    }


    if (direction == Direction.R2L) {
      Collections.reverse(dataList);
    }

    for (int i=1 ; i <= dataList.size() ; i++) {
      FortuneData fortuneData = dataList.get(i-1);
      int startFortune = fortuneData.getStartFortuneAge();
      StemBranch stemBranch = fortuneData.getStemBranch();
      ChronoLocalDateTime startFortuneLmt = TimeTools.getLmtFromGmt(fortuneData.getStartFortuneGmtJulDay() , personContext.getLocation() , revJulDayFunc);

      下方大運橫.setText(AlignUtil.alignCenter(startFortune , 6) , 1 , (i-1)*8+1 , "green" , null , "起運時刻：" + timeDecorator.getOutputString(startFortuneLmt));
      Reactions reaction = reactionsUtil.getReaction(stemBranch.getStem() , eightWords.getDay().getStem());
      下方大運橫.setText(reaction.toString().substring(0, 1), 2, (i-1)*8+3, "gray");
      下方大運橫.setText(reaction.toString().substring(1,2) , 3 , (i-1)*8+3 , "gray");
      下方大運橫.setText(stemBranch.getStem()  .toString() , 4 , (i-1)*8+3 , "red");
      下方大運橫.setText(stemBranch.getBranch().toString(), 5, (i-1)*8+3, "red");
      下方大運橫.add(地支藏干(stemBranch.getBranch() , eightWords.getDay().getStem()) , 6 , (i-1)*8+1);
    }



    // 2017-10-25 起，右邊大運固定顯示虛歲
    cc.setText("大運（"+ model.getFortuneOutput() +"）", 10, 55);
    cc.add(右方大運直, 11, 47);
    cc.add(下方大運橫, 22, 1);

    ColorCanvas 節氣 = new ColorCanvas(2 , cc.getWidth() ,  "　");
    SolarTerms prevMajorSolarTerms = model.getPrevMajorSolarTerms();
    SolarTerms nextMajorSolarTerms = model.getNextMajorSolarTerms();

    Tuple2<Integer , Integer> pair1 = TimeTools.splitSecond(personContext.getTargetMajorSolarTermsSeconds(-1));
    ChronoLocalDateTime prevMajorSolarTermsTime = model.getLmt().plus(pair1.v1() , ChronoUnit.SECONDS).plus(pair1.v2() , ChronoUnit.NANOS);
    //LocalDateTime prevMajorSolarTermsTime =LocalDateTime.from(model.getLmt()).plusSeconds(pair1.v1()).plusNanos(pair1.v2());

    節氣.setText(prevMajorSolarTerms.toString() , 1 , 1);
    節氣.setText("：" , 1, 5);
    節氣.setText(this.timeDecorator.getOutputString(prevMajorSolarTermsTime) , 1,7);

    Tuple2<Integer , Integer> pair2 = TimeTools.splitSecond(personContext.getTargetMajorSolarTermsSeconds(1));
    ChronoLocalDateTime nextMajorSolarTermsTime = model.getLmt().plus(pair2.v1() , ChronoUnit.SECONDS).plus(pair2.v2() , ChronoUnit.NANOS);
    //LocalDateTime nextMajorSolarTermsTime =LocalDateTime.from(model.getLmt()).plusSeconds(pair2.v1()).plusNanos(pair2.v2());
    節氣.setText(nextMajorSolarTerms.toString() , 2 , 1);
    節氣.setText("：" , 2, 5);
    節氣.setText(this.timeDecorator.getOutputString(nextMajorSolarTermsTime) , 2,7);

    cc.add(節氣 , 31 , 1);

    switch(this.outputMode)
    {
      case TEXT:
        return cc.toString();
      case HTML:
        return cc.getHtmlOutput();
      default:
        return cc.getHtmlOutput();
    }
  } // toString()

}
