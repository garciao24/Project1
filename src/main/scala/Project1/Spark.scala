package Project1


import org.apache.spark.sql.{DataFrame, Encoders, SparkSession}

object Spark {
  private var bool : Boolean = false
  private var spark: SparkSession = _
  private var userDf: DataFrame = _
  private var df: DataFrame = _
  def connect(): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\hadoop3")
    spark = SparkSession
      .builder()
      .appName("Spark api")
      .config("spark.master", "local[*]")
      .enableHiveSupport()
      .getOrCreate()
    println("created spark session\n")

    spark.sparkContext.setLogLevel("ERROR")
    spark.sql("Set hive.exec.dynamic.partition.mode=nonstrict")

    //dataDf = spark.read.option("delimiter", ",").option("inferSchema","true").option("header", "true").csv(path = "C:\\input\\tmp.csv")



    try{
      df = spark.read.option("delimiter", ",").option("inferSchema","true").option("header", "true").csv("hdfs://localhost:9000/user/ogarcia2834/project1/covidData.csv")
    }
    catch {
      case e: Exception => e.printStackTrace()
        println("Something went wrong, check Hadoop")
    }

    //val df = spark.read.option("delimiter", ",").option("inferSchema","true").option("header", "true").csv(path = "C:\\input\\tmp.csv")

    df.createOrReplaceTempView("Main")
    //spark.sql("SELECT * FROM Main").show()


    spark.sql("DROP TABLE IF EXISTS Query1")
    spark.sql("create table Query1(`Start Date` String,`End Date` String,Sex String,`Age Group` String,`COVID-19 Deaths` Int,`Pneumonia Deaths` Int,`Influenza Deaths` Int, `Pneumonia Int/Influenza/COVID-19 Deaths` Int,`Other Causes of Death` Int,`Total Deaths` Int) partitioned by (State STRING) row format delimited fields terminated by ',' stored as textfile")
    spark.sql("INSERT INTO TABLE Query1 (SELECT `Start Date`,`End Date`,Sex,`Age Group`,`COVID-19 Deaths`,`Pneumonia Deaths`,`Influenza Deaths`, `Pneumonia, Influenza, or COVID-19 Deaths`,`Total Deaths`-`Pneumonia, Influenza, or COVID-19 Deaths` AS `Other Causes of Death`,`Total Deaths`,State FROM Main WHERE Sex = 'All Sexes' AND Group = 'By Total' )")

    //spark.sql("SELECT `Start Date`,`End Date`,`Sex`,`State`,`Age Group`,`Covid-19 Deaths`,`Pneumonia Deaths`,`Influenza Deaths`,`Pneumonia Int/Influenza/COVID-19 Deaths`,`Other Causes of Death`,`Total Deaths` FROM Query1").show()

    spark.sql("DROP TABLE IF EXISTS Query2")
    spark.sql("create table Query2(`State` String,`Total Death` Int) row format delimited fields terminated by ',' stored as textfile")
    spark.sql("INSERT INTO TABLE Query2 (Select  State, MAX(`COVID-19 Deaths`) AS totalDeath from Main WHERE State != 'United States' Group by State Order By totalDeath DESC)")
    //spark.sql("Select * From Query2").show()


    spark.sql("DROP TABLE IF EXISTS Query3")
    spark.sql("create table Query3(`Age Group` String,`COVID-19 Deaths` Int) partitioned by (Sex String) row format delimited fields terminated by ',' stored as textfile")
    spark.sql("INSERT INTO TABLE Query3 (SELECT `Age Group`,`COVID-19 Deaths`,Sex FROM Main WHERE State = 'United States' AND Group = 'By Total'  )")
    //spark.sql("Select `Age Group`,Sex,`COVID-19 Deaths` From Query3 WHERE Sex = 'All Sexes'").show()


    spark.sql("DROP TABLE IF EXISTS Query4")
    spark.sql("create table Query4(Year Int,Month Int,`Total Covid-19 Death` Int) row format delimited fields terminated by ',' stored as textfile")
    spark.sql("INSERT INTO TABLE Query4 (SELECT Year, Month, SUM(`COVID-19 Deaths`) AS `Total Covid-19 Death` FROM Main WHERE Month IS NOT NULL GROUP BY Month,Year ORDER BY Year,Month ASC  )")
    //spark.sql("Select * From Query4").show(50)


    spark.sql("DROP TABLE IF EXISTS Query5")
    spark.sql("create table Query5(State String,SEX String,`Age Group` String,`Other Causes of Death` Int) row format delimited fields terminated by ',' stored as textfile")
    spark.sql("INSERT INTO TABLE Query5(SELECT State,Sex,`Age Group`,(`Total Deaths`-`COVID-19 Deaths`) AS `Other Causes of Death` From Main WHERE `Age Group` = 'All Ages' AND Sex = 'All Sexes' AND Group = 'By Total' ORDER BY `Other Causes of Death` ASC)")
    //spark.sql("Select * From Query5").show(50)


