package Project1
import scala.annotation.tailrec
import scala.sys.exit


object Main {
  private var bool : Boolean = false
  private var admin : Boolean = false
  private var currentUser = ""
  private var otherUser = ""

  def main(args: Array[String]): Unit = {

    mysqlDatabase.connect()
    Spark.connect()
    mainStartUpMenu()
    //Spark.query5()

//    val fname = "Oscar"
//    val lname = "Garcia"
//    currentUser = "ogarcia2834"
//    mysqlDatabase.updateName(fname,lname,currentUser)

  }



  @tailrec
  def mainStartUpMenu(): Unit = {

    println("\u001B[35m--------Main Menu Log in--------\u001B[0m")
    println {
      "\u001B[32mOption 1:\u001B[34m Login as an existing User\n" +
        "\u001B[32mOption 2:\u001B[34m Create New Account  \n" +
        "\u001B[32mOption 3:\u001B[34m Exit the app "
    }
    print("\u001B[32mInput option -> \u001B[0m")
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
    Spark.close()
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
    print("Please enter username: ")
    currentUser = scala.io.StdIn.readLine()
    print("Please enter password: ")
    val password = scala.io.StdIn.readLine()

    val validLogin = mysqlDatabase.validateLogin(currentUser, password)

    //println(validLogin)

    if(validLogin){
      println("\u001B[33mcorrect input going to user menu\u001B[0m")
      val admin = mysqlDatabase.validateAdmin(currentUser)

      if (admin){adminMenu()}
      else {userMenu()}
    }

    else{
      println("incorrect login info. Try again")
      login()
    }
  }


  def adminMenu(): Unit = {
    println("\u001B[35m--------Admin menu--------\u001B[0m")
    println("\u001B[32mPlease select an option 1-8")
    print("Welcome -> ")
    mysqlDatabase.showUser(currentUser)
    println("\nUser: "+currentUser)
    println {
      "Option 1: \u001B[34mMake a new Admin\n" +
        "\u001B[32mOption 2: \u001B[34mChange Username\n" +
        "\u001B[32mOption 3: \u001B[34mChange Name\n" +
        "\u001B[32mOption 4: \u001B[34mChange Password\n" +
        "\u001B[32mOption 5: \u001B[34mGo to data \n" +
        "\u001B[32mOption 6: \u001B[34mLogout\n" +
        "\u001B[32mOption 7: \u001B[34mDelete Account\n" +
        "\u001B[32mOption 8: \u001B[34mexit app\u001B[0m"
    }

    print("\u001B[32mInput option -> \u001B[0m")
    val option = scala.io.StdIn.readLine()
    option match {
      case "1" => upAdmin()
      case "2" => changeUsername()
      case "3" => changeName()
      case "4" => changePassword()
      case "5" => dataChoiceAdmin()
      case "6" => logout()
      case "7" => deleteUser()
      case "8" => close()
      case _ => adminMenu()
    }
    adminMenu()
  }



  def userMenu(): Unit = {
    println("\u001B[35m---Regular User Menu---\u001B[0m")
    println("\u001B[32mPlease select an option 1-7\u001B[0m")
    print("Welcome -> ")
    mysqlDatabase.showUser(currentUser)
    println("\nUser: "+currentUser)
    println {
        "\u001B[32mOption 1:\u001B[34m Change Username\n" +
        "\u001B[32mOption 2:\u001B[34m Change Name\n" +
        "\u001B[32mOption 3:\u001B[34m Change Password\n" +
        "\u001B[32mOption 4:\u001B[34m Go to data \n" +
        "\u001B[32mOption 5:\u001B[34m Logout\n" +
        "\u001B[32mOption 6:\u001B[34m Delete Account\n" +
        "\u001B[32mOption 7:\u001B[34m exit app\u001B[0m"
    }

    print("\u001B[32mInput option -> \u001B[0m")
    val option = scala.io.StdIn.readLine()
    option match {
      case "1" => changeUsername()
      case "2" => changeName()
      case "3" => changePassword()
      case "4" => dataChoiceRegular()
      case "5" => logout()
      case "6" => deleteUser()
      case "7" => close()
      case _ => userMenu()
    }
    userMenu()
  }




