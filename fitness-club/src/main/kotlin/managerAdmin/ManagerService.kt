package managerAdmin

import dao.FitnessClubDaoImpl
import managerAdmin.command.ManagerCommand
import managerAdmin.command.ManagerCommandHandler
import managerAdmin.query.ManagerQuery
import managerAdmin.query.ManagerQueryHandler

class ManagerService(dao: FitnessClubDaoImpl) {

    private val managerCommandHandler = ManagerCommandHandler(dao)
    private val managerQueryHandler = ManagerQueryHandler(dao)

    fun execute(command: ManagerCommand) = managerCommandHandler.handle(command)
    fun execute(query: ManagerQuery) {
        val res = managerQueryHandler.handle(query)
        println(
            """${res.id} + subscription info:
                User id: ${res.userId}
                Subscription first day: ${res.startTime}
                Subscription last day: ${res.endTime}
            """
        )


    }

}