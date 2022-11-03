package Xbss.view.UIFun

import Xbss.view.MinPopupMenu
import Xbss.view.Notification
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.Timer
import javax.swing.ImageIcon

/**
 * @author  Xbss
 * @create 2022-11-03-11:25
 * @version  1.0
 * @describe
 */
class InitSystemTray {
    companion object{
        fun initSystemTray(stage: Stage ,notificationStage:EmptyStage,sleep: Button,timer:Timer,notificationTimer: Timer){
            val popupMenu =  MinPopupMenu(stage,notificationStage,sleep,timer,notificationTimer)
            val emptyStage = EmptyStage()
            emptyStage.show()
            val trayIcon = TrayIcon(ImageIcon(javaClass.getResource("/img/股票分析.png")).image, "金融宝典6.0").apply {
                isImageAutoSize = true
                addMouseListener(object : MouseAdapter() {
                    override fun mouseReleased(e: MouseEvent?) {
                        super.mouseReleased(e)
                        if (e?.button == 1)
                            Platform.runLater {
                                stage.show()
                                stage.toFront()
                            }
                        else if (e?.button == 3) {
                            val x = e.x
                            val y = e.y
                            Platform.runLater {
                                //不加这一句会一直显示菜单不知道为什么
                                emptyStage.requestFocus()
                                popupMenu.show(emptyStage,x.toDouble(),y.toDouble())
                            }
                        }
                    }
                })
            }
            SystemTray.getSystemTray().add(trayIcon)
        }
    }
}