package ru.republicn.softwaredesign.fitnessclub.managerAdmin.command

import ru.republicn.softwaredesign.fitnessclub.dao.FitnessClubDaoImpl
import ru.republicn.softwaredesign.fitnessclub.reportService.storage.ReportStorage

class ManagerCommandHandler(private val dao: FitnessClubDaoImpl) {

    fun handle(command: ManagerCommand): Int =
        when (command) {
            is CreateUser -> dao.createUser(command.name)
            is CreateSubscription -> {
                ReportStorage.createUserStat(command.userId, command.startTime, command.endTime)
                dao.createSubscription(command.userId, command.startTime, command.endTime)
            }
            is ExtendSubscription -> {
                ReportStorage.extendSubscription(command.userId, command.endTime)
                dao.extendSubscription(command.subscriptionId, command.userId, command.endTime)
            }
        }

}