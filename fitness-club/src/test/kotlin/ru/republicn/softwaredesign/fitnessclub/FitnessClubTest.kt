package ru.republicn.softwaredesign.fitnessclub

import ru.republicn.softwaredesign.fitnessclub.connection.ConnectionManager
import ru.republicn.softwaredesign.fitnessclub.dao.FitnessClubDaoImpl
import ru.republicn.softwaredesign.fitnessclub.managerAdmin.ManagerService
import ru.republicn.softwaredesign.fitnessclub.managerAdmin.command.CreateSubscription
import ru.republicn.softwaredesign.fitnessclub.managerAdmin.query.SubscriptionInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.republicn.softwaredesign.fitnessclub.reportService.ReportService
import ru.republicn.softwaredesign.fitnessclub.reportService.query.WholeStatistic
import turnstile.TurnstileService
import turnstile.command.Enter
import turnstile.command.Exit
import java.time.LocalDateTime
import java.time.LocalDateTime.parse

class FitnessClubTest {

    private val connectionManager = ConnectionManager()
    private val dao = FitnessClubDaoImpl(connectionManager)

    private val turnstileService = TurnstileService(dao)
    private val managerService = ManagerService(dao)
    private val reportService = ReportService()

    private val testUserName = "Ivanov Ivan"
    private val testUserId = dao.createUser(testUserName)

    @Test
    fun createUserTest() {
        val name = "Pavlov Pavel"
        val userId = dao.createUser(name)
        assert(userId >= 0)

        val checkUser = dao.getUserById(userId)
        assertEquals(checkUser.id, userId)
        assertEquals(checkUser.name, name)
    }

    @Test
    fun createSubscriptionTest() {
        val startTime = "2020-12-03T10:15:30"
        val endTime = "2021-12-03T10:15:30"

        val subscriptionId =
            managerService.execute(
                CreateSubscription(
                    testUserId,
                    parse(startTime),
                    parse(endTime)
                )
            )

        val subscriptionInfo = managerService.execute(SubscriptionInfo(subscriptionId))
        assertEquals(subscriptionInfo,
            """$subscriptionId subscription info:
                User id: $testUserId
                Subscription first day: $startTime
                Subscription last day: $endTime
            """)
    }

    @Test
    fun checkStatisticsTest() {
        val startTime = "2020-12-03T10:15:30"
        val endTime = "2021-12-03T10:15:30"

        val subscriptionId =
            managerService.execute(
                CreateSubscription(
                    testUserId,
                    parse(startTime),
                    parse(endTime)
                )
            )

        turnstileService.handle(Enter(subscriptionId, testUserId, parse("2020-12-04T10:15:30")))
        turnstileService.handle(Exit(subscriptionId, testUserId, parse("2020-12-04T12:11:30")))
        turnstileService.handle(Enter(subscriptionId, testUserId, parse("2020-12-06T10:15:30")))

        val userStat = reportService.execute(WholeStatistic(testUserId))
        assertEquals(userStat, "")
    }


}