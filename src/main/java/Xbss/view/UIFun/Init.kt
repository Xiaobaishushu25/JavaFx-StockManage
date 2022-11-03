package Xbss.view.UIFun

import Xbss.view.MinPopupMenu
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
import javax.swing.ImageIcon

/**
 * @author  Xbss
 * @create 2022-11-03-11:11
 * @version  1.0
 * @describe ：初始化系统托盘
 */
//fun initSystemTray(stage:Stage,emptyStage: EmptyStage,sleep:Button){
//    val popupMenu =  MinPopupMenu(stage,sleep)
////        val trayIcon = TrayIcon(ImageIcon(File("${path}//src/main/resources/img/logo.png").toURI().toURL()).image, "Xbss截图").apply {
//    val trayIcon = TrayIcon(ImageIcon(sleep::class.java.getResource("/img/股票分析.png")).image, "金融宝典6.0").apply {
////        val trayIcon = TrayIcon(toolkit.getImage(javaClass.getResource("/img/logo.png")), "Xbss截图").apply {
//        isImageAutoSize = true
//        addMouseListener(object : MouseAdapter() {
//            override fun mouseReleased(e: MouseEvent?) {
//                super.mouseReleased(e)
//                if (e?.button == 1)
//                    Platform.runLater {
//                        stage.show()
//                        stage.toFront()
//                    }
//                else if (e?.button == 3) {
//                    val x = e.x
//                    val y = e.y
//                    Platform.runLater {
//                        //不加这一句会一直显示菜单不知道为什么
//                        emptyStage.requestFocus()
//                        popupMenu.show(emptyStage,x.toDouble(),y.toDouble())
//                    }
//                }
//            }
//        })
//    }
//    SystemTray.getSystemTray().add(trayIcon)
//}