package com.example.mindfulinha

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class LockScreenActivity : AppCompatActivity() {

    private val unlockReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val unlockIntent = Intent(this@LockScreenActivity, MainActivity::class.java)
            unlockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(unlockIntent)
            finish()
        }
    }

    private var isActivityTop = false
    private lateinit var motivationalQuotes: Array<String>
    private lateinit var handler: Handler
    private lateinit var tvRemainingTime: TextView
    private var lockEndTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_screen)

        motivationalQuotes = arrayOf(
            "행복은 소유가 아니라 경험에서 온다.- 알버트 아인슈타인",
            "자신에게 시간을 줄 때 진정한 자신을 찾을 수 있다.",
            "디지털 세계를 떠나 현실 세계를 만끽하세요.",
            "잠시 멈추고, 심호흡하고, 당신 주위의 아름다움을 느껴보세요.",
            "조용함 속에서 진정한 평화가 찾아옵니다.",
            "소셜 미디어는 잠시 잊고, 자신에게 집중하세요.",
            "현실 속의 순간들이 당신을 더 강하게 만든다.",
            "테크놀로지를 잠시 내려놓고, 자연과 다시 연결되세요.",
            "내면의 목소리를 듣기 위해 외부의 소음을 줄여보세요.",
            "디지털 디톡스는 마음을 새롭게 하고, 영혼을 치유합니다.",
            "스크린을 넘어서, 진짜 삶이 기다리고 있습니다.",
            "오늘 하루는 오프라인에서 나만의 시간을 보내세요.",
            "연결을 끊으면 더 깊은 연결을 찾을 수 있습니다.",
            "디지털 세상에서 벗어나, 현실의 매력을 발견하세요.",
            "지금 이 순간에 집중하세요, 그리고 진정한 자유를 느껴보세요.",
            "화면을 끄고, 마음의 창을 여세요.",
            "자연 속에서 잃어버린 나를 다시 찾으세요.",
            "휴식을 통해 더 나은 내일을 준비하세요.",
            "디지털 휴식은 새로운 영감을 가져다줍니다.",
            "지금의 고요함이 미래의 창조성을 키웁니다.",
            "조용함은 마음의 음악이다. - 칼릴 지브란",
            "단순함은 궁극의 정교함이다. - 레오나르도 다 빈치",
            "기술은 좋은 종이지만 나쁜 주인이다. - 크리스천 네스터",
            "모든 위대한 변화는 혼돈 속에서 시작된다. - 디팩 초프라",
            "가장 중요한 것은 우리가 어떻게 시간을 사용하는가이다. - 리오넬 크리닝",
            "진정한 여행은 새로운 풍경을 보는 것이 아니라 새로운 눈을 가지는 것이다. - 마르셀 프루스트",
            "내면의 평화를 찾으려면, 외부의 소음을 줄여야 한다. - 베네딕트 컴버배치",
            "삶의 진정한 본질은 연결이 아니라 존재함에 있다. - J.K. 롤링",
            "조용함은 지혜의 시작이다. - 세네카",
            "기술은 인간의 능력을 확장시키기 위한 것이어야 한다, 인간의 삶을 지배하기 위한 것이 아니라. - 크리스 앤더슨"
        )

        val tvMotivationalQuote: TextView = findViewById(R.id.tvMotivationalQuote)
        val randomQuote = motivationalQuotes[Random.nextInt(motivationalQuotes.size)]
        tvMotivationalQuote.text = randomQuote

        tvRemainingTime = findViewById(R.id.tvRemainingTime)

        handler = Handler(Looper.getMainLooper())

        val btnEmergencyExit: Button = findViewById(R.id.btnEmergencyExit)
        btnEmergencyExit.setOnClickListener {
            val unlockIntent = Intent(this, MainActivity::class.java)
            unlockIntent.putExtra("SELECTED_NAV_ITEM", R.id.navigation_lock)
            unlockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(unlockIntent)
            finish()
        }

        val filter = IntentFilter("com.example.mindfulinha.UNLOCK_SCREEN")
        registerReceiver(unlockReceiver, filter)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        lockEndTime = System.currentTimeMillis() + intent.getLongExtra("LOCK_TIME_REMAINING", 0)

        updateRemainingTime()
    }

    private fun updateRemainingTime() {
        val remainingMillis = lockEndTime - System.currentTimeMillis()
        if (remainingMillis > 0) {
            val hours = (remainingMillis / 1000) / 3600
            val minutes = ((remainingMillis / 1000) % 3600) / 60
            val seconds = (remainingMillis / 1000) % 60
            tvRemainingTime.text = String.format("남은 시간: %02d:%02d:%02d", hours, minutes, seconds)
            handler.postDelayed({ updateRemainingTime() }, 1000)
        } else {
            val unlockIntent = Intent(this, MainActivity::class.java)
            unlockIntent.putExtra("SELECTED_NAV_ITEM", R.id.navigation_lock)
            unlockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(unlockIntent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(unlockReceiver)
        handler.removeCallbacksAndMessages(null)
    }

    override fun onBackPressed() {
        // 뒤로 가기 버튼을 비활성화하여 LockScreenActivity가 최상위에 유지됩니다.
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus && !isActivityTop) {
            val intent = Intent(this, LockScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        isActivityTop = true
    }

    override fun onPause() {
        super.onPause()
        isActivityTop = false
    }
}
