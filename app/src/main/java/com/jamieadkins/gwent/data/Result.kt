package com.jamieadkins.gwent.data

class Result<T>(var status: Status = Status.OK, var content: T? = null) {
    enum class Status {
        INTELLIGENT_SEARCH_FAILED,
        OK
    }
}