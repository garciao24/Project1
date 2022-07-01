package Project1

import Project1.Hive.spark
import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}

object Main {
  def main(args: Array[String]): Unit = {
    //println("Hello world!")
    //meteorite data
    //val data = requests.get(" https://data.nasa.gov/resource/y77d-th95.json")


    //    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    //    val result = hc.sql("SELECT * FROM projectone.salaries_extra;")
    //    result.show()

    ///////////
        val data = requests.get(" https://data.cdc.gov/resource/9bhg-hcku.json")
        val text = data.text()
        val json = ujson.read(text)
        os.write(os.pwd/"api.json",json)//test hdfs figure it out

        val spark = SparkSession
          .builder()
          .appName("Hello Hive")
          .config("spark.master", "local[*]")
          .enableHiveSupport()
          .getOrCreate()
        Logger.getLogger("org").setLevel(Level.ERROR)
        println("created spark session")
        val df = spark.read.json(path = "tmp.json")
        df.show()
    ////////////////


    //mainStartUpMenu()

    //mysqlDatabase.connect()
    //mysqlDatabase.createUser("demo","test","yeahboi","5678",0)
  }


  def mainStartUpMenu(): Unit = {

    println {
      "Option 1: Create New Account \n" +
        "Option 2: Login as an existing User \n" +
        "Option 3: Exit the app (We will be sad to see you go, but we get it)"
    }
    val option = scala.io.StdIn.readLine()
    option match {
      case "1" => println("test option 1")
      case "2" => println("test option 2")
      case "3" => println("test option 3")
      case _ => mainStartUpMenu()


    }
  }



}
