package managerAdmin.command

import dao.FitnessClubDaoImpl
import reportService.storage.ReportStorage

class ManagerCommandHandler(private val dao: FitnessClubDaoImpl) {

    fun handle(command: ManagerCommand) =
        when (command) {
            is CreateUser -> dao.createUser(command.name)
            is CreateSubscription -> {
                dao.createSubscription(command.userId, command.startTime, command.endTime)
                ReportStorage.createUserStat(command.userId, command.startTime, command.endTime)
            }
            is ExtendSubscription -> {
                dao.extendSubscription(command.subscriptionId, command.userId, command.endTime)
                ReportStorage.extendSubscription(command.userId, command.endTime)
            }
        }

}