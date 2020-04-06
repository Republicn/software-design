package ru.republicn.softwaredesign.fitnessclub.dao

import ru.republicn.softwaredesign.fitnessclub.connection.ConnectionManager
import ru.republicn.softwaredesign.fitnessclub.pojo.Subscription
import ru.republicn.softwaredesign.fitnessclub.pojo.User
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.LocalDateTime.now

class FitnessClubDaoImpl(private val connectionManager: ConnectionManager) : FitnessClubDao {

    init {
        connectionManager.connect().use { connection ->
            connection.createStatement().use { statement ->
                val createTurnstileEventSql = "CREATE TYPE TurnstileEvent AS ENUM ('ENTER', 'EXIT');"
                val createSubscriptionEventSql = "CREATE TYPE SubscriptionEvent AS ENUM ('CREATE', 'EXTEND');"

                val createUsersTableSql =
                    "DROP TABLE Users CASCADE;\n" +
                            "DROP TABLE SubscriptionEvents CASCADE;\n" +
                            "DROP TABLE TurnstileEvents CASCADE;\n" +
                            "DROP TYPE TurnstileEvent CASCADE;\n" +
                            "DROP TYPE SubscriptionEvent CASCADE;" +
                            "CREATE TABLE IF NOT EXISTS Users(\n" +
                            "user_id SERIAL PRIMARY KEY,\n" +
                            "user_name VARCHAR(50) NOT NULL);"
                val createSubscriptionEventsTableSql =
                    "CREATE TABLE IF NOT EXISTS SubscriptionEvents(\n" +
                            "event_id SERIAL PRIMARY KEY,\n" +
                            "subscription_id INT NOT NULL,\n" +
                            "subscription_timestamp TIMESTAMP NOT NULL,\n" +
                            "subscription_event SubscriptionEvent NOT NULL,\n" +
                            "user_id SERIAL NOT NULL,\n" +
                            "FOREIGN KEY (user_id) REFERENCES Users (user_id));"
                val createTurnstileEventsTable =
                    "CREATE TABLE IF NOT EXISTS TurnstileEvents(\n" +
                            "event_id SERIAL PRIMARY KEY,\n" +
                            "turnstile_time TIMESTAMP NOT NULL,\n" +
                            "turnstile_event TurnstileEvent NOT NULL,\n" +
                            "user_id SERIAL NOT NULL,\n" +
                            "FOREIGN KEY (user_id) REFERENCES Users (user_id));"

                statement.execute(createUsersTableSql)
                statement.execute(createTurnstileEventSql)
                statement.execute(createSubscriptionEventSql)
                statement.execute(createSubscriptionEventsTableSql)
                statement.execute(createTurnstileEventsTable)
            }
        }
    }

    override fun createUser(name: String): Int {
        connectionManager.connect().use { connection ->
            connection.createStatement().use { statement ->
                val sql = "INSERT INTO Users (user_name)" +
                        "VALUES ('$name')\n" +
                        "RETURNING user_id;"

                statement.executeQuery(sql).use {
                    if (it.next()) return it.getInt("user_id")
                }

                return -1
            }
        }
    }

    override fun getUserById(id: Int): User {
        connectionManager.connect().use { connection ->
            connection.createStatement().use { statement ->
                val sql = "SELECT user_id, user_name FROM Users\n" +
                        "WHERE user_id = $id;"

                var userId: Int
                var userName: String
                statement.executeQuery(sql).use {

                    if (it.next()) {
                        userId = it.getInt("user_id")
                        userName = it.getString("user_name")
                        return User(userId, userName)
                    } else throw Exception("No user with id $id")
                }

            }
        }
    }

