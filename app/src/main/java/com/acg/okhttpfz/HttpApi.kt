package com.acg.okhttpfz

import okhttp3.Callback

/**
 * @Classname HttpApi
 * @Description 网络请求的统一接口类
 * @Version 1.0.0
 * @Date 2022/9/15 17:09
 * @Created by an
 */
interface HttpApi {
    /**
     * 抽象http的get请求封装，异步
     * 由于本身的 Callback只能是okhttp3.Callback , 这样太固定，因此将它抽象出来
     * fun get(param:Map<String,Any>,path:String, callback: Callback){}
     */
    fun get(params: Map<String, Any>, path: String, callback: IHttpCallback) {}

    /**
     * 抽象的http的get的请求，同步
     */
    fun getSync(params: Map<String, Any>, path: String): Any? {
        return Any()
    }

    /**
     * 抽象的http的post的请求，异步
     */
    fun post(body: Any, path: String, callback: IHttpCallback) {}

    /**
     * 抽象的http的post的请求，同步
     */
    fun postSync(body: Any, path: String): Any? = Any()

    /**
     * 2 取消指定tag的请求
     */
    fun cancelRequest(tag:Any)

    /**
     * 2 取消所有请求
     */
    fun cancelAllRequests()

}