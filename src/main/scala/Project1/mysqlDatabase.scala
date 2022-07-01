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
    var rsSet = 0
    var pstmt = connection.prepareStatement(s"Insert into users (Firstname, Lastname, Username, Password, AdminPriv) Values(?, ?, ?, ?, ?)")
    try {
      pstmt.setString(1, FirstName)
      pstmt.setString(2, LastName)
      pstmt.setString(3, Username)
      pstmt.setString(4, Password)
      pstmt.setInt(5, AdminPriv)
      rsSet = pstmt.executeUpdate()
      println("The user account has been created")
      //showAllUsers()
      rsSet
    }
    catch {
      case e: SQLException => e.printStackTrace()
        rsSet
    }
  }

  def showAllUsers(): Unit = {
    connect()

  }

  def showUser(existingUser: String): Unit = {
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


