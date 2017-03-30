package idc.scalautils

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import idc.constants.Constants
import idc.dao.TaskBean


/**
  * Created by sky on 2017/3/13.
  */
object QueryFactory extends QueryTrait {

    def assembleSQL(taskBean: TaskBean): String = {
        val lowerAge = taskBean.getLowerAge
        val higherAge = taskBean.getHigherAge
        val startSessionTime = taskBean.getStartSessionTime
        val endSessionTime = taskBean.getEndSessionTime

        var sql: String = "SELECT * FROM clickAction, userInfo WHERE clickAction.user_id = userInfo.user_id"
        if (lowerAge != 0) sql += s" AND age >= $lowerAge"
        if (higherAge != 0) sql += s" AND age <= $higherAge"
        sql += s" AND age >= $lowerAge AND age < $higherAge"
        sql += jointSQL(Constants.TASK_PROFESSIONAL, taskBean.getProfessional)
        sql += jointSQL(Constants.TASK_CITY, taskBean.getCity)
        sql += jointSQL(Constants.DFRAME_SEARCH_WORD, taskBean.getSearchWord)
        sql += jointSQL(Constants.TASK_CATEGORY, taskBean.getCategory)
        sql += s" AND action_time >= ${startSessionTime.getTime} AND action_time < ${endSessionTime.getTime}"

        println("\n\n=====================================================================")
        println(s"===============================TASK ${taskBean.getTaskId}================================")
        println("=====================================================================")
        println("\n\n[Query statement according to the task in the database]:")
        println(sql + "\n\n")

        sql
    }

}

trait QueryTrait {

    def jointSQL(name: String, obj: util.ArrayList[String]): String = {
        var sql: String = ""
        if (obj != null) {
            for (i <- 0 until obj.size()) {
                val value = obj.get(i)
                if (i == 0) {
                    sql += " AND (" + name + " = '" + value + "'"
                } else {
                    sql += " OR " + name + " = '" + value + "'"
                }
                if (i == obj.size() - 1) {
                    sql += ")"
                }
            }
        }
        sql
    }

}



