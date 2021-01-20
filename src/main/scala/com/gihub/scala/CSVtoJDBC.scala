package com.gihub.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}

import java.util.Properties

object CSVtoJDBC {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("com").setLevel(Level.ERROR)
    val sparkSession = SparkSession.builder().master("local[*]").appName("CSVtoJDBC").getOrCreate()

    //read csv file and store it in a df
    val sourceDF = sparkSession.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data/FinancialSample.csv")
    sourceDF.createOrReplaceTempView("__tmptable__")
    //show df
    sourceDF.show(false)


    //properties for postgres
    val properties = new Properties()
    properties.put("driver", "org.postgresql.Driver");
    properties.put("user", "postgres_user")
    properties.put("password", "mysecretpassword")

    //write it to a table in postgres.
    sourceDF.write
      .mode(SaveMode.Overwrite)
      .jdbc("jdbc:postgresql://postgres-test:5433/test", "test.tmp_table", properties)

    sparkSession.stop()
  }
}
