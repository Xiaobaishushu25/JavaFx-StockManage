package Xbss.view

import Xbss.bean.NoticeData
import Xbss.view.UIFun.EmptyStage
import Xbss.view.UIFun.getFocusTextFlow
import javafx.animation.TranslateTransition
import javafx.geometry.Insets
import javafx.geometry.Rectangle2D
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import javafx.stage.Popup
import javafx.stage.Screen
import javafx.util.Duration

/**
 * @author  Xbss
 * @create 2022-10-26-17:47
 * @version  1.0
 * @describe :在桌面右下角弹出通知窗口
 */
class Notification(
    private val message: NoticeData,
    private var placement:Placement = Placement.BOTTOM_RIGHT
) {
    companion object{
        //主窗口要关闭这个窗口，所以static
        val emptyStage = EmptyStage()
    }
    private val textFlow = getFocusTextFlow(message)
    private val messageLabel = Label()
    private lateinit var visualBounds:Rectangle2D
    private val root = StackPane()
    private val translate = TranslateTransition(Duration.millis(800.0), root)
    private val popup = Popup()
    init {
        val pane = Pane()
        val iconUrl = if(message.direction.contains("跌"))"img/noticedown.png" else "img/noticeup.png"
        val noticeUp = ImageView(Image(iconUrl,24.0,24.0,false,false))
        val noticeLabel = Label("金融宝典通知").apply { style = "-fx-font-size:18" }
        val close = ImageView(getImage("img/close.png")).apply {
            isPickOnBounds = true
            style = "-fx-cursor: hand"
            setOnMouseClicked { popup.hide() }
        }
        val hBox = HBox(noticeUp,noticeLabel,pane,close).apply {
            padding = Insets(10.0)
            spacing = 20.0
            HBox.setHgrow(pane,Priority.ALWAYS)
        }
        val hhBox = HBox(textFlow).apply {
            padding = Insets(20.0)
        }
        val vBox = VBox(hBox,hhBox)
        root.apply {
            style = "-fx-background-color:white;"+"-fx-border-color:blue"
            prefWidth = 250.0
            prefHeight = 100.0
            children.addAll(vBox)
            stylesheets.addAll("css/Xbss.css")
        }
        popup.content.addAll(root)
        create()
        show()
    }
    private fun create():Notification{
        addRegister()
        this.visualBounds = Screen.getPrimary().visualBounds
        return this
    }
    fun setPlacement(placement: Placement):Notification{
        this.placement = placement
        return this
    }
    fun show(){
        if (!emptyStage.isShowing)
            emptyStage.show()
        if (!popup.isShowing)
            popup.show(emptyStage,0.0,0.0)
    }
    private fun addRegister(){
        popup.setOnShown {
            val maxX = visualBounds.maxX
            val maxY = visualBounds.maxY
            val minX = visualBounds.minX
            when(placement){
                Placement.BOTTOM_RIGHT ->{
                    popup.y = maxY - root.height
                    this.translate.fromX = maxX+10.0
                    this.translate.toX = maxX - root.width
                }
                else -> {}
            }
            this.translate.play()
        }
    }
    private fun getImage(url:String):Image{
        return Image(url,24.0,24.0,false,false)
    }
    enum class Placement {
        TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT
    }
}