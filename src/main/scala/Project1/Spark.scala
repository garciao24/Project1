package Project1

import Project1.mysqlDatabase.{connect, connection}
import org.apache.spark.sql
import org.apache.spark.sql.SparkSession

object Spark {


  private var spark: SparkSession = _
  private var sourceDf: sql.DataFrame = _
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

//    spark.sql("DROP TABLE IF EXISTS bestbooks_table")
//    spark.sql("CREATE TABLE IF NOT EXISTS bestbooks_table(Name String, Author String, UserRating double, Reviews Int, Price Int, Year Int, Genre String) Row format delimited fields terminated by ',' stored as textfile")
//
//
//    spark.sql("Load data Local Inpath 'bestbooks_table_data.csv' into table bestbooks_table")
  }

  def updateTable():Unit = {
    val url = "jdbc:mysql://localhost:3306/usersdb"
    val user = "root"
    val pass = "j6Av6mTSzr#6R"
    sourceDf=spark.read.format("jdbc").option("url",url)
      .option("dbtable","users").option("user",user)
      .option("password",pass).load()
  }

  def displayUsers():Unit = {
    updateTable()
    sourceDf.createOrReplaceTempView("usersView")
    val newTable = spark.sql("SELECT Firstname, Lastname, Username FROM usersView ")
    newTable.show()
  }

  def showNonAdmins():Unit = {
    updateTable()
    sourceDf.createOrReplaceTempView("usersView")
    val newTable = spark.sql("SELECT Firstname, Lastname, Username FROM usersView WHERE AdminPriv = 0")
    newTable.show()
  }

  def showAdmins():Unit = {
    updateTable()
    sourceDf.createOrReplaceTempView("usersView")
    val newTable = spark.sql("SELECT Firstname, Lastname, Username FROM usersView WHERE AdminPriv = 1")
    newTable.show()
  }










  def displayAPIQueries(selectedQuery: String): Unit = {



  }
}
