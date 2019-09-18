package com.example.testmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        newFunc()
    }

    @FuncConst("newFunc")
    fun newFunc() {
        Thread.sleep(1000)
    }
}
