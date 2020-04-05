package pojo

data class User(val id: Int, val name: String, val subscriptionId: Int?) {
    init {
        assert(id > 0) { "Non-positive user id: $id" }
    }
}