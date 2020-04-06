package turnstile

import ru.republicn.softwaredesign.fitnessclub.dao.FitnessClubDaoImpl
import turnstile.command.TurnstileEvent
import turnstile.command.TurnstileEventHandler

class TurnstileService(dao: FitnessClubDaoImpl) {

    private val turnstileEventHandler = TurnstileEventHandler(dao)

    fun handle(command: TurnstileEvent) = turnstileEventHandler.handle(command)

}