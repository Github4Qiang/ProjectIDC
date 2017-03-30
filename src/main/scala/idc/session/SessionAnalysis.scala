package idc.session

import idc.constants.Constants
import idc.dao.TaskBean
import idc.scalautils.QueryFactory
import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SQLContext}

/**
  * 覆写的排序方法
  * 按照点击次数、下单、支付的优先级排序
  */
object Context {
    implicit val productid = new Ordering[ProductId] {
        override def compare(x: ProductId, y: ProductId): Int = {
            if (x.click == y.click) {
                if (x.order == y.order) {
                    return x.pay - y.pay
                }
                return x.order - y.order
            }
            x.click - y.click
        }
    }
}

/**
  * 用户session行为离线分析模块
  */
object SessionAnalysis {

    /** 3、	根据用户的查询条件，一个或者多个：
      * 年龄范围，职业（多选），城市（多选），搜索词（多选），点击品类（多选）进行数据过滤
      * 注意：session时间范围是必选的。返回的结果RDD元素格式同上 */
    def conditionsFind(sqlContext: SQLContext, task: TaskBean): DataFrame = {
        var sql = QueryFactory.assembleSQL(task)
        sqlContext.sql(sql)
    }

    /** 4、	实现自定义累加器完成多个聚合统计业务的计算，
      * 统计业务包括访问时长：1~3秒，4~6秒，7~9秒，10~30秒，30~60秒的session访问量统计，
      * 访问步长：1~3个页面，4~6个页面等步长的访问统计
      * Input：DataFrame
      * {   date, user_id, session_id ,page_id, action_time, search_keyword, click_category_id,
      * click_product_id, order_category_ids, order_product_ids, pay_category_ids, pay_product_ids, city_id,
      * user_id, username, name, age, professional, city, sex   }
      * */
    def statistic(sc: SparkContext, dataFrame: DataFrame): CountBean = {
        //rdd 返回 ((user_id:Long,session_id:String),action_time:Long)
        val rdd = dataFrame.rdd.map(row => ((row.getLong(1), row.getString(2)), row.getLong(4)))

        //通过Key进行聚合
        //=> （访问最小时间（开始），访问最大时间（结束），session个数（页面步长），
        val aggr = rdd.aggregateByKey(Long.MaxValue, Long.MinValue, 0)((u, m) => {
            (math.min(u._1, m), math.max(u._2, m), u._3 + 1)
        }, (u1, u2) => {
            (math.min(u1._1, u2._1), math.max(u1._2, u1._2), u1._3 + u1._3)
        })

        // 根据最大的时间以及最小的时间取得该Session的总时间
        // ((phone, sessionId), internal, count)
        val count = aggr.map(x => (x._1, (x._2._2 - x._2._1) / 1000, x._2._3))

        // 自定义累加器统计访问时长、步长
        val accumulable = sc.accumulable(CountBean(IntervalBean(0L, 0L, 0L, 0L, 0L, 0L),
            StepBean(0L, 0L, 0L)))(SessionAccumulator)
        count.foreach(x => accumulable.add(SessionBean(x)))
        accumulable.value
    }

    /**
      * 5、	对通过筛选条件的session，按照各个品类的点击、下单和支付次数，
      * 降序排列，获取前10个热门品类。优先级：点击，下单，支付
      **/
    def productSort(sc: SparkContext, sqlContext: SQLContext, dataFrame: DataFrame): List[(String, (Int, Int, Int))] = {
        //    clickRDD.show()
        //定义点击、下单、支付动作的合并计算函数
        val func = (a: (Int, Int, Int), b: (Int, Int, Int)) => {
            (a._1 + b._1, a._2 + b._2, a._3 + b._3)
        }
        import Context._
        // map：形成（品类ID，（点击，下单，支付））键值对
        // filter：过滤不为null的product
        // reduceByKey：按照productID进行分组，并且计算（点击，下单，支付）对应的次数
        // sortBy ：使用覆写的比较方法排序
        val productRDD = dataFrame.map(x => {
            if (x(6) != Constants.STRING_NULLABLE) (x(6).toString, (1, 0, 0))
            else if (x(8) != Constants.STRING_NULLABLE) (x(8).toString, (0, 1, 0))
            else if (x(10) != Constants.STRING_NULLABLE) (x(10).toString, (0, 0, 1))
            else (Constants.STRING_NULLABLE, (0, 0, 0))
        }).filter(_._1 != Constants.STRING_NULLABLE).reduceByKey(func).sortBy(x =>
            ProductId(x._1, x._2._1, x._2._2, x._2._3), false).take(10)

        productRDD.toList
    }
}

case class ProductId(productID: String, click: Int, order: Int, pay: Int) extends Serializable