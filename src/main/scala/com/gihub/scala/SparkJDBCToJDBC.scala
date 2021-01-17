package com.gihub.scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}

object SparkJDBCToJDBC {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("com").setLevel(Level.ERROR)

    //create a spark session
    val sparkSession = SparkSession.builder().appName("JDBCConnector").master("local[*]").getOrCreate()

    //read the table from postgres and write it to another table in postgres.
    val sourceDF = sparkSession.read
      .format("jdbc")
      .option("driver", "org.postgresql.Driver")
      .option("url", "jdbc:postgresql://postgresql_test:5433/test")
      .option("dbtable", "test.type")
      .option("user", "test")
      .option("password", "mysecretpassword")
      .load()

    //creating a temp table .It is basically used for transforming data
    sourceDF.createTempView("__table__")

    //write to a new table in postgres after applying transformations.
    sourceDF.sqlContext.sql("select * from __table__").sqlContext.sql(" select  cast (col * 10 AS Integer) as bot_type_id,bot_type,bot_classname,bot_message_bus_class_name,control_message_bus_class_name,cru_by,cru_ts from __table__  where col in (1,2,3)").write
      .format("jdbc")
      .mode(SaveMode.Overwrite)
      .option("driver", "org.postgresql.Driver")
      .option("url", "jdbc:postgresql://postgresql_test:5433/test")
      .option("user", "test")
      .option("dbtable", "test.tmp1")
      .option("password", "mysecretpassword").save()

    //source data
    sourceDF.show()

    //check for the data in new table
    val queryResult = sparkSession.read
      .format("jdbc")
      .option("driver", "org.postgresql.Driver")
      .option("url", "jdbc:postgresql://postgresql_test:5433/test")
      .option("dbtable", "test.tmp1")
      .option("user", "test")
      .option("password", "mysecretpassword")
      .load()
    println("Destination table data :")
    queryResult.show()

    //stop spark session
    sparkSession.stop()
  }
}
