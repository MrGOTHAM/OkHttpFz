package com.acg.okhttpfz

import androidx.collection.SimpleArrayMap
import com.google.gson.Gson
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.security.KeyStore
import java.util.concurrent.TimeUnit

/**
 * @Classname OkhttpApi
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/9/15 17:25
 * @Created by an
 */

/**
 * okHttp基本封装
 * 1. 构建client
 * 2. 配置参数
 * 3. 构建request
 * 4. 执行请求call.execute/enqueue
 */

class OkhttpApi:HttpApi {

    companion object{
        private const val TAG = "OkhttpApi" //Tag
    }

    private var baseUrl = "https://api.qingyunke.com/"
    // 2 最大重试次数
    var maxRetry = 0

    // 2 存储请求，用于取消
    private val callMap = SimpleArrayMap<Any,Call>()

    // okHttpClient
    private var mClient = OkHttpClient.Builder()
        .callTimeout(10,TimeUnit.SECONDS)       //完整请求超时时长，从发起到接收返回数据，默认0，不限定，
        .connectTimeout(10,TimeUnit.SECONDS)    //与服务器建立链接的时长，默认10s
        .readTimeout(10,TimeUnit.SECONDS)       //读取服务器返回数据的时长
        .writeTimeout(10,TimeUnit.SECONDS)      //向服务器写入数据的时长
        .retryOnConnectionFailure(true) //重连
        .followRedirects(false)            //重定向
        .cache(Cache(File("sdcard/cache","okhttp"),1024))
        .cookieJar(LocalCookieJar())    // 2 自定义的本地化cookie缓存
        .addNetworkInterceptor(KtHttpLogInterceptor{
            logLevel(KtHttpLogInterceptor.LogLevel.BODY)
        })// 2 添加网络拦截器，可以对okhttp的网络请求做拦截处理，不同于应用拦截器，这里能感知所有网络状态，比如重定向
        .addNetworkInterceptor(RetryInterceptor(maxRetry)) // 2 请求重试拦截
        .build()

    override fun get(params: Map<String, Any>, path: String, callback: IHttpCallback) {
        val url = "$baseUrl$path"
        // 拼接请求参数
        val urlBuilder = url.toHttpUrl().newBuilder()
        params.forEach{
            urlBuilder.addEncodedQueryParameter(it.key, it.value.toString())
        }

        val request = Request.Builder()
            .get()
            .tag(params)        // 2 给请求打上tag，用于取消
            .url(urlBuilder.build())
            .cacheControl(CacheControl.FORCE_NETWORK)   // 2
            .build()
        val newCall = mClient.newCall(request)
        // 存储请求，用于取消
        callMap.put(request.tag(), newCall)

        newCall.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailed(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess(response.body?.string())
            }
        })
    }

    /**
     * 访问cn5登录接口时替换url
     */
    override fun post(body: Any, path: String, callback: IHttpCallback) {
        val url = "$baseUrl$path"
        val request = Request.Builder()
            .post(Gson().toJson(body).toRequestBody())
            .url(url)
            .build()

        mClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailed(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess(response.body?.string())
            }
        })
    }

    // 2
    override fun cancelRequest(tag: Any) {
        TODO("Not yet implemented")
    }

    // 2
    override fun cancelAllRequests() {
        TODO("Not yet implemented")
    }
}