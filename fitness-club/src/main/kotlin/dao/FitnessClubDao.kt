package dao

import pojo.Subscription
import java.time.LocalDateTime

interface FitnessClubDao {

    fun createUser(name: String)

    fun createSubscription(userId: Int, startTime: LocalDateTime, endTime: LocalDateTime)

    fun extendSubscription(subscriptionId: Int, userId: Int, endTime: LocalDateTime)

    fun getSubscriptionById(subscriptionId: Int): Subscription

    fun enterOrExitEvent(subscriptionId: Int, userId: Int, enterTime: LocalDateTime, event: String)

}