package ru.republicn.softwaredesign.fitnessclub.pojo

import java.time.LocalDateTime

data class Subscription(val id: Int, val userId: Int, var startTime: LocalDateTime, val endTime: LocalDateTime) {
    init {
        assert(id >= 0) { "Negative subscription id: $id" }
        assert(userId >= 0) { "Negative user id: $userId" }
    }
}