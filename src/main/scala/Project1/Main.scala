package Project1


import scala.annotation.tailrec
import scala.sys.exit


object Main {
  private var bool : Boolean = false
  private var admin : Boolean = false
  private var currentUser = ""
  private var otherUser = ""

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



    //Spark.connect()
//    Spark.updateTable()
//    Spark.displayUsers()

    //Spark.showNonAdmins()
    //mysqlDatabase.connectionTest()
//    mainStartUpMenu()


    //val tttt = mysqlDatabase.checkifExists("ogg")
    val listr = List("y","n")
        do {
          println("Enter y/n -> ")
          val input = scala.io.StdIn.readLine().toLowerCase()
          bool = listr.contains(input)
          if ('y' == input){

          }
          else if ('n' == input){

          }

        }while(!bool)








  }



  @tailrec
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


  @tailrec
  def CreateNewAccount(): Unit = {
    println("Create an account to access database")
    println("Please enter in your first name")
    val firstName = scala.io.StdIn.readLine()
    println("Please enter in your last name")
    val lastName = scala.io.StdIn.readLine()

    do {
      println("Please enter in your username")
      currentUser = scala.io.StdIn.readLine()
      bool = mysqlDatabase.checkifExists(currentUser)
    }while(bool)


    println("Please enter in your password")
    val usersPassword = scala.io.StdIn.readLine()
    val adminAuth = 0


    val newAccount = mysqlDatabase.createUser(firstName, lastName, currentUser, usersPassword, adminAuth)
    if (newAccount == 1) {
      println("You have successfully created an account")
      mainStartUpMenu()
    } else mainStartUpMenu()
    CreateNewAccount()
  }




  @tailrec
  def login(): Unit = {
    println("Please input user info")
    println("Please enter username")
    currentUser = scala.io.StdIn.readLine()
    println("Please enter password")
    val password = scala.io.StdIn.readLine()

    val validLogin = mysqlDatabase.validateLogin(currentUser, password)

    println(validLogin)

    if(validLogin){
      println("correct input going to user menu")
      val admin = mysqlDatabase.validateAdmin(currentUser)

      if (admin){adminMenu()}
      else {userMenu()}
    }

    else{
      println("incorrect login info. Try again")
      login()
    }
  }


  @tailrec
  def adminMenu(): Unit = {
    println("YOu have arrived at the Admin menu, Admin, welcome master")
    println("Master what would oyu like me to do? Please select an option 1-5")
    println {
      "Option 1: Make a new Admin\n" +
        "Option 2: Change Username\n" +
        "Option 3: Change Name\n" +
        "Option 4: Change Password\n" +
        "Option 5: Go to data \n" +
        "Option 6: Logout\n" +
        "Option 7: Delete Account\n +" +
        "Option 8: exit app"
    }

    val option = scala.io.StdIn.readLine()
    option match {
      case "1" => upAdmin()
      case "2" => changeUsername()
      case "3" => changeName()
      case "4" => changePassword()
      case "5" =>
      case "6" => logout()
      case "7" => deleteUser()
      case "8" => close()
      case _ => mainStartUpMenu()
    }
    adminMenu()
  }



  def userMenu(): Unit = {



  }


  def upAdmin(): Unit = {
    println("Admin elevation")
    println("Current Admins")
    Spark.showAdmins()
    println("Other Users")
    Spark.showNonAdmins()
    do {
      println("Please enter username of user to elevate to admin")
      otherUser = scala.io.StdIn.readLine()
      bool = mysqlDatabase.checkifExists(otherUser)
    }while(!bool)

    mysqlDatabase.elevate2Admin(otherUser)
    println("user is now admin")
  }

  def changeUsername(): Unit = {
    println("Changing your current username => ")
    mysqlDatabase.showUsername(currentUser)
    do {
      println("Please enter in your new username")
      otherUser = scala.io.StdIn.readLine()
      bool = mysqlDatabase.checkifExists(otherUser)
    }while(bool)

    mysqlDatabase.updateUsername(currentUser,otherUser)
  }

  def changeName(): Unit = {
    println("Current Name -> ")
    mysqlDatabase.showUser(currentUser)
    println("Input New First Name")
    val fname = scala.io.StdIn.readLine()

    println("Input New Last Name")
    val lname = scala.io.StdIn.readLine()

    mysqlDatabase.updateName(fname,lname,currentUser)
  }



  def changePassword(): Unit = {
    print("Changing password for current username => ")
    mysqlDatabase.showUsername(currentUser)
    do {
      println("Please enter your old password")
      otherUser = scala.io.StdIn.readLine()
      bool = mysqlDatabase.validateLogin(currentUser,otherUser)
    }while(!bool)

    println("Enter new password")
    val pass = scala.io.StdIn.readLine()

    mysqlDatabase.updatePassword(currentUser,pass)
  }

  def logout(): Unit = {
    currentUser = ""
    otherUser = ""
    mainStartUpMenu()
  }


  def deleteUser(): Unit = {
    println("Are you sure about this")
    val listr = List("y","n")
    do {
      println("Enter y/n -> ")
      val input = scala.io.StdIn.readLine().toLowerCase()
      bool = listr.contains(input)
      if ('y' == input){

      }
      else if ('n' == input){
        if(admin){
          adminMenu()
        }
        else{
          userMenu()
        }
      }
    }while(!bool)
    mysqlDatabase.deleteUser(currentUser)
    println("goodbye")
    mainStartUpMenu()

  }


}