    spark.sql("DROP TABLE IF EXISTS Query6")
    spark.sql("create table Query6(`Start Date` String,`End Date` String,Year Int,Month Int,State String,Sex String,`Age Group` String,`COVID-19 Deaths` Int,`Pneumonia Deaths` Int,`Influenza Deaths` Int, `Pneumonia Int/Influenza/COVID-19 Deaths` Int,`Other Causes of Death` Int,`Total Deaths` Int) row format delimited fields terminated by ',' stored as textfile")
    spark.sql("INSERT INTO TABLE Query6(SELECT `Start Date`,`End Date`,Year,Month,State,Sex,`Age Group`,`COVID-19 Deaths`,`Pneumonia Deaths`,`Influenza Deaths`, `Pneumonia, Influenza, or COVID-19 Deaths`,`Total Deaths`-`Pneumonia, Influenza, or COVID-19 Deaths` ,`Total Deaths` FROM Main " +
      "WHERE Sex = 'All Sexes' AND Group = 'By Month' AND Year = '2022' AND `Age Group` = 'All Ages' Order BY Month DESC)")
    val d6 = spark.sql("Select * From Query6")


  }


  def close():Unit = {
    spark.close()
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

  def showAll():Unit = {
    updateTable()
    userDf.createOrReplaceTempView("usersView")
    val newTable = spark.sql("SELECT Firstname, Lastname, Username FROM usersView")
    newTable.show()
  }


  def query1():Unit = {
    var state = ""

    val datalist = spark.sql("SELECT DISTINCT State From Main ORDER BY State ASC")
    val listOne = datalist.as(Encoders.STRING).collectAsList

    println("Accepted State inputs")
    datalist.show(55)
    do {
      println("Please enter a state")
      state = scala.io.StdIn.readLine()
      bool = listOne.contains(state)
    }while(!bool)

    spark.sql(s"SELECT `Start Date`,`End Date`,`Sex`,`State`,`Age Group`,`Covid-19 Deaths`,`Pneumonia Deaths`,`Influenza Deaths`,`Pneumonia Int/Influenza/COVID-19 Deaths`,`Other Causes of Death`,`Total Deaths` FROM Query1 " +
            s"WHERE State = '$state' ").show()
  }

  def query2():Unit = {

    spark.sql("Select * From Query2").show()
  }

  def query3():Unit = {
    var input = ""
    val listOne = List("All Sexes","Female","Male")
    do {

      println("Please enter from the given choices => [\"All Sexes\",\"Female\",\"Male\"]")
      input = scala.io.StdIn.readLine()
      bool = listOne.contains(input)
    }while(!bool)
    spark.sql(s"Select `Age Group`,Sex,`COVID-19 Deaths` From Query3 WHERE Sex = '$input'").show()
  }

  def query4():Unit = {

    spark.sql("Select Year,CASE " +
      "WHEN Month = 1 THEN 'January' " +
      "WHEN Month = 2 THEN 'February' " +
      "WHEN Month = 3 THEN 'March' " +
      "WHEN Month = 4 THEN 'April' " +
      "WHEN Month = 5 THEN 'May' " +
      "WHEN Month = 6 THEN 'June' " +
      "WHEN Month = 7 THEN 'July' " +
      "WHEN Month = 8 THEN 'August' " +
      "WHEN Month = 9 THEN 'September' " +
      "WHEN Month = 10 THEN 'October' " +
      "WHEN Month = 11 THEN 'November' " +
      "WHEN Month = 12 THEN 'December' " +
      "ElSE Month " +
      "END AS `Month`,`Total Covid-19 Death`  From Query4").show(50)
  }


  def query5():Unit = {
    spark.sql("Select * From Query5").show(60)
  }

  def query6():Unit = {

    var state = ""

    val datalist = spark.sql("SELECT DISTINCT State From Main ORDER BY State ASC")
    val listOne = datalist.as(Encoders.STRING).collectAsList

    println("Accepted State inputs")
    datalist.show(55)
    do {
      println("Please enter a state")
      state = scala.io.StdIn.readLine()
      bool = listOne.contains(state)
    }while(!bool)

    spark.sql(s"Select * From Query6 WHERE State = '$state'").show(50)
  }

  def APIQueries(selectedQuery: String): Unit = {
    //DONE: Use something similar to a switch statement to run/ set up each query
    selectedQuery match {
      case "1" => query1()
      case "2" =>
      case "3" =>
      case "4" =>
      case "5" =>
      case "6" =>
      case "7" =>
      case _ =>

    }
  }
}
