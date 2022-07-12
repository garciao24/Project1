package Project1

import java.sql.{Connection, DriverManager, SQLException}
import scala.collection.mutable.ListBuffer
object mysqlDatabase {


  private var connection: Connection = _

  def connect(): Unit = {
    val url = "jdbc:mysql://localhost:3306/usersdb"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "j6Av6mTSzr#6R"

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      //println("MySQL CONNECTION IS GOOD")

    } catch {
      case e: Exception => e.printStackTrace()
        println("something is wrong")
    }

  }




  def createUser(FirstName: String, LastName: String, Username: String, Password: String, AdminPriv: Int): Int = {
    connect()
    var resultSet = 0
    var statement = connection.prepareStatement(s"Insert into users (Firstname, Lastname, Username, Password, AdminPriv) Values(?, ?, ?, ?, ?)")
    try {
      statement.setString(1, FirstName)
      statement.setString(2, LastName)
      statement.setString(3, Username)
      statement.setString(4, Password)
      statement.setInt(5, AdminPriv)
      resultSet = statement.executeUpdate()
      println("The user account has been created")
      //showAllUsers()
      resultSet
    }
    catch {
      case e: SQLException => e.printStackTrace()
        resultSet
    }
  }

  def showUser(Username: String): Unit = {
    connect()
    val statement = connection.prepareStatement(s"SELECT Firstname, Lastname FROM users WHERE Username = '$Username' ")
    try {
      val resultSet = statement.executeQuery()

      while (resultSet.next()) {
        print(resultSet.getString("Firstname"))
        print(" ")
        print(resultSet.getString("Lastname"))
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def showUsername(Username: String): Unit = {
    connect()
    val statement = connection.prepareStatement(s"SELECT Username FROM users WHERE Username = '$Username' ")
    try {
      val resultSet = statement.executeQuery()

      while (resultSet.next()) {
        print(resultSet.getString("Username"))
        print("\n")
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }





  def checkifExists(userCheck: String): Boolean = {
  connect()
    var bufferL = new ListBuffer[String]()

    val statement = connection.prepareStatement("SELECT Username FROM users")
    val resultSet = statement.executeQuery()
    while (resultSet.next()) {
      bufferL += resultSet.getString("Username")
    }
    val userList = bufferL.toList
    val check = userList.contains(userCheck)
    if (check) {//user is detected
      true
    } else {//User is not detected
      false
    }
  }






  def updateUsername(oldusername:String, NewUserName: String): Unit = {
    connect()
    val statement = connection.createStatement()
    try {
      statement.executeUpdate(s"UPDATE users SET Username = '$NewUserName' WHERE Username = '$oldusername'")
    } catch {
      case e: Exception => e.printStackTrace()
    }
    println("Username has been updated")
  }

  def updatePassword(username:String, password: String): Unit = {
    connect()
    val statement = connection.createStatement()

    try {
      statement.executeUpdate(s"UPDATE users SET password = '$password' WHERE Username = '$username'")
    } catch {
      case e: Exception => e.printStackTrace()
    }
    println("Username has been updated")

  }



  def updateName(newFirstName: String, newLastName: String,existingUser : String): Unit = {
    connect()
    val statement = connection.createStatement()
    try {
      statement.executeUpdate(s"UPDATE users SET Firstname = '$newFirstName', Lastname = '$newLastName'  WHERE Username = '$existingUser'")
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def elevate2Admin(selectedUser: String): Unit = {//decide whether to use id or unique username
    connect()
    val statement = connection.createStatement()
    val rsSet = statement.executeUpdate(s"UPDATE users SET AdminPriv = 1 where username = '$selectedUser'")

  }

  def deleteUser(delUser: String): Unit = {
    connect()
    val statement = connection.createStatement().executeUpdate(s"Delete From users where username = '$delUser'")

    if (statement == 0) {
      println("User does not exist, please try again")
    } else {
      println("User deleted")
    }
  }

  def validateLogin(usersUserName: String, usersPassword: String): Boolean = {
    connect()
    val statement = connection.prepareStatement(s"SELECT * From users WHERE Username = '$usersUserName' AND password = '$usersPassword'")
    var validUsername = statement.executeQuery()

    try {
      if (!validUsername.next()) {
        println("Username and Password are incorrect")
        //print("")
        false

      } else {
        println("Username and Password are correct.")
        //print("")
        true
      }
    }
  }

  def validateAdmin(usersUserName: String): Boolean = {
    connect()

    val statement = connection.prepareStatement(s"SELECT * From users WHERE Username = '$usersUserName' AND AdminPriv = '1'")
    var validUsername = statement.executeQuery()

    try {
      if (!validUsername.next()) {false}
      else {true}
    }
  }



  def disDB(): Unit = {

    connection.close()

  }
}


