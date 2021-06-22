# SparkProgrammingwithScala

Spark is a general purpose unified engine which is faster than mapreduce.

Hadoop - (2004 to 2013) --(Hive/Impala/Hadoop/Drill/Tez)-hadoop processing tools 
Spark  -(2013) --(unified compute engine which does all of these)
                 (spark sql mllib graphx streaming)
Cluster Managers of running saprk- 
                        1.local mode --one machine/jvm with driver and executor runing.
                        2.Mesos ---spark was originally created on top of mesos cluster manager
                        3.standalone --sparks own cluster manager
                        4.Yarn --spark can run on top of YARN -most commony used clustermanager for spark.
                        5.Kubernetes -- spark on kubernetes    
All the avbove modes can be co-ordinated by zookeeper                                    

Spark doesnt have any storage management.It's just an execution engine to process your workload.

Hadoop    spark
-----------------
MR        spark
YARN      Mesos
HDFS      tachyon
hive      sql
mahout    sparkmlib
storm     streaming


-->In MR there is data persitence which makes it slow . Reads processes and stores back to disk and so on.
-->Spark does in memory processing.Intermidiate data is not written back to disk.This it is (10 to 100 times) faster than MR.

100TB data took 25 min in spark while MR took 72 min.

Sark Architecture:
-------------------
There are 2 components 1.Driver(Master) 2.Executor(Slaves)

Local -- JVM--contains both Driver and Executor(container)

![image](https://user-images.githubusercontent.com/59328701/122972837-33195300-d3ae-11eb-835e-8429cdce469c.png)


cluster --- executors are launched across data nodes and processed.
There are two deploy modes in cluster:
1. client mode -driver euns on client machine and executors run on the cluster.
2. cluster mode -driver and executor both run on cluster 

![image](https://user-images.githubusercontent.com/59328701/122972685-08c79580-d3ae-11eb-8f45-ba83104909a1.png)


Spark offers you 3 datastructures to load data from any source
1.dataframe
2.datatset
3.RDD

Dataframe-
1.2-d table like DS same as pandas df,with columnname and datatypes with schema.
2.Distributed
3.Composed of bunch of partitions.
4.Immutable dtatastructure 


RDD's:
------
RDD's are building blocks of spark .They represent the data inmemory(RAM/containers/executors).RDD's are immutable .
Once data is in RDD there are two types of opetations that can be done on an RDD.
1.Transformations -- functions that we apply on RDD that creates a new RDD.
    ->types: Narrow-map/filter -can be appplued individually on a partition
             wide -reduce -shuffle is required
2.Actions -- spark jobs are started only when an action is called.

Writing code using RDD'S Transformations and actions ,the problem is spark will not be able to optimize data as they dont have scrict schema.
So for working with structured data RDD's are not very powerful.For that spark sql has come into picture.For stuctured data spark kows what operations 
can be done and optimized based on the data.
Spark SQL is mostly used than RDD'S. It uses Dataframes for processing data.Dataframes are table like structure with Dataset[Row]

Jobs  --> Stages -->tasks

A job is started when an action is called.Every job has atleast one stage and each stage must have atleast one task.
A new stage is  created when a wide transformation is called.
A new task is created on each tranformations within a stage for a df partition.

All the above are shown in something acalled as a DAG which is created by spark when a job is submitted using something called CBO(Catalyst based optimizer)

In spark if the file is read from hadoop and processed ,the numner of partitions is equal to the number of blocks but while running locally the no of parttions is number of executor cores 
within that machine.


Datasets vs Dataframes:
=======================
RDDs Apis in 0.x were very fundamental ad were not developer friendly.
This resulted in Dataframe in spark 1.3

RDDs had complile time type safety / Dataframe used generics and types wer infered at run time.
Lambda based transfrormations / expression based tx. with optimization benifits

Datasets were introduced in 1.6--combination of df and rdds without loosing optimizations.

Now Dataframe is an alias for Dataset[Row].Spark implicit conversion converts df to ds and vice versa.
Using data set provides compile time saftey with optimizations also having lambda based transofrmations.

Dataframe is a bunch of partitions read from a distributed storage and sored in a logical in-memory data-structure.

Dataframe Operations:
=====================
Dataframe is nothing but a Dataset[Row] that means each row in a dataframe is an object of type ROW.
Dataframe cols are objects of type Column,

1. select
```
df.select("col1","col2").show()
df.select(column("col1"),col("col1"),$"col3").show()//all correct ways of refering to columns in DF.
df.select(column("col1"),col("col1"),$"col3",expr("select a as newcol")).show()
```
2. select and where
```
df.select("col1","col2").where("col2 < 10").show()
```
3. add column with expr
```
df.withColumn("new_col",some_fun).withColumn("id",monotonically_increasing_id).withColumn("col4",expr(""<any sql expression>""))
```
4. drop
```
df.drop("col1","col2")
df.dropDuplicates("col1")
```
5. renamecolumn
```
df.withColumnRenamed("<old>","new>")
```
6. aggregate functions
```
df.select(
      count("*").as("cnt"),
      sum("Quantity").as("total_qty"),
      avg("price").as("price"),
      countDistinct("id_no").as("distinct")
    ).show()

    
//aggregate
df.selectExpr(
      "count(*) as count",
      "count(quantity) as total_qty",
    ).show()
```

Joins in spark :
There are two types of joins supported by spark.

1. Shuffle Join 
Most commonly used join type in spark.The transfer of data from map exchange to reduce exchange is called as Shuffle opertion and is the main reason why spark joins are slow.
Tuning performance of joins is all about optimizing shuffle operations.`spark.sql.shuffle.partitions` configures the number of partitions that are used when shuffling data for joins or aggregations. Default value is 200 i.e..,once you perform an aggregation or join on a dataframe the result of a shuffle operation will result in default 200 partitions.

Difference between adding a repartition and shuffle partition while performing a job vs not adding them.Below is an optimized job:
```scala
import com.github.edge.roman.spear.SpearConnector
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SaveMode

Logger.getLogger("com.github").setLevel(Level.INFO)
val targetProps = Map(
    "driver" -> "org.postgresql.Driver",
    "user" -> "postgres_user",
    "password" -> "mysecretpassword",
    "url" -> "jdbc:postgresql://postgres:5432/pgdb"
  )

spark.conf.set("spark.sql.shuffle.partitions", 4)

val csvJdbcConnector = SpearConnector
    .createConnector(name="CSVtoPostgresConnector")
    .source(sourceType = "file", sourceFormat = "csv")
    .target(targetType = "relational", targetFormat = "jdbc")
    .getConnector   
 
csvJdbcConnector
  .source(sourceObject="file:///opt/spear-framework/data/us-election-2012-results-by-county.csv", Map("header" -> "true", "inferSchema" -> "true"))
  .saveAs("__tmp__")
  .repartition(4)
  .transformSql(
    """select state_code,party,
      |sum(votes) as total_votes
      |from __tmp__
      |group by state_code,party""".stripMargin)
  .targetJDBC(objectName="mytable", props=targetProps, saveMode=SaveMode.Overwrite)
```
![image](https://user-images.githubusercontent.com/59328701/122783479-3be72780-d2cf-11eb-8c67-a856008a55ff.png)


2. Broadcast Join 
