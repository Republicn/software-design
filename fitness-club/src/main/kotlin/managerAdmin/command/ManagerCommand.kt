package managerAdmin.command

import java.time.LocalDateTime

sealed class ManagerCommand

data class CreateUser(val name: String) : ManagerCommand()
data class CreateSubscription(val userId: Int, val startTime: LocalDateTime, val endTime: LocalDateTime) :
    ManagerCommand()

data class ExtendSubscription(val subscriptionId: Int, val userId: Int, val endTime: LocalDateTime) : ManagerCommand()
