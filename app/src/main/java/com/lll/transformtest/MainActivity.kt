package com.lll.transformtest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testmodule.FuncConst
import com.example.testmodule.TestActivity
import kotlinx.android.synthetic.main.activity_main.*

//python systrace.py -t 20 -o ~/test.html -a com.lll.transformtest gfx view wm am pm res dalvik sync
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testFun()
        tv_click.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        testFun2()
    }

    @FuncConst("testFun")
    fun testFun() {
        Thread.sleep(3000)
//        Log.e("asd", System.currentTimeMillis().toString())
    }

    @FuncConst("testFun2")
    fun testFun2() {
        Thread.sleep(2000)
    }
}
