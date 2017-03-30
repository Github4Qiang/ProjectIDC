package idc.session

import idc.constants.Constants
import idc.dao.TaskDAO
import idc.session.SessionAnalysis._
import idc.scalautils.SparkUtils.loadLocalTestDataToTmpTable
import idc.scalautils.StreamingExamples
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext}

/**
  * Created by sky on 2017/3/13.
  */
object SessionManager {

    def main(args: Array[String]): Unit = {
        // 设置日志打印级别：WARN
        StreamingExamples.setStreamingLogLevels()

        val conf = new SparkConf().setAppName(Constants.LOCAL_APP_NAME)
        val sc = new SparkContext(conf)
        val sqlContext = new SQLContext(sc)

        // 载入数据，注册临时表
        loadLocalTestDataToTmpTable(sc, sqlContext)
        // 读取数据库，获取任务集
        val tasks = new TaskDAO().getAll()
        // 遍历任务集
        val iter = tasks.iterator()
        while (iter.hasNext) {

            // 根据任务中条件查询临时表，返回符合条件的Session构成的DataFrame
            val dataFrame = conditionsFind(sqlContext, iter.next())

            // 统计聚合，计算访问时长，页面访问步长
            val countBean = statistic(sc, dataFrame)
            printResult4Stat(countBean)

            // 排序
            val sortedProduct = productSort(sc, sqlContext, dataFrame)
            printResult4Sorted(sortedProduct)
        }
        sc.stop()
    }

    def printResult4Sorted(products: List[(String, (Int, Int, Int))]): Unit = {
        println("--------------------Secondary Sort------------------------")
        println("CategoryId\tClick\tOrder\tPay")
        products.foreach(product =>
            println(product._1 + "\t\t" + product._2._1 + "\t" +
                    product._2._2 + "\t" + product._2._3))
        println("----------------------------------------------------------")
    }

    def printResult4Stat(countBean: CountBean): Unit = {
        println("-------------------Access Time Length---------------------")
        println("[0, 3]\t(3, 6]\t(6, 9]\t(9, 30]\t(30, 60]\t(60, +)")
        println(countBean.interval.less3 + "\t" +
                countBean.interval.less6 + "\t" +
                countBean.interval.less9 + "\t" +
                countBean.interval.less30 + "\t" +
                countBean.interval.less60 + "\t\t" +
                countBean.interval.more60)
        println("-------------------Access Step Length---------------------")
        println("[1, 3]\t[4, 6]\t[6, ++)")
        println(countBean.step.less3 + "\t" + countBean.step.less6 + "\t" + countBean.step.more6)
    }

}
