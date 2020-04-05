package managerAdmin.query

sealed class ManagerQuery

data class SubscriptionInfo(val subscriptionId: Int) : ManagerQuery()