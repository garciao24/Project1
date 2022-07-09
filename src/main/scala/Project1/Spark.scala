package Project1

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession

object Spark {


  private var spark: SparkSession = _
  private var userDf: DataFrame = _
  private var dataDf: DataFrame = _
  def connect(): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\hadoop3")
    spark = SparkSession
      .builder()
      .appName("Spark api")
      .config("spark.master", "local[*]")
      .enableHiveSupport()
      .getOrCreate()
    println("created spark session")

    spark.sparkContext.setLogLevel("ERROR")
    spark.sql("Set hive.exec.dynamic.partition.mode=nonstrict")

    //dataDf = spark.read.option("delimiter", ",").option("inferSchema","true").option("header", "true").csv(path = "C:\\input\\tmp.csv")
    val df = spark.read.option("delimiter", ",").option("inferSchema","true").option("header", "true").csv(path = "C:\\input\\tmp.csv")

    df.createOrReplaceTempView("Main")




























  }

  def updateTable():Unit = {
    val url = "jdbc:mysql://localhost:3306/usersdb"
    val user = "root"
    val pass = "j6Av6mTSzr#6R"
    userDf=spark.read.format("jdbc").option("url",url)
      .option("dbtable","users").option("user",user)
      .option("password",pass).load()
  }

  def displayUsers():Unit = {
    updateTable()
    userDf.createOrReplaceTempView("usersView")
    val newTable = spark.sql("SELECT Firstname, Lastname, Username FROM usersView ")
    newTable.show()
  }

  def showNonAdmins():Unit = {
    updateTable()
    userDf.createOrReplaceTempView("usersView")
    val newTable = spark.sql("SELECT Firstname, Lastname, Username FROM usersView WHERE AdminPriv = 0")
    newTable.show()
  }

  def showAdmins():Unit = {
    updateTable()
    userDf.createOrReplaceTempView("usersView")
    val newTable = spark.sql("SELECT Firstname, Lastname, Username FROM usersView WHERE AdminPriv = 1")
    newTable.show()
  }







  def APIQueries(selectedQuery: String): Unit = {
    //DONE: Use something similar to a switch statement to run/ set up each query
    selectedQuery match {
      case "1" =>
      case "2" =>
      case "3" =>
      case "4" =>
      case "5" =>
      case "6" =>
      case "7" =>

    }
  }
}