  def dataChoiceAdmin(): Unit = {
    println("-------------------------------Queries-------------------------------")
    println("Choice 1: Given a random user given state, display all criteria of deceased")
    println("Choice 2: Show the Top States with Covid Deaths")
    println("Choice 3: Display the total covid-19 deaths by Age Group with user input Sex 3that has been recorded")
    println("Choice 4: See Death toll by month since the beginning of the pandemic to current")
    println("Choice 5: What is the total death not retaining to covid-19 by state and up to current")
    println("Choice 6: What is the latest Data depending on choice of State")
    println("Choice 7: Go Back ")

    print("\u001B[32mInput option -> \u001B[0m")
    var choiceQuery = scala.io.StdIn.readLine()
    choiceQuery match {
      case "1" => Spark.query1()
      case "2" => Spark.query2()
      case "3" => Spark.query3()
      case "4" => Spark.query4()
      case "5" => Spark.query5()
      case "6" => Spark.query6()
      case "7" => goBack()
      case _ =>  dataChoiceAdmin()
    }
    dataChoiceAdmin()
  }


  def dataChoiceRegular():Unit = {
    println("-------------------------------Queries-------------------------------")
    println("Choice 1: See Death toll by month since the beginning of the pandemic to current")
    println("Choice 2: What is the total death not retaining to covid-19 by state and up to current")
    println("Choice 3: What is the latest Data depending on choice of State")
    println("Choice 4: Go Back ")

    print("\u001B[32mInput option -> \u001B[0m")
    var choiceQuery = scala.io.StdIn.readLine()
    choiceQuery match {
      case "1" => Spark.query4()
      case "2" => Spark.query5()
      case "3" => Spark.query6()
      case "4" => goBack()
      case _ =>  dataChoiceRegular()
    }
    dataChoiceRegular()
  }


  def goBack():Unit = {
    if(admin){
      adminMenu()
    }
    else{
      userMenu()
    }
  }


  def upAdmin(): Unit = {
    println("Admin elevation")
    println("Current Admins")
    Spark.showAdmins()
    println("Other Users")
    Spark.showNonAdmins()
    do {
      print("Please enter username of user to elevate to admin: ")
      otherUser = scala.io.StdIn.readLine()
      bool = mysqlDatabase.checkifExists(otherUser)
    }while(!bool)

    mysqlDatabase.elevate2Admin(otherUser)
    println("User is now admin")
    println("Current Admins")
    Spark.showAdmins()
  }

  def changeUsername(): Unit = {
    print("Changing your current username => ")
    mysqlDatabase.showUsername(currentUser)
    do {
      print("Please enter in your new username(One that does not exist): ")
      otherUser = scala.io.StdIn.readLine()
      bool = mysqlDatabase.checkifExists(otherUser)
    }while(bool)
    println(bool)

    mysqlDatabase.updateUsername(currentUser,otherUser)
    currentUser=otherUser
  }

  def changeName(): Unit = {
    print("Current Name -> ")
    mysqlDatabase.showUser(currentUser)
    print("\nInput New First Name: ")
    val fname = scala.io.StdIn.readLine()

    print("Input New Last Name: ")
    val lname = scala.io.StdIn.readLine()

    mysqlDatabase.updateName(fname,lname,currentUser)
  }



  def changePassword(): Unit = {
    print("Changing password for current username => ")
    mysqlDatabase.showUsername(currentUser)
    do {
      print("Please enter your old password: ")
      otherUser = scala.io.StdIn.readLine()
      bool = mysqlDatabase.validateLogin(currentUser,otherUser)
    }while(!bool)

    print("Enter new password: ")
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
      print("Enter y/n -> ")
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
