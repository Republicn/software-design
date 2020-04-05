package turnstile.command

import dao.FitnessClubDaoImpl
import reportService.storage.ReportStorage

class TurnstileEventHandler(private val dao: FitnessClubDaoImpl) {

    fun handle(event: TurnstileEvent) =
        when (event) {
            is Enter -> {
                dao.enterOrExitEvent(event.subscriptionId, event.userId, event.timestamp, "ENTER")
                ReportStorage.enter(event.userId, event.timestamp)
            }
            is Exit -> {
                dao.enterOrExitEvent(event.subscriptionId, event.userId, event.timestamp, "EXIT")
                ReportStorage.exit(event.userId, event.timestamp)
            }
        }
}