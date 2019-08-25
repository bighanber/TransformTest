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

    @FuncConst("2333")
    fun testFun() {
//        Log.e("asd", System.currentTimeMillis().toString())
    }
}
