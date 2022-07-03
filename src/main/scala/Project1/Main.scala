package Project1


import Project1.Hive.spark
import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}
import ujson.IndexedValue.{False, True}

import scala.sys.exit

object Main {
  private var bool : Boolean = false
  private var currentUser = ""

  def main(args: Array[String]): Unit = {
    //println("Hello world!")
    //meteorite data
    //val data = requests.get(" https://data.nasa.gov/resource/y77d-th95.json")


    //    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    //    val result = hc.sql("SELECT * FROM projectone.salaries_extra;")
    //    result.show()

    ///////////
//        val data = requests.get(" https://data.cdc.gov/resource/9bhg-hcku.json")
//        val text = data.text()
//        val json = ujson.read(text)
//        os.write(os.pwd/"api.json",json)//test hdfs figure it out
//
//        val spark = SparkSession
//          .builder()
//          .appName("Hello Hive")
//          .config("spark.master", "local[*]")
//          .enableHiveSupport()
//          .getOrCreate()
//        Logger.getLogger("org").setLevel(Level.ERROR)
//        println("created spark session")
//        val df = spark.read.json(path = "tmp.json")
//        df.show()
    ////////////////


    //mainStartUpMenu()
    //mysqlDatabase.showUser("ogarcia2834")


    //mysqlDatabase.createUser("demo","test","yeahboi","5678",0)
//    var validLogin = mysqlDatabase.validateLogin("ogarcia24","1234")
//    println(validLogin)


    mysqlDatabase.connect()
//    mainStartUpMenu()


    //val tttt = mysqlDatabase.checkifExists("ogg")


    CreateNewAccount()



  }



  def mainStartUpMenu(): Unit = {

    println {
      "Option 1: Login as an existing User\n" +
        "Option 2: Create New Account  \n" +
        "Option 3: Exit the app "
    }
    val option = scala.io.StdIn.readLine()
    option match {
      case "1" => login()
      case "2" => CreateNewAccount()
      case "3" => close()
      case _ => mainStartUpMenu()


    }
  }
  def close(): Unit = {
    mysqlDatabase.disDB()
    exit(0)
  }


  def CreateNewAccount(): Unit = {
    println("YOu have chosen option 2, create an account to access database")
    println("This is the beginning of the form you will have ot fill out to create an account.")
    println("Please enter in your first name")
    var firstName = scala.io.StdIn.readLine()
    println("Please enter in your last name")
    var lastName = scala.io.StdIn.readLine()


    do {
      println("Please enter in your username")
      currentUser = scala.io.StdIn.readLine()
      bool = mysqlDatabase.checkifExists(currentUser)
      println(bool)

    }while(bool)


    println("Please enter in your password")
    var usersPassword = scala.io.StdIn.readLine()
    var adminAuth = 0



    var newAccount = mysqlDatabase.createUser(firstName, lastName, currentUser , usersPassword, adminAuth)
    if (newAccount == 1) {
      println("You have successfully created an account")
      mainStartUpMenu()
    } else (mainStartUpMenu())
    CreateNewAccount()
  }




  def login(): Unit = {
    println("Please input user info")
    println("Please enter username")
    var username = scala.io.StdIn.readLine()
    println("Please enter password")
    var password = scala.io.StdIn.readLine()

    var validLogin = mysqlDatabase.validateLogin(username,password)

    println(validLogin)

    if(validLogin){
      println("correct input going to user menu")
      var admin = mysqlDatabase.validateAdmin(username)

      ///if admin = 1
      //then admin menu
      //otherwise regular user menu
      if (admin){

      }
      else {

      }




    }
    else{
      println("incorrect login info. Try again")
      login()
    }
  }


  def adminMenu(): Unit = {
    println("YOu have arrived at the Admin menu, Admin, welcome master")
    println("Master what would oyu like me to do? Please select an option 1-5")
    println {
      "Option 1: Make a new Admin\n" +
        "Option 2: Change Name\n" +
        "Option 3: Change Username\n" +
        "Option 4: Change Password\n" +
        "Option 5: Go to data \n" +
        "Option 6: Logout\n" +
        "Option 7: exit app\n"
    }

    val option = scala.io.StdIn.readLine()
    option match {
      case "1" =>
      case "2" =>
      case "3" =>
      case "4" =>
      case "5" =>
      case "6" =>
      case "7" =>
      case _ => mainStartUpMenu()
    }
    adminMenu()
  }





  def userMenu(): Unit = {



  }


}
