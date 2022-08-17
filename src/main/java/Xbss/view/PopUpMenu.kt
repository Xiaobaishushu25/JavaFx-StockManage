package Xbss.view

import Xbss.service.delete
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem

/**
 * @author  Xbss
 * @create 2022-08-03-21:58
 * @version  1.0
 * @describe
 */
class PopUpMenu(private val node: Node, var code: String, private var indexProperty: SimpleIntegerProperty) : ContextMenu() {
    init {
        val menu = MenuItem("删除").apply {
            setOnAction {
                println("开始删除$code")
                println("开始删除第${indexProperty.get()}")
                delete(code)
                MainWindow.observableList.removeAt(indexProperty.get())
//                tableview.refresh()
            }
//            addEventHandler(ActionEvent.ACTION){
//                println("开始删除$code")
//            }
        }
        items+=menu
//        items.addAll(menu, Menu().apply {
//            items+=MenuItem("取消")
//        })
        node.onContextMenuRequested= EventHandler{
            hide()
            show(node,it.screenX,it.screenY)
        }
    }
}