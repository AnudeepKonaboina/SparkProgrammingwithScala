package com.gihub.scala

import org.apache.log4j.{Level, Logger}

//sample example of creating an object class and a main method to print Hello world.
object HelloWorld {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("com.github").setLevel(Level.ERROR)
    println("Hello world! ")

  }
}
