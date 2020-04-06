package ru.republicn.softwaredesign.fitnessclub.dao

import ru.republicn.softwaredesign.fitnessclub.pojo.Subscription
import ru.republicn.softwaredesign.fitnessclub.pojo.User
import java.time.LocalDateTime

interface FitnessClubDao {

    fun createUser(name: String): Int

    fun getUserById(id: Int): User

    fun createSubscription(userId: Int, startTime: LocalDateTime, endTime: LocalDateTime): Int

    fun extendSubscription(subscriptionId: Int, userId: Int, endTime: LocalDateTime): Int

    fun getSubscriptionById(subscriptionId: Int): Subscription

    fun enterOrExitEvent(subscriptionId: Int, userId: Int, enterTime: LocalDateTime, event: String)

}