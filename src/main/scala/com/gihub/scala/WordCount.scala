package com.gihub.scala

import org.apache.log4j._
import org.apache.spark.SparkContext
import org.apache.spark.sql._
object WordCount {
  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("com.github").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "WordCount")

    // Read each line of my book into an RDD
    val input = sc.textFile("data/book.txt")

    // Split into words separated by a space character
    val words = input.flatMap(x => x.split(" "))

    // Count up the occurrences of each word
    val wordCounts = words.countByValue()

    // Print the results.
    wordCounts.foreach(println)


    //Better way of doing a word count
    val wordsAfterRegex = input.flatMap(x => x.split("\\W+"))

    // Normalize everything to lowercase
    val lowercaseWords = words.map(x => x.toLowerCase())

    // Count of the occurrences of each word
    val wordCountsNew = lowercaseWords.countByValue()

    // Print the results
    wordCountsNew.foreach(println)
  }
}
