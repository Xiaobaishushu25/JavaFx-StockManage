package Xbss.bean

import java.util.concurrent.locks.Condition

/**
 * @author  Xbss
 * @create 2022-11-01-17:56
 * @version  1.0
 * @describe isTrigger:0未触发  1已触发
 */
data class NoticeData(val id:String, val code:String, val name:String, var direction:String,var condition: String, var isTrigger:Int = 0,
                      val createTime:String, var triggerTime:String, val data:String, var isRead:Int = 0)
