package com.gihub.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

object SaprkSQLCustomerAmt {

  case class CustomerData(id: Int, order_id: Int, amount_spent: Double)

  def main(args: Array[String]): Unit = {
    Logger.getLogger("com.github").setLevel(Level.ERROR)

    // Use SparkSession interface
    val sparkSession = SparkSession.builder().appName("SaprkSQLCustomerAmt").master("local[*]")
      .getOrCreate()

    import sparkSession.implicits._
    val customerData = sparkSession.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data/customer-orders-headders.csv").as[CustomerData]
    customerData.show()

    customerData.createOrReplaceTempView("customer")

    //Data Frame of the result data
    sparkSession.sql("select id,sum(amount_spent) as amt_spent from customer group by id ").show()

    //collecting data into an array and displaying it
    val result = sparkSession.sql(" select id,sum(amount_spent) as amt_spent from customer  group by id ").collect()
    result.foreach(println)

    sparkSession.stop()
  }

}
