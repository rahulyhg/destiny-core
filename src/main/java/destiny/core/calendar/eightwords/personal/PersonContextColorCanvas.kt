/**
 * Created by smallufo on 2014-11-30.
 */
package destiny.core.calendar.eightwords.personal

import destiny.core.calendar.JulDayResolver1582CutoverImpl
import destiny.core.calendar.TimeSecDecoratorChinese
import destiny.core.calendar.TimeTools
import destiny.core.calendar.eightwords.Direction
import destiny.core.calendar.eightwords.EightWordsColorCanvas
import destiny.core.calendar.eightwords.IEightWordsContextModel
import destiny.core.calendar.eightwords.TimeLine
import destiny.core.chinese.NaYin
import destiny.tools.ChineseStringTools
import destiny.tools.canvas.ColorCanvas
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter
import java.util.*

class PersonContextColorCanvas(private val personContext: IPersonContext,
                               /** 預先儲存已經計算好的結果  */
                               private val model: IPersonPresentModel,
                               /** 地支藏干的實作，內定採用標準設定  */
                               private val hiddenStemsImpl: IHiddenStems,
                               private val linkUrl: String,
                               private val direction: Direction,
                               /** 是否顯示納音 */
                               private val showNaYin: Boolean = false) :
  ColorCanvas(36, 70, ChineseStringTools.NULL_CHAR)