    override fun createSubscription(userId: Int, startTime: LocalDateTime, endTime: LocalDateTime): Int {
        connectionManager.connect().use { connection ->
            connection.createStatement().use { statement ->
                val sql = "SELECT subscription_id\n" +
                        "FROM SubscriptionEvents\n" +
                        "ORDER BY subscription_id DESC\n" +
                        "LIMIT 1;"

                statement.executeQuery(sql).use {
                    val newIndex = if (it.next()) it.getInt("subscription_id") + 1 else 1
                    val startTimestamp = Timestamp.valueOf(startTime)
                    val createSql = "INSERT INTO SubscriptionEvents\n" +
                            "(subscription_id, subscription_timestamp, subscription_event, user_id)\n" +
                            "VALUES (?, ?, 'CREATE'::SubscriptionEvent, ?);"

                    val endTimestamp = Timestamp.valueOf(endTime)
                    val setEndTimeSql = "INSERT INTO SubscriptionEvents\n" +
                            "(subscription_id, subscription_timestamp, subscription_event, user_id)\n" +
                            "VALUES (?, ?, 'EXTEND'::SubscriptionEvent, ?);"

                    val prepared = connection.prepareStatement(createSql)
                    prepared.setInt(1, newIndex)
                    prepared.setTimestamp(2, startTimestamp)
                    prepared.setInt(3, userId)
                    prepared.execute()

                    val prepared2 = connection.prepareStatement(setEndTimeSql)
                    prepared2.setInt(1, newIndex)
                    prepared2.setTimestamp(2, endTimestamp)
                    prepared2.setInt(3, userId)
                    prepared2.execute()

                    return newIndex
                }
            }
        }
    }

    override fun extendSubscription(subscriptionId: Int, userId: Int, endTime: LocalDateTime): Int {
        connectionManager.connect().use { connection ->
            val endTimestamp = Timestamp.valueOf(endTime)
            val sql = "INSERT INTO SubscriptionEvents\n" +
                    "(subscription_id, subscription_timestamp, subscription_event, user_id)\n" +
                    "VALUES ($subscriptionId, ?, 'EXTEND'::SubscriptionEvent, $userId);"
            val stmt = connection.prepareStatement(sql)
            stmt.setTimestamp(1, endTimestamp)
            stmt.execute()

            return subscriptionId
        }
    }

    override fun getSubscriptionById(subscriptionId: Int): Subscription {
        connectionManager.connect().use { connection ->
            connection.createStatement().use { statement ->
                val sql = "SELECT subscription_timestamp, user_id\n" +
                        "FROM SubscriptionEvents\n" +
                        "WHERE subscription_id = $subscriptionId;"
                statement.executeQuery(sql).use {
                    var first = true
                    var startTime: LocalDateTime = now()
                    var endTime: LocalDateTime = now()
                    var userId = 1
                    while (it.next()) {
                        if (first) {
                            startTime = it.getTimestamp("subscription_timestamp").toLocalDateTime()
                            first = false
                        }
                        if (it.isLast) {
                            endTime = it.getTimestamp("subscription_timestamp").toLocalDateTime()
                            userId = it.getInt("user_id")
                        }
                    }

                    return Subscription(subscriptionId, userId, startTime, endTime)
                }
            }
        }
    }

    override fun enterOrExitEvent(subscriptionId: Int, userId: Int, enterTime: LocalDateTime, event: String) {
        connectionManager.connect().use { connection ->
            connection.createStatement().use { statement ->
                val timestamp = Timestamp.valueOf(enterTime)
                val sql = "INSERT INTO TurnstileEvents\n" +
                        "(turnstile_time, turnstile_event, user_id)\n" +
                        "VALUES (?, '$event'::TurnstileEvent, $userId);"
                val prepared = connection.prepareStatement(sql)
                prepared.setTimestamp(1, timestamp)
                if (event == "enter") {
                    val checkSql = "SELECT turnstile_time\n" +
                            "FROM TurnstileEvents\n" +
                            "ORDER BY turnstile_time DESC\n" +
                            "LIMIT 1;"
                    statement.executeQuery(checkSql).use {
                        if (it.next()) {
                            val endTime = it.getTimestamp("turnstile_time").toLocalDateTime()
                            if (endTime.isAfter(enterTime)) {
                                prepared.execute()
                            } else {
                                throw Exception("Subscription is not activated")
                            }
                        } else {
                            throw Exception("No subscription found")
                        }
                    }
                } else {
                    prepared.execute()
                }
            }
        }
    }

}