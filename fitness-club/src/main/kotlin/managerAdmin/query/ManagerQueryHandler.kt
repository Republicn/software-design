package managerAdmin.query

import dao.FitnessClubDaoImpl

class ManagerQueryHandler(private val dao: FitnessClubDaoImpl) {

    fun handle(query: ManagerQuery) =
        when (query) {
            is SubscriptionInfo -> dao.getSubscriptionById(query.subscriptionId)
        }

}