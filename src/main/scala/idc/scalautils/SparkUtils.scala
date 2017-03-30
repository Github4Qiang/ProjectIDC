package idc.scalautils

import java.text.SimpleDateFormat

import idc.conf.ConfigurationManager
import idc.constants.Constants
import org.apache.spark.SparkContext
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext}

/**
  * 加载本地测试数据到注册表
  * 与原始数据进行连接，将原始数据从
  * 文本文件中提取每个字段，并放到相应的表中
  */

object SparkUtils {
    def loadLocalTestDataToTmpTable(sc: SparkContext, sqlContext: SQLContext): Unit = {

        //读入用户session日志数据并注册为临时表
        //首先指定临时表的Schema
        val sessionschema = StructType(
            List(
                StructField("date", StringType, true),
                StructField("user_id", LongType, true),
                StructField("sessiopn_id", StringType, true),
                StructField("page_id", LongType, true),
                StructField("action_time", LongType, true),
                StructField("search_keyword", StringType, true),
                StructField("click_category_id", StringType, true),
                StructField("click_product_id", StringType, true),
                StructField("order_category_ids", StringType, true),
                StructField("order_product_ids", StringType, true),
                StructField("pay_category_ids", StringType, true),
                StructField("pay_product_ids", StringType, true),
                StructField("city_id", LongType, true)
            )
        )

        //从指定位置创建RDD
        val session_path = ConfigurationManager.getProperty(Constants.LOCAL_SESSION_DATA_PATH)
        val sessionRDD = sc.textFile(session_path).map(_.split(" "))
        val sessionrowRDD = sessionRDD.map(s =>
            Row(s(0).trim, s(1).toLong, s(2).trim, s(3).toLong,
                new SimpleDateFormat(Constants.DATE_TIME_FORMAT).parse(s(4).trim + " " + s(5).trim).getTime(),
                s(6).trim, s(7).trim, s(8).trim, s(9).trim, s(10).trim, s(11).trim, s(12).trim, s(13).toLong))
        //将schema信息应用到rowRDD上
        val sessionDataFrame = sqlContext.createDataFrame(sessionrowRDD, sessionschema)
        //注册临时sessionAction表
        sessionDataFrame.registerTempTable(Constants.TABLE_USER_VISIT_ACTION)

        //定义用户数据schema
        val userschema = StructType(
            List(
                StructField("user_id", LongType, true),
                StructField("username", StringType, true),
                StructField("name", StringType, true),
                StructField("age", IntegerType, true),
                StructField("professional", StringType, true),
                StructField("city", StringType, true),
                StructField("sex", StringType, true)
            )
        )
        //从指定位置创建RDD
        val user_path = ConfigurationManager.getProperty(Constants.LOCAL_USER_DATA_PATH)
        val userRDD = sc.textFile(user_path).map(_.split(" "))
        val userrowRdd = userRDD.map(u => Row(u(0).toLong, u(1).trim, u(2).trim, u(3).toInt, u(4).trim,
            u(5).trim, u(6).trim))
        val userDataFrame = sqlContext.createDataFrame(userrowRdd, userschema)
        //注册用户信息临时表
        userDataFrame.registerTempTable(Constants.TABLE_USER_INFO)

    }
}
