package com.setruth.jetpackcomposemviframework.network

import android.content.Context
import android.util.Log
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.setruth.jetpackcomposemviframework.constant.REQUEST_TIMEOUT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RequestBuilder(context: Context) {
    private var retrofitBuilder: Retrofit

    init {
        OkHttpClient.Builder()
            .cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context)))
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()
            .apply {
                retrofitBuilder = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(this)
                    .build()
            }
    }
    /**
     * TODO 获取APi接口的实体请求类
     *
     * @param T
     * @param apiType
     * @return
     */
    fun <T> getAPI(apiType: Class<T>): T = retrofitBuilder.create(apiType)

    /**
     * TODO 处理网络请求
     * @param requestFun Function0<Response<T>>
     * @return Flow<RequestStatus<T>>
     */
    suspend fun <T> getResponse(requestFun: () -> Response<T>): Flow<RequestStatus<T>> =
        flow {
            emit(RequestStatus.Loading)
            try {
                with(requestFun()) {
                    if (isSuccessful) {
                        if (body() != null) {
                            RequestStatus.Success(body()!!)
                        } else RequestStatus.Error(
                            Exception("失败"),
                            "服务器异常"
                        )
                    } else {
                        RequestStatus.Error(Exception("${code()}"), "网络不通")
                    }.let {
                        emit(it)
                    }
                }
            } catch (e: Exception) {
                Log.e("TAG", "getResponse:${e.message} ", )
                emit(RequestStatus.Error(e, "网络请求发送失败"))
            }
        }.catch { e ->
            emit(RequestStatus.Error(e as Exception, "网络请求无法开始"))
        }.flowOn(Dispatchers.IO)
    companion object{
        private const val PORT:Int=1234
        const val BASE_URL="http://10.0.2.2:$PORT"
    }
}
sealed class RequestStatus<out T> {
    data class Success<out T>(val data: T) : RequestStatus<T>()
    data class Error(val exception: Exception, val errMsg: String) : RequestStatus<Nothing>()
    object Loading : RequestStatus<Nothing>()
}