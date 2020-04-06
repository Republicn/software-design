package ru.republicn.softwaredesign.fitnessclub.reportService.storage

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object ReportStorage {
    private val wholeStatistic: HashMap<Int, UserStatistic> = HashMap()

    fun getUserStatisticById(id: Int) = wholeStatistic[id]

    fun createUserStat(id: Int, startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
        wholeStatistic.putIfAbsent(id, UserStatistic(id, startDateTime, endDateTime))
    }

    fun enter(id: Int, enterTime: LocalDateTime) {
        val userStatistic = wholeStatistic[id]
        userStatistic?.lastEntryTime = enterTime
        userStatistic?.addEntry(enterTime.dayOfWeek.value - 1)
    }

    fun exit(id: Int, exitTime: LocalDateTime) {
        val userStatistic = wholeStatistic[id]
        if (userStatistic?.lastEntryTime != null) {
            userStatistic.exit()
            userStatistic.addDuration(ChronoUnit.MINUTES.between(userStatistic.lastEntryTime, exitTime))
        }
    }

    fun extendSubscription(id: Int, newEndTime: LocalDateTime) {
        getUserStatisticById(id)?.endDate = newEndTime
    }
}