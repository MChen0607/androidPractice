package com.example.broadcastbestpractice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.ResultReceiver
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity :AppCompatActivity() {
    lateinit var  receiver: ForceOfflineReceiver
    override fun onResume() {
        super.onResume()
        val intentFilter=IntentFilter()
        intentFilter.addAction("com.example.broadcastbestpractice.FORCE_OFFLINE")
        receiver=ForceOfflineReceiver()
        registerReceiver(receiver,intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    inner class ForceOfflineReceiver :BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            AlertDialog.Builder(context).apply {
                setTitle("Warning")
                setMessage("You are forced to be offline.please try to login again.")
                setCancelable(false)
                setPositiveButton("OK"){_,_->
                    ActivityCollector.finishAll() //摧毁所有Activity
                    val i=Intent(context,LoginActivity::class.java)
                    context.startActivity(i)//重新启动LoginActivity
                }
                show()
            }
        }
    }
}