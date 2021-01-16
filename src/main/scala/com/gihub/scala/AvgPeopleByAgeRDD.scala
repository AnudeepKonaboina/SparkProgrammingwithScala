package com.gihub.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext


object AvgPeopleByAgeRDD {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("com.github") setLevel (Level.ERROR)

    //create a sparkcontext
    val sparkContext = new SparkContext("local[*]", "AvgPeopleByAge")

    //calling an action which reads data from the file and stores it in the form of rdd.
    val fileData = sparkContext.textFile("data/person-dummy-data.csv")

    //parse every row of that data and map it to a new data containing tuples of (person,age)
    val rdd = fileData.map(parseLineAndConvert)

    //Get the sum of all ages of people with same Name
    // We are starting with an RDD of form (person, age) where person is the KEY and age is the VALUE
    // We use mapValues to convert each person value to a tuple of (age, 1)
    // Then we use reduceByKey to sum up the total age of a person with a particular name by
    // adding together all the age values and 1's respectively.
    val getTotalAgesOfParticularPeople = rdd.mapValues(x => (x, 1)).reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))

    //Get the avg age of each person
    val avgAgeOfEachPerson = getTotalAgesOfParticularPeople.mapValues(row => row._1 / row._2)

    //finaly collect the data and print
    val finalRes = avgAgeOfEachPerson.collect()
    finalRes.sorted.foreach(println)
  }

  /**
   * Convert each line to a tuple
   *
   * @param line input line
   * @return tuple of (person and Age)
   */
  def parseLineAndConvert(line: String): (String, Int) = {
    val data = line.split(",")
    val person = data(1)
    val age = data(2).toInt
    (person, age)
  }
}
