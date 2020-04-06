package ru.republicn.softwaredesign.fitnessclub.managerAdmin

import ru.republicn.softwaredesign.fitnessclub.dao.FitnessClubDaoImpl
import ru.republicn.softwaredesign.fitnessclub.managerAdmin.command.ManagerCommand
import ru.republicn.softwaredesign.fitnessclub.managerAdmin.command.ManagerCommandHandler
import ru.republicn.softwaredesign.fitnessclub.managerAdmin.query.ManagerQuery
import ru.republicn.softwaredesign.fitnessclub.managerAdmin.query.ManagerQueryHandler

class ManagerService(dao: FitnessClubDaoImpl) {

    private val managerCommandHandler = ManagerCommandHandler(dao)
    private val managerQueryHandler = ManagerQueryHandler(dao)

    fun execute(command: ManagerCommand) = managerCommandHandler.handle(command)
    fun execute(query: ManagerQuery): String {
        val res = managerQueryHandler.handle(query)
        return(
            """${res.id} subscription info:
                User id: ${res.userId}
                Subscription first day: ${res.startTime}
                Subscription last day: ${res.endTime}
            """
        )


    }

}