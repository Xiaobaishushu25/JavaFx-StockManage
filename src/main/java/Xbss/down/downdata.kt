package Xbss.down

import Xbss.service.QueryDayKByYear
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 * @author  Xbss
 * @create 2022-08-11-20:28
 * @version  1.0
 * @describe
 */
fun main() {
    val kArrayList = QueryDayKByYear.queryDayKByYear("515030").apply {
        val writer = BufferedWriter(OutputStreamWriter(FileOutputStream("G:\\A自己的下载\\金融宝典\\dataset.txt")))
        for(s in this){
            writer.write("\n${s.name} ${s.date} ${s.low} ${s.open} ${s.close} ${s.high} ${s.MA5} ${s.MA10} ${s.MA20} ${s.MA60} ")
        }
        writer.close()
    }
}