//  ColorCanvas(33, 70, ChineseStringTools.NULL_CHAR)
{

  var outputMode = ColorCanvas.OutputMode.HTML

  private val ewContextColorCanvas: EightWordsColorCanvas by lazy {
    val m: IEightWordsContextModel = personContext.getEightWordsContextModel(model.lmt, model.location, model.place)
    EightWordsColorCanvas(m, personContext, model.place ?: "", hiddenStemsImpl, linkUrl, direction, showNaYin)
  }


  private val timeDecorator = TimeSecDecoratorChinese()

  private val monthFormatter = DateTimeFormatter.ofPattern("MM月")
  private val monthDayFormatter = DateTimeFormatter.ofPattern("MMdd")


  init {
    val metaDataColorCanvas = ewContextColorCanvas.metaDataColorCanvas
    add(metaDataColorCanvas, 1, 1) // 國曆 農曆 經度 緯度 短網址 等 MetaData

    setText("性別：", 1, 59)
    setText(model.gender.toString(), 1, 65) // '男' or '女'
    setText("性", 1, 67)

    setText("八字：", 10, 1)

    val eightWords = model.eightWords

    val reactionsUtil = ReactionUtil(this.hiddenStemsImpl)

    add(ewContextColorCanvas.eightWordsColorCanvas, 11, 9) // 純粹八字盤


    val 右方大運直 = ColorCanvas(9, 24, ChineseStringTools.NULL_CHAR)
    val 下方大運橫 = ColorCanvas(10, 70, ChineseStringTools.NULL_CHAR)

    val dataList = model.fortuneDataLarges.toMutableList() // ArrayList(model.fortuneDataLarges)
    logger.debug("dataList size = {}", dataList.size)

    // 右方大運直 (限定9柱)
    for (i in 1..9) {

      val fortuneData = dataList[i - 1]
      val selected = fortuneData.stemBranch == model.selectedFortuneLarge
      val startFortune = ChineseStringTools.toBiggerDigits(fortuneData.startFortuneAge).let {
        if (selected)
          "★$it"
        else
          it
      }
      val endFortune = ChineseStringTools.toBiggerDigits(fortuneData.endFortuneAge)
      val stemBranch = fortuneData.stemBranch


      val startFortuneLmt = TimeTools.getLmtFromGmt(fortuneData.startFortuneGmtJulDay, model.location, revJulDayFunc)
      val endFortuneLmt = TimeTools.getLmtFromGmt(fortuneData.endFortuneGmtJulDay, model.location, revJulDayFunc)

      val bgColor = if (selected) "CCC" else null

      val row = ColorCanvas(1, 24, ChineseStringTools.NULL_CHAR, null, bgColor)

      row.setText(ChineseStringTools.alignRight(startFortune, 6), 1, 1, "green", null,
                  "起運時刻：" + timeDecorator.getOutputString(startFortuneLmt))
      row.setText("→", 1, 9, "green", null, null)
      row.setText(ChineseStringTools.alignRight(endFortune, 6), 1, 13, "green", null,
                  "終運時刻：" + timeDecorator.getOutputString(endFortuneLmt))
      row.setText(stemBranch.toString(), 1, 21, "green")
      右方大運直.add(row, i, 1)
    }


    if (direction == Direction.R2L) {
      dataList.reverse()
    }


    val ageNoteImpls = personContext.ageNoteImpls

    // 下方大運橫
    if (dataList.size == 9) {
      // 完整九條大運 , 每條 height=10 , width = 6
      for (i in 1..dataList.size) {
        val fortuneData = dataList[i - 1]
        val selected = fortuneData.stemBranch == model.selectedFortuneLarge

        val startFortune: String =
          ageNoteImpls.map { it -> it.getAgeNote(fortuneData.startFortuneGmtJulDay) }
            .filter { it !== null }
            .map { it -> it!! }
            .first()

        val stemBranch = fortuneData.stemBranch
        val startFortuneLmt = TimeTools.getLmtFromGmt(fortuneData.startFortuneGmtJulDay, model.location, revJulDayFunc)

        val bgColor = if (selected) "DDD" else null
        val triColumn = ColorCanvas(10, 6, ChineseStringTools.NULL_CHAR, null, bgColor)

        triColumn.setText(StringUtils.center(startFortune, 6, ' '), 1, 1, "green", null,
                          "起運時刻：" + timeDecorator.getOutputString(startFortuneLmt))
        // 加上月
        val monthDay = startFortuneLmt.toLocalDate().let { value ->
          monthFormatter.format(value)
        }
        triColumn.setText(StringUtils.center(monthDay, 6, ' '), 2, 1, "green", null, null)
        if (showNaYin) {
          NaYin.getNaYin(stemBranch.stem, stemBranch.branch)?.also { naYin ->
            val name = naYin.name
            triColumn.setText(name[0].toString(), 4, 5, "plum")
            triColumn.setText(name[1].toString(), 5, 5, "plum")
            triColumn.setText(name[2].toString(), 6, 5, "plum")
          }
        }

        val reaction = reactionsUtil.getReaction(stemBranch.stem, eightWords.day.stem)
        triColumn.setText(reaction.toString().substring(0, 1), 3, 3, "gray")
        triColumn.setText(reaction.toString().substring(1, 2), 4, 3, "gray")
        triColumn.setText(stemBranch.stem.toString(), 5, 3, "red")
        triColumn.setText(stemBranch.branch.toString(), 6, 3, "red")
        triColumn.add(ewContextColorCanvas.地支藏干(stemBranch.branch, eightWords.day.stem), 7, 1)

        下方大運橫.add(triColumn, 1, (i - 1) * 8 + 1)
      } // 1~9
    } else if (dataList.size == 18) {
      // 18條大運 , 分成上下兩條 , 每條 height=5 , width = 6
      for (i in 1..9) {
        for (j in 1..2) {
          val index = (2 * (i - 1) + j - 1).let {
            if (direction == Direction.L2R)
              it
            else {
              // 交換上下兩列
              if (j==1)
                it+1
              else
                it-1
            }
          }
          val fortuneData = dataList[index]

          val selected = fortuneData.stemBranch == model.selectedFortuneLarge
          val startFortune: String =
            ageNoteImpls.map { it -> it.getAgeNote(fortuneData.startFortuneGmtJulDay) }
              .filter { it !== null }
              .map { it -> it!! }
              .first()
          val stemBranch = fortuneData.stemBranch
          val startFortuneLmt =
            TimeTools.getLmtFromGmt(fortuneData.startFortuneGmtJulDay, model.location, revJulDayFunc)

          val bgColor = if (selected) "DDD" else null
          val triColumnShort = ColorCanvas(5, 6, ChineseStringTools.NULL_CHAR, null, bgColor)
          //triColumnShort.setText(i.toString()+"/"+j.toString()+":"+dataList.indexOf(fortuneData) , 1 , 1)

          //val title = AlignTools.leftPad(startFortune, 6 , ChineseStringTools.NULL_CHAR.get(0))
          val title = StringUtils.center(startFortune, 6, ' ')
          //val title = ChineseStringTools.replaceToBiggerDigits(startFortune)
          //val title = StringUtils.rightPad(startFortune, 6, ChineseStringTools.NULL_CHAR)
          triColumnShort.setText(title, 1, 1, "green", null, "起運時刻：" + timeDecorator.getOutputString(startFortuneLmt))

          val reaction = reactionsUtil.getReaction(stemBranch.stem, eightWords.day.stem)
          triColumnShort.setText(reaction.getAbbreviation(Locale.TAIWAN) , 2 , 3 , "gray")
          triColumnShort.setText(stemBranch.stem.toString() , 3 , 3 , "red")
          triColumnShort.setText(stemBranch.branch.toString() , 4 , 3 , "red")
          // 地支藏干
          val reactions = reactionsUtil.getReactions(stemBranch.branch, eightWords.day.stem)
          for (k in reactions.indices) {
            val eachReaction = reactions[k]
            triColumnShort.setText(ReactionUtil.getStem(eightWords.day.stem, eachReaction).toString(), 5, 5 - 2 * k, "gray") // 天干
          }
          //triColumnShort.setText("XX" , 5 , 3 , "gray")
          val (x, y) = ((j - 1) * 5 + 1) to ((i - 1) * 8 + 1)
          //println("i = $i , j = $j , x = $x , y = $y")
          下方大運橫.add(triColumnShort, x, y)
        } // 1~2
      } // 1~ (18/2=9)

    } // 18條大運 , 分成上下兩條 , 每條 height=5 , width = 6


    // 2017-10-25 起，右邊大運固定顯示虛歲
    setText("大運（虛歲）", 10, 55)
    add(右方大運直, 11, 47)
    add(下方大運橫, 22, 1)

//    val 節氣 = ColorCanvas(2, width, ChineseStringTools.NULL_CHAR)
//    val prevMajorSolarTerms: Pair<SolarTerms, Double> = model.prevMajorSolarTerms
//    val nextMajorSolarTerms: Pair<SolarTerms, Double> = model.nextMajorSolarTerms
//
//    val prevMajorSolarTermsTime = TimeTools.getLmtFromGmt(prevMajorSolarTerms.second, model.location, revJulDayFunc)
//
//    節氣.setText(prevMajorSolarTerms.first.toString(), 1, 1)
//    節氣.setText("：", 1, 5)
//    節氣.setText(this.timeDecorator.getOutputString(prevMajorSolarTermsTime), 1, 7)
//
//    val nextMajorSolarTermsTime = TimeTools.getLmtFromGmt(nextMajorSolarTerms.second, model.location, revJulDayFunc)
//    節氣.setText(nextMajorSolarTerms.first.toString(), 2, 1)
//    節氣.setText("：", 2, 5)
//    節氣.setText(this.timeDecorator.getOutputString(nextMajorSolarTermsTime), 2, 7)
//
//    add(節氣, 32, 1)

    add(TimeLine(model) , 32 , 1)
  }

  /** 取得八字命盤  */
  override fun toString(): String {
    return when (outputMode) {
      ColorCanvas.OutputMode.TEXT -> getTextOutput()
      ColorCanvas.OutputMode.HTML -> htmlOutput
    }
  }

  companion object {
    private val logger = LoggerFactory.getLogger(PersonContextColorCanvas::class.java)!!
    private val revJulDayFunc = { it: Double -> JulDayResolver1582CutoverImpl.getLocalDateTimeStatic(it) }
  }

}
