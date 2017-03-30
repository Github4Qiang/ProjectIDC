package idc.session

import org.apache.spark.AccumulableParam

/**
  * Created by sky on 2017/3/13.
  */
object SessionAccumulator extends AccumulableParam[CountBean, SessionBean] {

    // 一个分区内，将输入值SessionBean加到累加器中
    override def addAccumulator(r: CountBean, t: SessionBean): CountBean =
        addInPlace(r, CountBean(if (t.tuple._2 <= 3) IntervalBean(1, 0, 0, 0, 0, 0)
        else if (3 < t.tuple._2 && t.tuple._2 <= 6) IntervalBean(0, 1, 0, 0, 0, 0)
        else if (6 < t.tuple._2 && t.tuple._2 <= 9) IntervalBean(0, 0, 1, 0, 0, 0)
        else if (9 < t.tuple._2 && t.tuple._2 <= 30) IntervalBean(0, 0, 0, 1, 0, 0)
        else if (30 < t.tuple._2 && t.tuple._2 <= 60) IntervalBean(0, 0, 0, 0, 1, 0)
        else IntervalBean(0, 0, 0, 0, 0, 1),
            if (t.tuple._3 <= 3) StepBean(1, 0, 0)
            else if (3 < t.tuple._3 && t.tuple._3 <= 6) StepBean(0, 1, 0)
            else StepBean(0, 0, 1)))


    // 两个分区的累加器结果相加
    override def addInPlace(r1: CountBean, r2: CountBean): CountBean = CountBean(
        IntervalBean(r1.interval.less3 + r2.interval.less3, r1.interval.less6 + r2.interval.less6,
            r1.interval.less9 + r2.interval.less9, r1.interval.less30 + r2.interval.less30,
            r1.interval.less60 + r2.interval.less60, r1.interval.more60 + r2.interval.more60),
        StepBean(r1.step.less3 + r2.step.less3, r1.step.less6 + r2.step.less6, r1.step.more6 + r2.step.more6))

    // 累加器的初始值
    override def zero(initialValue: CountBean): CountBean =
        CountBean(IntervalBean(0L, 0L, 0L, 0L, 0L, 0L), StepBean(0L, 0L, 0L))
}

// 自定义累加器的输入类型
// (tuple: (phone, sessionId), internal, count, searchWords, clickCategory)
case class SessionBean(tuple: ((Long, String), Long, Int))
        extends Serializable

// 自定义累加器的返回结果类型
// interval: 访问时长
// step: 访问步长
case class CountBean(interval: IntervalBean, step: StepBean)
        extends Serializable

// 访问时长的Bean
// interval -> [0, 3], (3, 6], (6, 9], (9, 30], (30, 60], (60, ++)
case class IntervalBean(less3: Long, less6: Long, less9: Long, less30: Long, less60: Long, more60: Long)
        extends Serializable

// 访问步长的Bean
// step -> [0, 3], (3, 6], (6, ++)
case class StepBean(less3: Long, less6: Long, more6: Long)
        extends Serializable