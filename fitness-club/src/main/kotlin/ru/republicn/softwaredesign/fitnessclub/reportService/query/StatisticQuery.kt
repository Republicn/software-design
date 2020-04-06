package ru.republicn.softwaredesign.fitnessclub.reportService.query

sealed class StatisticQuery

data class WholeStatistic(val userId: Int) : StatisticQuery()