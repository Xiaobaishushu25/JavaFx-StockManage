package Xbss.view

import Xbss.bean.CandleChartItem
import javafx.event.Event
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Side
import javafx.scene.Cursor
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.stage.Popup
import java.math.BigDecimal
import java.time.LocalDate

/**
 * @author  Xbss
 * @create 2022-08-12-22:56
 * @version  1.0
 * @describe
 */
class CandleChart(val name:String ,itemss:MutableList<CandleChartItem>):Region() {
    private val chartPreWidth=1860.0
    private val chartPreHeight=1000.0
    private val originData:MutableList<CandleChartItem>
    private var items:MutableList<CandleChartItem>
    private var minValue:Double = 0.0
    private var maxValue:Double = 0.0
    private var scaleFactoryY:Double = 0.0
    private lateinit var boxValueList:MutableList<Double>
    private var boxLineMap = mutableMapOf<Line,Double>()
    private lateinit var gainList:MutableMap<String,String>
    private lateinit var nodeItem:HashMap<Rectangle, CandleChartItem>
    private val popup = Popup()
    private val valuePopup = Popup()
    private val canvas:Canvas
    private var context:GraphicsContext
    private var leftAxis:NumberAxis
    private val root:GridPane
    private val boxPane:Pane
    private val scrollArea = mutableMapOf<Int,Rectangle>()
    private var chooseChartItem = CandleChartItem("0",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0)
    init {
        originData=itemss.reversed() as MutableList<CandleChartItem>
        val initBegin=if (originData.size>200) originData.size-200 else 0
        items = originData.slice(initBegin until originData.size)as MutableList<CandleChartItem>
        leftAxis = NumberAxis(0.0,10.0,1.0).apply { side=Side.LEFT }
        canvas=Canvas(chartPreWidth,chartPreHeight)
        context=canvas.graphicsContext2D
        root = GridPane().apply { padding= Insets(10.0) }
        boxPane = Pane().apply {
            this.prefWidth=chartPreWidth
            this.prefHeight=chartPreHeight
            setOnMouseClicked {
                if (it.button.equals(MouseButton.PRIMARY))
                    children.add(drawBoxLineByY(it.y))
            }
            setOnMouseMoved {
                Event.fireEvent(canvas,it)
            }
            setOnScroll {
                Event.fireEvent(canvas,it)
//                    canvas.setItems(prepareData() as MutableList<CandleChartItem>)
            }
            val todayData = originData[originData.size - 1]
            val yesterdayData = originData[originData.size - 2]
            val msg = getDragPane(VBox(Label("  $name"),
                                       Separator(Orientation.HORIZONTAL),
                                       Label("日期: ${todayData.date}/ ${computeDayOfWeek(todayData.date)}"),
                                       Label("现价: ${todayData.close}"),
                                       Label("涨幅：${computeGain(yesterdayData,todayData)}%"),
                                       Label("五日线 ${yesterdayData.MA5}").apply { style="-fx-background-color:#8B4726;"+"-fx-text-fill:white"},
                                       Label("十日线 ${yesterdayData.MA10}").apply { style="-fx-background-color:#436EEE;"+"-fx-text-fill:white"},
                                       Label("二十日线 ${yesterdayData.MA20}").apply { style="-fx-background-color:#FF00FF;"+"-fx-text-fill:white"},
                                       ).apply { style="-fx-border-color:#A020F0;"+"-fx-border-width:2;"+"-fx-font-size:20px" }
            ).apply {
                translateX=chartPreWidth-250.0
//                layoutX=chartPreWidth-250.0
//                layoutY=chartPreHeight-250.0
                translateY=chartPreHeight-250.0
            }
            this.children.add(msg)
        }
        val cascadePane = StackPane(canvas,boxPane)
        root.add(cascadePane, 1, 0)
        root.add(leftAxis, 0, 0)
        this.children.addAll(root)
        computeData()
        drawCandleChart()
        registerListener()
        canvas.localToScene(canvas.boundsInLocal).apply {
            val d = (maxX - minX) / 5
            println("最大纸 ：$minX")
            println("最大纸 ：$maxX")
            println("距离是：${maxX-minX}")
            println("距离是：${(maxX-minX)/5}")
            for (i in 1..5){
                scrollArea[i] = Rectangle(minX+(i-1)*d,0.0,d,100.0)
            }
        }
        for((x,y) in scrollArea){
            println("第${x}是从${y.x} 宽度是${y.width}")
        }
    }
    constructor(name: String,itemss:MutableList<CandleChartItem>, boxList: MutableList<Double>) : this(name,itemss) {
        this.boxValueList=boxList
//        println("开始第二构造器")
//        for (value in boxList)
//            boxPane.children.add(drawBoxLine(computeBoxLineYByValue(value)))
//        boxPane.children.add(drawBoxLine(value))
    }
    private fun setItems(items:MutableList<CandleChartItem>){
        //如果大小不变了，说明滚到最大了，此时就不用刷新了
        if (this.items.size!=items.size){
            this.items=items
            computeData()
            drawCandleChart()
            redrawBoxLine()
        }
    }
    fun getBoxList():MutableList<Double>{
        val boxList= mutableListOf<Double>()
        for ((key,value ) in boxLineMap){
            if (key.isVisible)
                boxList.add(value)
        }
        return boxList.apply { sort() }
    }
    private fun drawCandleChart(){
//         canvas = Canvas(1860.0,1000.0).apply {
         canvas.apply {
             nodeItem.clear()
//        val canvas = Canvas(1820.0,1000.0).apply {
            val width = chartPreWidth
             val height= chartPreHeight
//            val range= String.format("%.3f",maxValue-minValue).toDouble()
//            val scaleFactoryY=String.format("%.3f",height/range).toDouble()
            val itemWidth=width/(items.size+items.size*0.2)
            val halfItemWidth=itemWidth*0.5
            val itemSpace=itemWidth*0.2
            val ma5Point= mutableMapOf<Double,Double>()
            val ma10Point= mutableMapOf<Double,Double>()
            val ma20Point= mutableMapOf<Double,Double>()
            val ma60Point= mutableMapOf<Double?,Double>()
//            println("初始化的：$context")
//            context=graphicsContext2D.apply {
            graphicsContext2D.apply {
                clearRect(0.0,0.0,width,height)
                for ((index,item) in items.withIndex()){
                    val high=item.high
                    val low=item.low
                    val open=item.open
                    val close=item.close
                    val x=halfItemWidth+index.toDouble()*itemWidth+index.toDouble()*itemSpace
                    val openY=(open-minValue)*scaleFactoryY
                    val closeY=(close-minValue)*scaleFactoryY
                    val highY=(high-minValue)*scaleFactoryY
                    val lowY=(low-minValue)*scaleFactoryY
                    val rangeY=if (open<close) closeY-openY else openY-closeY
                    if(item.MA5!=null){
                        val ma5Y=(item.MA5-minValue)*scaleFactoryY
                        ma5Point[x] = height-ma5Y
                    }
                    if(item.MA10!=null){
                        val ma10Y=(item.MA10-minValue)*scaleFactoryY
                        ma10Point[x] = height-ma10Y
                    }
                    if(item.MA20!=null){
                        val ma20Y=(item.MA20-minValue)*scaleFactoryY
                        ma20Point[x] = height-ma20Y
                    }
                    if(item.MA60!=null){
                        val ma60Y=(item.MA60-minValue)*scaleFactoryY
                        ma60Point[x] = height-ma60Y
                    }
//                    val ma5Y= (item.MA5?.minus(minValue))?.times(scaleFactoryY)
//                    val ma10Y= (item.MA10?.minus(minValue))?.times(scaleFactoryY)
//                    val ma20Y=(item.MA20-minValue)*scaleFactoryY
//                    val ma20Y= (item.MA20?.minus(minValue))?.times(scaleFactoryY)
//                    val ma60Y= (item.MA60?.minus(minValue))?.times(scaleFactoryY)
//                    ma5Point[x] = if (ma5Y!=null) height-ma5Y else null
//                    ma10Point[x] = height-ma10Y
//                    ma20Point[x]=height-ma20Y
//                    ma60Point[x]=height-ma60Y
                    fill=if(open>close) Color.GREEN else Color.RED
                    stroke=if(open>close) Color.GREEN else Color.RED
                    if (item.open>item.close){//绿柱子
                        strokeLine(x,height-highY,x,height-lowY)
                        fillRect(x-halfItemWidth,height-openY,itemWidth,rangeY)
                        nodeItem[Rectangle(x-halfItemWidth,height-openY-20.0,itemWidth,rangeY+20.0)] = item
                    }else{//红柱子
                        strokeLine(x,height-highY,x,height-closeY)
                        strokeLine(x,height-openY,x,height-lowY)
                        strokeRect(x-halfItemWidth,height-closeY,itemWidth,rangeY)
                        nodeItem[Rectangle(x-halfItemWidth,height-closeY-20.0,itemWidth,rangeY+20.0)] = item
                    }
                }
            }
            drawMaLine(ma5Point,"#8B4726")
            drawMaLine(ma10Point,"#436EEE")
            drawMaLine(ma20Point,"#FF00FF")
//            val list =
//                mutableListOf<Double>(height-(1.943 - minValue) * scaleFactoryY, height-(2.26 - minValue) * scaleFactoryY)
//            drawBox(list,width)
//            println("绘制的：$context")
//            var oldRectangle=Rectangle()
//            onMouseMoved= EventHandler {
//                if (oldRectangle.contains(it.x,it.y)&&popup.isShowing){
//                }else{
//                    for ((area,item) in nodeItem){
//                        if (area.contains(it.x,it.y)){
//                            if (!popup.isShowing){
//                                changeAndShow(item,it.x,it.y)
//                                oldRectangle=area
//                            }
//                            break
//                        }else{
//                            hidePopUp()
//                        }
//                    }
//                }
//                showValuePopup(String.format("%.3f",(height-it.y) /scaleFactoryY+minValue),it.screenY)
//            }
        }
//        this.children.clear()
//        this.children.addAll(canvas)
    }
    private fun drawMaLine(maPoint:MutableMap<Double,Double>, color:String){
//        println("调用了的：$context")
        context.apply {
            stroke= Paint.valueOf(color)
            beginPath()//清除之前的路径（主要是清除颜色）
            maPoint.entries.iterator().next().apply { moveTo(this.key,this.value) }
            for ((x,y) in maPoint){
                lineTo(x,y)
            }
            stroke()
        }
    }
    private fun changeAndShow( item: CandleChartItem, x:Double, y:Double){
        popup.content.clear()
        popup.content.addAll(VBox(
            Label("${item.date}/ ${computeDayOfWeek(item.date)}").apply { style="-fx-background-color:#00BFFF" },
            Label("开盘  ：${item.open}"),
            Label("最高  ：${item.high}"),
            Label("最低  ：${item.low}"),
            Label("收盘  ：${item.close}"),
            Label("MA5  ：${item.MA5}"),
            Label("MA10 ：${item.MA10}"),
            Label("MA20 ：${item.MA20}"),
            Label("MA60 ：${item.MA60}"),
            Label("涨跌幅：${gainList[item.date]}")
        ).apply { style="-fx-background-color:#FFDEAD" })
        //这个弹窗有时会挡住很多东西，要注意位置
        popup.show(this,x+20.0,y+60.0)
    }
    private fun hidePopUp(){
        popup.hide()
    }
    private fun showValuePopup(value:String,y: Double){
        valuePopup.content.clear()
        valuePopup.content.addAll(Label(value).apply { style="-fx-background-color:blue;"+"-fx-text-fill:white" })
        valuePopup.show(this,10.0,y)
    }
    private fun registerListener(){
        var oldRectangle=Rectangle()
        canvas.onMouseMoved= EventHandler {
            if (oldRectangle.contains(it.x,it.y)&&popup.isShowing){
            }else{
                for ((area,item) in nodeItem){
                    if (area.contains(it.x,it.y)){
                        if (!popup.isShowing){
                            changeAndShow(item,it.x,it.y)
                            oldRectangle=area
                        }
                        break
                    }else{
                        hidePopUp()
                    }
                }
            }
            showValuePopup(String.format("%.3f",(chartPreHeight-it.y) /scaleFactoryY+minValue),it.screenY)
        }
//        val foreNum =80
//        val backNum =80
//        var beginIndex=0//开始裁剪的位置
//        var endIndex=0//结束裁剪的位置
        canvas.setOnScroll {
            for ((index,area) in scrollArea){
                if (area.x<=it.x&&it.x<=area.x+area.width) {
                    println("在第$index 区域滚动")
                    val flag = it.deltaY>0
                        when(index){
                            1 -> scrollClip(0,4,flag)
                            2 -> scrollClip(1,3,flag)
                            3 -> scrollClip(1,1,flag)
                            4 -> scrollClip(3,1,flag)
                            5 -> scrollClip(4,0,flag)
                        }
                }
            }
//            for ((area,item) in nodeItem){
//                if (area.x<=it.x&&it.x<=area.x+area.width){//判断从哪里开始裁剪
//                    if (chooseChartItem!=item){//如果换中心item的话重新划定裁剪范围
//                        val index = originData.indexOf(item)
//                        beginIndex = if(index-foreNum>0) index-foreNum else 0
//                        endIndex = if(index+backNum>=originData.size) originData.size-1 else index+backNum
//                        setItems(originData.slice(beginIndex..endIndex)as MutableList<CandleChartItem> )
//                        chooseChartItem = item
//                    }else{ //没换的话直接在原开始结束位置自增自减就可以了
//                        if (it.deltaY<0){ //鼠标滚轮向下，范围扩大
//                            beginIndex=if (beginIndex-30>0) beginIndex-30 else 0
//                            endIndex = if (endIndex+30>=originData.size) originData.size-1 else endIndex+30
//                            setItems(originData.slice(beginIndex..endIndex)as MutableList<CandleChartItem> )
//                        }else{
//                            beginIndex += 30
//                            endIndex -= 30
//                            if(beginIndex>endIndex)
//                                setItems(originData.slice(beginIndex..beginIndex)as MutableList<CandleChartItem> )
//                            else
//                                setItems(originData.slice(beginIndex..endIndex)as MutableList<CandleChartItem> )
//                        }
//                    }
//                    break
//                }
//            }
            it.consume()//把这个滚动事件消耗掉
        }
    }

