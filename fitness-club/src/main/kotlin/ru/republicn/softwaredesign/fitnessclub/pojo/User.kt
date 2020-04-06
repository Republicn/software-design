package ru.republicn.softwaredesign.fitnessclub.pojo

data class User(val id: Int, val name: String) {
    init {
        assert(id >= 0) { "Negative user id: $id" }
    }
}