package com.setruth.jetpackcomposemviframework.network.res

data class BaseResponse<T>(var status: Int, var msg: String, var data: T?)
object ResponseStatus {
    const val SUCCESS = 200
    const val ERROR = 500
}