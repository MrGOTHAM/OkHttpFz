package com.acg.okhttpfz

/**
 * @Classname IHttpCallback
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/9/15 17:13
 * @Created by an
 */
interface IHttpCallback {

    /**
     * 网络请求成功的回调
     * [data] 返回回调的数据结果
     */

    fun onSuccess(data: Any?)

    /**
     * 接口回调失败
     * [error] 错误信息的数据类
     */
    fun onFailed(error: Any?)

}