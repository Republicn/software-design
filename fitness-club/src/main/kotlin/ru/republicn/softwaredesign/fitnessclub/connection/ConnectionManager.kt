package ru.republicn.softwaredesign.fitnessclub.connection

import java.sql.DriverManager

class ConnectionManager {

    fun connect() = DriverManager
        .getConnection("jdbc:postgresql://localhost:5432/postgres")

}