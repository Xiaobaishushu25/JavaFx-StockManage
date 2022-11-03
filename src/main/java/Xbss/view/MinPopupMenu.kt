package Xbss.view

import Xbss.view.UIFun.EmptyStage
import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ContextMenu
import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Popup
import javafx.stage.Stage
import java.io.File
import java.util.*
import javax.sound.sampled.AudioSystem
import kotlin.system.exitProcess

/**
 * @author  Xbss
 * @create 2022-09-02-13:39
 * @version  1.0
 * @describe :界面最小化后的右键菜单
 */
class MinPopupMenu(stage: Stage, notificationStage: EmptyStage, sleep: Button, timer: Timer, notificationTimer: Timer):ContextMenu() {

    init {
        //试了类加载器、File转URL的方式，都不行，只能写死了
//        val resource = this.javaClass.classLoader.getResource("src/main/resources/nircmd-x64/nircmd.exe")?.toURI().toString().drop(6)
        val sleepItem = MenuItem("休眠").apply {
            textProperty().bind(sleep.textProperty())
            setOnAction {
                sleep.fire()
//                text = sleep.text
            }
        }
        val set = MenuItem("设置").apply {
            setOnAction {
                stage.show()
                stage.toFront()
            }
        }
        val exit = MenuItem("退出").apply {
            setOnAction {
                notificationStage.close()
                timer.cancel()
                notificationTimer.cancel()
                exitProcess(0)
            }
        }
        this.items.addAll(sleepItem,set,exit)
    }
}