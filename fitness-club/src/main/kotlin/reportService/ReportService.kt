package reportService

import reportService.query.StatisticHandler
import reportService.query.StatisticQuery
import java.time.LocalDateTime
import java.time.LocalDateTime.now

class ReportService {

    private val statisticHandler = StatisticHandler()

    fun execute(query: StatisticQuery) {
        val userStatistic = statisticHandler.handle(query)
        val dailyStatistic = userStatistic?.getDailyStatistic()
        println(
            """User ${userStatistic?.userId} statistic:

            Daily
            Monday: + ${dailyStatistic!![0]}
            Tuesday: + ${dailyStatistic[1]}
            Wednesday: + ${dailyStatistic[2]}
            Thursday: + ${dailyStatistic[3]}
            Friday: + ${dailyStatistic[4]}
            Saturday: + ${dailyStatistic[5]}
            Sunday: + ${dailyStatistic[6]}

            User visits center every ${userStatistic.getAverageFrequency(now())} days in average
            User average visit is ${userStatistic.getAverageDuration()} minutes"""
        )
    }

}