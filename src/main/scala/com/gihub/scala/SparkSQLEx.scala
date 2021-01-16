package com.gihub.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession


object SparkSQLEx {

  case class Customer(id: Int, order_id: Int, amount_spent: Double)

  def main(args: Array[String]): Unit = {
    // Set the log level to only print errors
    Logger.getLogger("com.github").setLevel(Level.ERROR)

    // Use SparkSession interface
    val sparkSession = SparkSession
      .builder
      .appName("SparkSQL")
      .master("local[*]")
      .getOrCreate()

    import sparkSession.implicits._
    val customer = sparkSession.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data/customer-orders-headders.csv")
      .as[Customer]

    println("Schema:")
    customer.printSchema()
    customer.createOrReplaceTempView("customer")

    //show data frame
    customer.show()

    ///execute a query on the temp table created from a data set
    val query_result = sparkSession.sql("select * from customer where id > 20")
    val result = query_result.collect()
    result.foreach(println)

    //select a column
    customer.select("id", "order_id").show()

    //group by
    customer.groupBy("id").count().show()

    //filter like where
    customer.filter(customer("id") > 20).show()

    sparkSession.stop()

  }
}
