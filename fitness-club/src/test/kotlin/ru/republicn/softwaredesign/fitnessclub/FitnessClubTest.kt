package ru.republicn.softwaredesign.fitnessclub

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.republicn.softwaredesign.fitnessclub.connection.ConnectionManager
import ru.republicn.softwaredesign.fitnessclub.dao.FitnessClubDaoImpl
import ru.republicn.softwaredesign.fitnessclub.managerAdmin.ManagerService
import ru.republicn.softwaredesign.fitnessclub.managerAdmin.command.CreateSubscription
import ru.republicn.softwaredesign.fitnessclub.managerAdmin.query.SubscriptionInfo
import ru.republicn.softwaredesign.fitnessclub.reportService.ReportService
import ru.republicn.softwaredesign.fitnessclub.reportService.query.WholeStatistic
import turnstile.TurnstileService
import turnstile.command.Enter
import turnstile.command.Exit
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
        assert(userId >= 1)

        println(userId)
        val checkUser = dao.getUserById(userId)
        assertEquals(checkUser.id, userId)
        assertEquals(checkUser.name, name)
    }

    @Test
    fun createSubscriptionTest() {
        val startTime = "2020-01-03T10:15:30"
        val endTime = "2021-01-03T10:15:30"

        val subscriptionId =
            managerService.execute(
                CreateSubscription(
                    testUserId,
                    parse(startTime),
                    parse(endTime)
                )
            )

        val subscriptionInfo = managerService.execute(SubscriptionInfo(subscriptionId))
        assertEquals(
            subscriptionInfo,
            """$subscriptionId subscription info:
                User id: $testUserId
                Subscription first day: $startTime
                Subscription last day: $endTime
            """
        )
    }

    @Test
    fun checkStatisticsTest() {
        val startTime = "2020-01-03T10:15:30"
        val endTime = "2021-01-03T10:15:30"

        val name = "Pavlov Pavel"
        val userId = dao.createUser(name)
        print(userId)
        dao.getUserById(2)

        val subscriptionId =
            managerService.execute(
                CreateSubscription(
                    2,
                    parse(startTime),
                    parse(endTime)
                )
            )

        turnstileService.handle(Enter(subscriptionId, userId, parse("2020-01-04T10:15:30")))
        turnstileService.handle(Exit(subscriptionId, userId, parse("2020-01-04T12:11:30")))
        turnstileService.handle(Enter(subscriptionId, userId, parse("2020-01-06T10:15:30")))

        val userStat = reportService.execute(WholeStatistic(userId, parse("2020-01-07T10:15:30")))
        assertEquals(
            userStat, """User $userId statistic:

            Daily
            Monday: 1
            Tuesday: 0
            Wednesday: 0
            Thursday: 0
            Friday: 0
            Saturday: 1
            Sunday: 0

            User visits center every 2.0 days in average
            User average visit is 116.0 minutes"""
        )
    }


}