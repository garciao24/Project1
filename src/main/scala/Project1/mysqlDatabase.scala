package Project1

import java.sql.{Connection, DriverManager, ResultSet, SQLException, Statement}

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
      println("it works")

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

  def showAllUsers(): Unit = {
    connect()
    val statement = connection.prepareStatement("SELECT DISTINCT USERNAME FROM users")
    try {
      val resultSet = statement.executeQuery()
      println("Users")
      while (resultSet.next()) {
        println(resultSet.getString("USERNAME"))
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def showUser(Username: String): Unit = {
    connect()


  }


  def updateUsername(newUsersUsername: String, usersUserName: String): Unit = {
    connect()


  }



  def updateFirstName(newFirstName: String, existingUser: String): Unit = {
    connect()

  }

  def updateLastName(newLastName: String, existingUser: String): Unit = {
    connect()

  }

  def elevateUser2Admin(selectedUser: String): Unit = {
    connect()
  }

  def deleteUser(selectedUser: String): Unit = {
    connect()


  }

//  def validateLogin(usersUserName: String, usersPassword: String): Boolean = {
//
//  }

//  def checkIsAdmin(usersUserName: String): Int = {
//
//  }



  def disconnectFromDB(): Unit = {

    connection.close()

  }
}


