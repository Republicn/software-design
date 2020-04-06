package ru.republicn.softwaredesign.fitnessclub.reportService.query

import java.time.LocalDateTime

sealed class StatisticQuery

data class WholeStatistic(val userId: Int, val time: LocalDateTime) : StatisticQuery()