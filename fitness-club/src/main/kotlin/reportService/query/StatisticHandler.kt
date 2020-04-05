package reportService.query

import reportService.storage.ReportStorage

class StatisticHandler {

    fun handle(query: StatisticQuery) =
        when (query) {
            is WholeStatistic -> ReportStorage.getUserStatisticById(query.userId)
        }

}