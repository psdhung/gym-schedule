package dave.gymschedule.model

class Resource<T>(val status: Status, val data: T, val error: Throwable? = null) {
    enum class Status {
        LOADING,
        SUCCESS,
        ERROR
    }
}
