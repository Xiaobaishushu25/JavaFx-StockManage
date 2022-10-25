package Xbss.view

import Xbss.bean.TableInfo
import javafx.collections.ObservableList
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.image.WritableImage
import javafx.scene.input.ClipboardContent
import javafx.scene.input.DataFormat
import javafx.scene.input.TransferMode
import javafx.util.Callback
import java.util.Timer

/**
 * @author  Xbss
 * @create 2022-08-08-20:05
 * @version  1.0
 * @describe
 */
class TableViewDragRows(private val tableView: TableView<TableInfo>, val stage: Any): Callback<TableView<TableInfo>, TableRow<TableInfo>> {
    var startIndex=0 //记录拖动开始的位置
    var endIndex=0  //记录拖动结束的位置
    override fun call(param: TableView<TableInfo>?): TableRow<TableInfo> {
        val row = object :TableRow<TableInfo>(){
            override fun updateItem(item: TableInfo?, empty: Boolean) {
                super.updateItem(item, empty)
            }
        }
        //开始拖动检测：记录下开始的位置（结束的时候删除并获得该单元格的数据）
        row.setOnDragDetected {
            startIndex=row.index
            row.startDragAndDrop(*TransferMode.ANY).apply {
                setContent(ClipboardContent().apply { putString(row.item.toString()) })
                val image = WritableImage(row.width.toInt(), row.height.toInt())
                dragView = row.snapshot(null, image)
            }
        }
        //拖动时划过时准备接受数据
        row.setOnDragOver {
            it.acceptTransferModes(*TransferMode.ANY)
        }
        //松手拖动结束，删除拖动开始位置的元素并将其加入到结束拖动的位置
        row.setOnDragDropped {
            endIndex= if(row.isEmpty) tableView.items.size-1 else row.index
            println("将第$startIndex  个数据${it.dragboard.getContent(DataFormat.PLAIN_TEXT)} 移动到第$endIndex 上")
            synchronized(stage){
                val removeData = MainWindow.observableList.removeAt(startIndex)
                println("被删除的数据${removeData.name} 移动到第$endIndex 上")
                MainWindow.observableList.add(endIndex,removeData)
            }
            it.isDropCompleted=true
            tableView.selectionModel.select(endIndex)
        }
        return row
    }
}