package com.gihub.scala

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{lit, _}


object DataframeOperations extends App {

  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("DFOperations")
    .getOrCreate()
  spark.conf.set("spark.sql.shuffle.partitions", "10")

  import spark.implicits._

  val df1 = spark.read
    .option("inferSchema", true)
    .option("header", true)
    .csv(path = "data/Employees.csv")

  df1.show(10, false) //show 10 lines of df with truncate as false

  df1.columns //prints the columns of the dataframe

  df1.printSchema() //prints the schema for the dataframe


  //select statements different ways
  df1.select("Emp ID", "First Name", "Last Name").show()
  df1.select(col("Emp ID"), col("First Name")).show()
  df1.select($"EMP ID", col("Emp ID"), col("First Name"), df1.col("Last Name")).show()

  //rename a column --if a column which doesn't exists is given then spark will not return any error and do nothing.
  df1.withColumnRenamed("Emp ID", "emp_id").show()
  //df1.withColumnRenamed($"Emp ID", "emp_id").show()//will not take $ for cols

  //change column datatype
  df1.select($"Emp ID".cast("Integer"))
  df1.selectExpr("CAST(Gender as STRING) AS Gender")

  //concat two cols
  df1.select($"EMP ID", concat(col("First Name"), lit(' '),
    col("Last Name")).as("EMP Name")).show()

  //add columns
  df1.withColumn("EMP name", concat(col("First Name"), lit(' '),
    col("Last Name")))
  df1.withColumn("EMP name", concat($"First Name", $"Last Name"))
  df1.withColumn("EMP name", concat_ws(" ", $"First Name", $"Last Name"))
  df1.withColumn("EMP name", expr("concat(First Name, ,Last Name)"))

  //drop cols--if a column which doesn't exists is given then spark will not return any error and do nothing.
  df1.withColumn("EMP name", concat(col("First Name"), lit(' '),
    col("Last Name"))).drop("First Name").drop("Last Name").show()

  //total count() of females and males
  df1.repartition(4)
  val df2 = df1.groupBy("Gender").count()
  df2.show()

  df1.withColumn("Duplicate", count("*")
    .over(Window.partitionBy("Emp ID")))
    .select("*")
    .where(col("Duplicate") > 1)
    .show()

  //joins
  val df1a = spark.read
    .option("inferSchema", true)
    .option("header", true)
    .csv(path = "data/username.csv")
  df1a.show(10)

  val df2a = spark.read
    .option("inferSchema", true)
    .option("header", true)
    .csv(path = "data/username-password-recovery-code.csv")
  df2a.show(10)

  //inner join
  val innerJoinDF = df1a.join(df2a, df1a.col("Identifier") === df2a.col("Identifier"), "inner")
  innerJoinDF.show()

  val rightJoinDF = df1a.join(df2a, df1a.col("Identifier") === df2a.col("Identifier"), "rightouter").select("*")
  rightJoinDF.show()

  val leftJoinDF = df1a.join(df2a, df1a.col("Identifier") === df2a.col("Identifier"), "leftouter")
  leftJoinDF.show()

  val fullJoinDF = df1a.join(df2a, df1a.col("Identifier") === df2a.col("Identifier"), "fullouter")
  fullJoinDF.show()

  val semiJoinDF = df1a.join(broadcast(df2a), df1a.col("Identifier") === df2a.col("Identifier"), "semi")
  println(semiJoinDF.rdd.toDebugString)
  semiJoinDF.show()

  val antiJoinDF = df1a.join(df2a, df1a.col("Identifier") === df2a.col("Identifier"), "anti")
  antiJoinDF.show()


  val d1 = spark.read
    .option("inferSchema", true)
    .option("header", true)
    .option("delimiter", " | ")
    .csv(path = "data/df1.csv")
  d1.show()


  val d2 = spark.read
    .option("inferSchema", true)
    .option("header", true)
    .option("delimiter", " | ")
    .csv(path = "data/df2.csv")
  d2.show()

  val semiJoin = d1.join(d2, d1.col("userName") === d2.col("userName"), "rightouter")
    .select(d2.col("userName"), d2.col("address"), d2.col("phone"), d2.col("Date"))
  semiJoinDF.show()

  val antiJoin = d1.join(d2, d1.col("userName") === d2.col("userName"), "anti")
  antiJoinDF.show()
  val resDF = semiJoin.union(antiJoin)
  resDF.show()

  //pivoting a table
  val studMarksDF = spark.read
    .option("inferSchema", true)
    .option("header", true)
    .option("delimiter", "|")
    .csv(path = "data/marks.csv")

  val pivotTbl = studMarksDF.groupBy("ROLLNO").pivot("SUBJECT").max("MARKS")

  pivotTbl.show()


  //window function
  case class Employee(employee_name: String, department: String, salary: Float)

  val simpleData = Seq(new Employee("James", "Sales", 3000),
    new Employee("Michael", "Sales", 4600),
    new Employee("Robert", "Sales", 4100),
    new Employee("Maria", "Finance", 3000),
    new Employee("James", "Sales", 3000),
    new Employee("Scott", "Finance", 3300),
    new Employee("Jen", "Finance", 3900),
    new Employee("Jeff", "Marketing", 3000),
    new Employee("Kumar", "Marketing", 2000),
    new Employee("Saif", "Sales", 4100)
  )
  val df = simpleData.toDF()
  df.show()

  val windowSpec = Window.partitionBy("department").orderBy("salary")
  df.withColumn("row_number", row_number.over(windowSpec))
    .show()

  df.withColumn("rank", rank().over(windowSpec))
    .show()

  df.withColumn("dense_rank", dense_rank().over(windowSpec))
    .show()


  val dfa = Seq((1, "a"), (2, "b"), (3, "c")).toDF("key", "value")
  val dfb = Seq((1, "a")).toDF("key", "value")

  dfb.write.mode("append").insertInto("raw.test")
  spark.stop()

  spark.stop()
}