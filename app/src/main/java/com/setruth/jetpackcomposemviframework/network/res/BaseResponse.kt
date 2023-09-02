package com.setruth.jetpackcomposemviframework.network.res

data class BaseResponse<T>(val status: Int, val msg: String, val data: T?)
object ResponseStatus {
    const val SUCCESS = 200
    const val ERROR = 500
}