package com.example.mindfulinha

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.widget.Toast

class LockService : Service() {

    private val handler = Handler()
    private var lockTimeMillis: Long = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lockTimeMillis = intent?.getLongExtra("LOCK_TIME", 0) ?: 0

        if (lockTimeMillis > 0) {
            handler.postDelayed({
                val unlockIntent = Intent("com.example.mindfulinha.UNLOCK_SCREEN")
                sendBroadcast(unlockIntent)
                stopSelf()
                Toast.makeText(this, "잠금 해제", Toast.LENGTH_SHORT).show()
            }, lockTimeMillis)

            val lockScreenIntent = Intent(this, LockScreenActivity::class.java)
            lockScreenIntent.putExtra("LOCK_TIME_REMAINING", lockTimeMillis - 100)
            lockScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(lockScreenIntent)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
