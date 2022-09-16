package com.acg.okhttpfz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var mResultTv:TextView
    private lateinit var mSendMsg:Button

     companion object{
       private const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mResultTv= findViewById(R.id.tv_result)
        mSendMsg = findViewById(R.id.btn_send)

        mSendMsg.setOnClickListener {
            sendGet()
        }

    }

//    private fun sendPostCn5Login(){
//        val httpApi:HttpApi=OkhttpApi()
//        httpApi.post(LoginReq(),"",object :IHttpCallback{
//            override fun onSuccess(data: Any?) {
//                Log.e(TAG,"success result : ${data.toString()}")
//                runOnUiThread{
//                    mResultTv.text = data.toString()
//                }
//            }
//
//            override fun onFailed(error: Any?) {
//                Log.e(TAG,"fail msg : ${error.toString()}")
//            }
//        })
//    }

    private fun sendPost(){
        val map = mapOf(
            "key" to "free",
            "appid" to "0",
            "msg" to "你好"
        )
        val jsonObject = JSONObject(map)
        val okhttpApi = OkhttpApi()
        okhttpApi.post(jsonObject.toString(),"api.php",object :IHttpCallback{
            override fun onSuccess(data: Any?) {
                Log.e(TAG,"success result : ${data.toString()}")
                runOnUiThread{
                    mResultTv.text = data.toString()
                }
            }

            override fun onFailed(error: Any?) {
                Log.e(TAG,"fail msg : ${error.toString()}")
            }
        })


    }

    private fun sendGet(){
        val map = mapOf(
            "key" to "free",
            "appid" to "0",
            "msg" to "你好"
        )

        val httpApi:HttpApi = OkhttpApi()
        httpApi.get(map, "api.php", object :IHttpCallback{
            override fun onSuccess(data: Any?) {
                Log.e(TAG,"success result : ${data.toString()}")
                runOnUiThread{
                    mResultTv.text = data.toString()
                }
            }

            override fun onFailed(error: Any?) {
                Log.e(TAG,"fail msg : ${error.toString()}")
            }
        })
    }
    data class LoginReq(val mobi:String="18648957777",val password:String="cn5123456")
}