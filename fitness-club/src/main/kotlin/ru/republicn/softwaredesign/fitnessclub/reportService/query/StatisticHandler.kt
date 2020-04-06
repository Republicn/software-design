package ru.republicn.softwaredesign.fitnessclub.reportService.query

import ru.republicn.softwaredesign.fitnessclub.reportService.storage.ReportStorage

class StatisticHandler {

    fun handle(query: StatisticQuery) =
        when (query) {
            is WholeStatistic -> ReportStorage.getUserStatisticById(query.userId)
        }

}