package dao

import connection.ConnectionManager
import pojo.Subscription
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
//                    "DROP TABLE Users CASCADE;\n" +
//                        "DROP TABLE Subscriptions CASCADE;\n" +
                    "CREATE TABLE IF NOT EXISTS Users(\n" +
                            "user_id SERIAL PRIMARY KEY,\n" +
                            "user_name VARCHAR(50) NOT NULL);"
                val createSubscriptionEventsTableSql =
                    "CREATE TABLE IF NOT EXISTS SubscriptionEvents(\n" +
                            "event_id SERIAL PRIMARY KEY,\n" +
                            "subscription_id INT NOT NULL,\n" +
                            "subscription_timestamp TIMESTAMP NOT NULL,\n" +
                            "subscription_event SubscriptionEvent NOT NULL,\n" +
                            "FOREIGN KEY (user_id) REFERENCES Users (user_id));"
                val createTurnstileEventsTable =
                    "CREATE TABLE IF NOT EXISTS TurnstileEvents(\n" +
                            "event_id SERIAL PRIMARY KEY,\n" +
                            "turnstile_time TIMESTAMP NOT NULL,\n" +
                            "turnstile_event TurnstileEvent NOT NULL,\n" +
                            "FOREIGN KEY (user_id) REFERENCES Users (user_id));"

                statement.execute(createTurnstileEventSql)
                statement.execute(createSubscriptionEventSql)
                statement.execute(createUsersTableSql)
                statement.execute(createSubscriptionEventsTableSql)
                statement.execute(createTurnstileEventsTable)
            }
        }
    }

    override fun createUser(name: String) {
        connectionManager.connect().use { connection ->
            connection.createStatement().use { statement ->
                val sql = "INSERT INTO Users (user_name)" +
                        "VALUES ('$name');"

                statement.execute(sql)
            }
        }
    }

    override fun createSubscription(userId: Int, startTime: LocalDateTime, endTime: LocalDateTime) {
        connectionManager.connect().use { connection ->
            connection.createStatement().use { statement ->
                val sql = "SELECT subscription_id\n" +
                        "FROM SubscriptionEvents\n" +
                        "ORDER BY subscription_id DESC\n" +
                        "LIMIT 1;"

                statement.executeQuery(sql).use {
                    val newIndex = if (it.next()) it.getInt("subscription_id") + 1 else 0
                    val startTimestamp = Timestamp.valueOf(startTime)
                    val createSql = "INSERT INTO SubscriptionEvents\n" +
                            "(subscription_id, subscription_timestamp, subscription_event, user_id)\n" +
                            "VALUES ($newIndex, $startTimestamp, 'CREATE'::SubscriptionEvent, $userId);"

                    val endTimestamp = Timestamp.valueOf(endTime)
                    val setEndTimeSql = "INSERT INTO SubscriptionEvents\n" +
                            "(subscription_id, subscription_timestamp, subscription_event, user_id)\n" +
                            "VALUES ($newIndex, $endTimestamp, 'EXTEND'::SubscriptionEvent, $userId);"

                    statement.execute(createSql)
                    statement.execute(setEndTimeSql)
                }
            }
        }
    }

    override fun extendSubscription(subscriptionId: Int, userId: Int, endTime: LocalDateTime) {
        connectionManager.connect().use { connection ->
            connection.createStatement().use { statement ->
                val endTimestamp = Timestamp.valueOf(endTime)
                val sql = "INSERT INTO SubscriptionEvents\n" +
                        "(subscription_id, subscription_timestamp, subscription_event, user_id)\n" +
                        "VALUES ($subscriptionId, $endTimestamp, 'EXTEND'::SubscriptionEvent, $userId);"

                statement.execute(sql)
            }
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
                    var userId = 0
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
                        "VALUES ($timestamp, '$event'::TurnstileEvent, $userId);"
                if (event == "enter") {
                    val checkSql = "SELECT turnstile_time\n" +
                            "FROM TurnstileEvents\n" +
                            "ORDER BY turnstile_time DESC\n" +
                            "LIMIT 1;"
                    statement.executeQuery(checkSql).use {
                        if (it.next()) {
                            val endTime = it.getTimestamp("turnstile_time").toLocalDateTime()
                            if (endTime.isAfter(enterTime)) {
                                statement.execute(sql)
                            } else {
                                throw Exception("Subscription is not activated")
                            }
                        } else {
                            throw Exception("No subscription found")
                        }
                    }
                } else {
                    statement.execute(sql)
                }
            }
        }
    }


}