package com.gihub.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
object ExploreRDD {
  def main(args: Array[String]): Unit = {
    // Set the log level to only print errors
    Logger.getLogger("com.github").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine,
    val sc = new SparkContext("local[*]", "ExploreRDD")

    // Load up each line of the twitter data into an RDD
    val lines = sc.textFile("data/mockdata.txt") //here an action is called amd hence an RDD is created

    //traverse on every row over an RDD
    println(lines.foreach(line => {
      println(line) ///using for loop on an RDD
    }))
    val res = lines.collect()
    println(for (elem <- res) {
      print(elem) //using for loop on rdd collect.
    })

    //count the no of lines in RDD
    val lineCount = lines.count()
    println(lineCount)

    sc.stop()
  }
}
