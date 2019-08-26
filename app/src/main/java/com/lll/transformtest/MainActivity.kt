package com.lll.transformtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testFun()
    }

    @FuncConst("testFun")
    fun testFun() {
        Thread.sleep(3000)
//        Log.e("asd", System.currentTimeMillis().toString())
    }
}
