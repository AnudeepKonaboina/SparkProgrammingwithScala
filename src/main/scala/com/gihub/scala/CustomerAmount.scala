package com.gihub.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

//Question:  Find the Total Amount Spent by Customer and the data is given in customer-order.csv file
object CustomerAmount {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("com.github").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "CustomerOrder")

    //reading the file to a rdd
    val dataRDD = sc.textFile("data/customer-orders.csv")

    //using map to parse each line and convert it to a tuple of (customerid,amount)
    //then reduce the result by customer id and perform add operation to get the sum
    val customerAmtRDD = dataRDD.map(line => {
      val fields = line.split(",")
      val customerID = fields(0).toInt
      val amountSpent = fields(2).toFloat
      (customerID, amountSpent)
    }).reduceByKey((x, y) => x + y).map(x => (x._2, x._1)).sortByKey()

    //collect the result and print it
    val result = customerAmtRDD.collect()
    result.foreach(println)
  }
}
