package Project1

import org.apache.spark.sql.SparkSession

object Hive {

  def main(args: Array[String]): Unit = {
    connect()
  }

  private var spark: SparkSession = _

  def connect(): Unit = {

  }

  def displayAPIQueries(selectedQuery: String): Unit = {
    //DONE: Use something similar to a switch statement to run/ set up each query


  }
}