    /**
     * TODO :根据传入的比例按一定比例切割
     *
     * @param left ：开始切割的索引
     * @param right：结束切割的索引
     * @param flag：flag为true代表滚轮向上，放大图即在items两端切割，为false代表滚轮向下
     */
    private fun scrollClip(left:Int,right:Int,flag:Boolean){
        if (items.size<25&&flag) //如果已经放的很大并且还想放大，直接返回
            return
        val ratio = if (items.size<50) 5 else 10 //比率，当前蜡烛图已经很少的话减小比率，这样更精细可以放的更大
        if(flag){
            val beginIndex=ratio*left//开始裁剪的位置
            val endIndex=items.size-ratio*right//结束裁剪的位置
            println("当前数目是 ${items.size} 从$beginIndex 开始切割到$endIndex")
            setItems(items.slice(beginIndex until  endIndex)as MutableList<CandleChartItem>)
        }else{
            val oldBeginIndex = originData.indexOf(items[0])
            val oldEndIndex = originData.indexOf(items[items.size-1])
            val beginIndex=if(oldBeginIndex-ratio*right<=0) 0 else oldBeginIndex-ratio*right//开始裁剪的位置
            val endIndex=if (oldEndIndex+ratio*left>originData.size) originData.size else oldEndIndex+ratio*left//结束裁剪的位置
            println("当前数目是 ${originData.size} 从$beginIndex 开始切割到$endIndex")
            setItems(originData.slice(beginIndex until  endIndex)as MutableList<CandleChartItem>)
        }
    }
    //由于popup必须在窗口显示出来才能show，所以不得已把初始化箱体线抽离出来在stage.show()后才能调用
    fun drawBoxLine(){
        for (value in boxValueList){
            boxPane.children.add(drawBoxLineByY(computeBoxLineYByValue(value)).apply { boxLineMap[this] = value })
        }
    }
    private fun redrawBoxLine(){
        //因为不能在遍历map的同时添加元素，需要一个临时map来存储数据
        val map= mutableMapOf<Line,Double>()
        for ((key,value )in boxLineMap){
            if (key.isVisible){
                map[key]=value
                key.isVisible=false
            }
        }
        for ((key,value )in map){
            boxPane.children.add(drawBoxLineByY(computeBoxLineYByValue(value)))
        }
    }
    private fun drawBoxLineByY(y:Double):Line{
        return  Line(0.0, y, chartPreWidth, y).apply {
            var fromY=0.0
            var lastTranslateY=0.0
            val popup=Popup()
            val valueLabel=Label(computeBoxValueYByY(y).toString()).apply {
//            val valueLabel=Label(String.format("%.3f",(chartPreHeight-y) /scaleFactoryY+minValue)).apply {
                style="-fx-background-color:#CD8500;"+"-fx-text-fill:white"
            }
            popup.content.add(valueLabel)
            popup.show(this@CandleChart,10.0,y+15.0)
            hoverProperty().addListener { _,_,newValue ->
                if (newValue)
                    cursor= Cursor.HAND
            }
            setOnMousePressed {
                fromY = it.sceneY
                lastTranslateY=translateY
            }
            setOnMouseDragged {
                translateY = (BigDecimal(it.sceneY).subtract(BigDecimal(fromY)).toDouble()+lastTranslateY).apply {
                    popup.show(this@CandleChart,10.0,this+y+15.0)
                }
//                valueLabel.text=String.format("%.3f",(chartPreHeight-y-translateY) /scaleFactoryY+minValue)
                valueLabel.text=computeBoxValueYByY(y+translateY).toString()
            }
            setOnMouseReleased {
                boxLineMap[this] = computeBoxValueYByY(y+translateY)//松手更新内容
            }
            setOnMouseClicked {
                if (it.button.equals(MouseButton.SECONDARY)){
                    this.isVisible=false
//                    popup.hide()
                }
                it.consume()
            }
            visibleProperty().addListener { _,_,newValue ->
                if (!newValue)
                    popup.hide()
            }
            boxLineMap[this] = computeBoxValueYByY(y)//每新画一条线就加到map集合里
        }
    }
    private fun getDragPane(pane: Pane):Pane{
        return Pane().apply {
            children.add(pane)
            var oldNodeX = 0.0
            var oldNodeY = 0.0
            var oldMoveX=0.0
            var oldMoveY=0.0
//            val bounds = this.localToScene(this.boundsInLocal)
//            val bounds = this.localToParent(this.boundsInLocal)
            setOnMousePressed {
                oldNodeX = it.sceneX
                oldNodeY = it.sceneY
                oldMoveX=translateX
                oldMoveY=translateY
            }
            setOnMouseDragged {
                translateX= it.sceneX - oldNodeX+oldMoveX
                translateY= it.sceneY - oldNodeY+oldMoveY
            }
            setOnMouseClicked {
                it.consume()//消耗掉点击事件以免画线
            }
        }
    }
    private fun computeBoxLineYByValue(value:Double):Double{
        return chartPreHeight-(value - minValue) * scaleFactoryY
    }
    private fun computeBoxValueYByY(y:Double):Double{
        return String.format("%.3f",(chartPreHeight-y) /scaleFactoryY+minValue).toDouble()
    }
    private fun computeGain(yesItem: CandleChartItem, todayItem:CandleChartItem):String{
        return String.format("%.2f",
            BigDecimal(todayItem.close.toString()).subtract(BigDecimal(yesItem.close.toString())).toDouble()/yesItem.close*100.0)
    }
    private fun computeDayOfWeek(date: String):String{
        return when(LocalDate.parse(date).dayOfWeek.value){
            1 ->  "一"
            2 ->  "二"
            3 ->  "三"
            4 ->  "四"
            5 ->  "五"
            6 ->  "六"
            7 ->  "日"
            else -> "?"
        }
    }
    //进行绘制前数据的计算
    private fun computeData(){
        minValue=items.minOf {  it.low }
        maxValue=items.maxOf {  it.high}
        gainList= mutableMapOf<String,String>().apply {
            clear()
            for (x in 1 until items.size){
                put(items[x].date,computeGain(items[x-1],items[x])+"%")
            }
        }
        nodeItem = hashMapOf<Rectangle, CandleChartItem>()
        val range= String.format("%.3f",maxValue-minValue).toDouble()
        scaleFactoryY=String.format("%.3f",chartPreHeight/range).toDouble()
        leftAxis.apply {
            lowerBound=minValue
            upperBound=maxValue
            tickUnit=if (String.format("%.1f",(maxValue-minValue) /10)!="0.0") String.format("%.1f",(maxValue-minValue) /10).toDouble()
            else String.format("%.2f",(maxValue-minValue) /10).toDouble()
        }
    }
}