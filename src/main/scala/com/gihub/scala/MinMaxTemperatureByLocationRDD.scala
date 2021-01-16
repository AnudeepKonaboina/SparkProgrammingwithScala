package com.gihub.scala


import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

import scala.math.{max, min}

object MinMaxTemperatureByLocationRDD {
  def parseLine(line: String): (String, String, Float) = {
    val fields = line.split(",")
    val stationID = fields(0)
    val entryType = fields(2)
    val temperature = fields(3).toFloat * 0.1f * (9.0f / 5.0f) + 32.0f
    (stationID, entryType, temperature)
  }

  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("com.github").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "MinTemperatureByLocation")

    // Read each line of input data
    val lines = sc.textFile("data/temperature_filter.csv")

    // Convert to (stationID, entryType, temperature) tuples
    val parsedLines = lines.map(parseLine)

    // Filter out all but TMIN entries
    val minTemps = parsedLines.filter(x => x._2 == "TMIN")

    // Filter out all but TMIN entries
    val maxTemp = parsedLines.filter(x => x._2 == "TMAX")

    // Convert to (stationID, temperature)
    val stationTemps = minTemps.map(x => (x._1, x._3.toFloat))


    // Reduce by stationID retaining the minimum temperature found
    val minTempsByStation = stationTemps.reduceByKey((x, y) => min(x, y))

    // Reduce by stationID retaining the max temperature found
    val maxTempsByStation = stationTemps.reduceByKey((x, y) => max(x, y))


    // Collect, format, and print the results
    val results = minTempsByStation.collect()

    for (result <- results.sorted) {
      val station = result._1
      val temp = result._2
      val formattedTemp = f"$temp%.2f F"
      println(s"Station id :$station has minimum temperature: $formattedTemp")
    }

    //max temp by station
    val result = maxTempsByStation.collect()

    for (value <- result.sorted) {
      val station = value._1
      val temp = value._2
      val formattedTemp = f"$temp%.2f F"
      println(s"Station id :$station has max temperature: $formattedTemp")
    }
  }
}
