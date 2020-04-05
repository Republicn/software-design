package reportService.query

sealed class StatisticQuery

data class WholeStatistic(val userId: Int) : StatisticQuery()