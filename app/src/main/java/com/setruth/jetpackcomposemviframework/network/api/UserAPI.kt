package com.setruth.jetpackcomposemviframework.network.api

import com.setruth.jetpackcomposemviframework.model.body.LoginBody
import com.setruth.jetpackcomposemviframework.network.res.BaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UserAPI {
    @POST("user/login")
    fun login(
        @Body loginBody: LoginBody
    ): Call<BaseResponse<Boolean>>
}