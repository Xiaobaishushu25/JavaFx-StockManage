package Xbss.view

import Xbss.service.delete
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.*

/**
 * @author  Xbss
 * @create 2022-08-03-21:58
 * @version  1.0
 * @describe
 */
class PopUpMenu(private val node: Node, var code: String, private var indexProperty: SimpleIntegerProperty) : ContextMenu() {
    private val diaLog = Dialog<ButtonType>()
    init {
        val delete = MenuItem("删除").apply {
            setOnAction { deleteDiaLogShow() }
        }
        val notice = MenuItem("提醒").apply {
            setOnAction {
                val tableInfo = MainWindow.observableList[indexProperty.get()]
                Notice(tableInfo)
            }
        }
//        items+=menu
        items.addAll(notice,delete)
//        items.addAll(menu, Menu().apply {
//            items+=MenuItem("取消")
//        })
        this.styleClass.addAll("cf-context-menu")
        node.onContextMenuRequested= EventHandler{
            hide()
            show(node,it.screenX,it.screenY)
        }
    }
    private fun deleteDiaLogShow(){
        diaLog.apply {
            title = "警告"
            contentText = "确定删除该证券吗？"
            dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)
            (dialogPane.lookupButton(ButtonType.OK) as Button).apply {
                setOnAction {
                    delete(code)
                    MainWindow.observableList.removeAt(indexProperty.get())
                }
            }
        }
        diaLog.show()
    }
}