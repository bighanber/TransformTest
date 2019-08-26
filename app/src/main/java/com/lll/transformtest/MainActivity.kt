package com.lll.transformtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testmodule.FuncConst
import com.example.testmodule.TestActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testFun()
        testFun2()
        tv_click.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }
    }

    @FuncConst("testFun")
    fun testFun() {
        Thread.sleep(3000)
//        Log.e("asd", System.currentTimeMillis().toString())
    }

    @FuncConst("testFun2")
    fun testFun2() {

    }
}
