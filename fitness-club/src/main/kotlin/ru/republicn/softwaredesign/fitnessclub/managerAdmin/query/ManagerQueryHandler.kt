package ru.republicn.softwaredesign.fitnessclub.managerAdmin.query

import ru.republicn.softwaredesign.fitnessclub.dao.FitnessClubDaoImpl

class ManagerQueryHandler(private val dao: FitnessClubDaoImpl) {

    fun handle(query: ManagerQuery) =
        when (query) {
            is SubscriptionInfo -> dao.getSubscriptionById(query.subscriptionId)
        }

}