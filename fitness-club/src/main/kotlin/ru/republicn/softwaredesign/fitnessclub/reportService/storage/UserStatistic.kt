package ru.republicn.softwaredesign.fitnessclub.reportService.storage

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class UserStatistic(val userId: Int, val startDate: LocalDateTime, var endDate: LocalDateTime) {

    private val dailyStatistic: ArrayList<Int> = ArrayList()
    private var summaryDuration: Long = 0
    private var countEntry = 0
    private var countExit = 0
    var lastEntryTime: LocalDateTime? = null

    init {
        (0..7).forEach { _ ->
            dailyStatistic.add(0)
        }
    }

    fun getDailyStatistic() = dailyStatistic

    fun getAverageFrequency(curTime: LocalDateTime): Double {
        val time = if (curTime.isBefore(endDate)) curTime else endDate
        return 1.0 * (ChronoUnit.DAYS.between(startDate, time)) / countEntry
    }

    fun getAverageDuration(): Double = 1.0 * summaryDuration / countExit

    fun addDuration(duration: Long) {
        summaryDuration += duration
    }

    fun addEntry(day: Int) {
        countEntry++
        dailyStatistic[day]++
    }

    fun exit() {
        countExit++
    }


}