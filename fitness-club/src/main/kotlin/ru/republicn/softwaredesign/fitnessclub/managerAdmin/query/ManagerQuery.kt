package ru.republicn.softwaredesign.fitnessclub.managerAdmin.query

sealed class ManagerQuery

data class SubscriptionInfo(val subscriptionId: Int) : ManagerQuery()