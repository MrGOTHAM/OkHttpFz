package com.acg.okhttpfz

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 重试请求的网络拦截器
 */

class RetryInterceptor(private val maxRetry: Int = 0) : Interceptor {

    // 已重试的次数，注意，设置maxRetry重试次数，作用于重试，
    // 所以总的请求次数可能就是原始的1，+maxRetry
    private var retriedNum = 0

    /**
     * 网络请求拦截，使用的是责任链模式
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.e("RetryInterceptor", "intercept 22 行：当前retriedNum=$retriedNum")
        var response = chain.proceed(request)
        while (!response.isSuccessful && retriedNum < maxRetry) {
            retriedNum++
            Log.e("RetryInterceptor", "intercept 26 行：当前retriedNum=$retriedNum")
            response = chain.proceed(request)
        }
        return response
    }
}
