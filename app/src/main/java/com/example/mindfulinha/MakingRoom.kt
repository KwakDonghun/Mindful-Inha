package com.example.mindfulinha

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MakingRoom : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_making_room)

        val roomTitleEditText = findViewById<EditText>(R.id.roomTitleEditText)
        val createRoomButton = findViewById<Button>(R.id.createRoomButton)

        createRoomButton.setOnClickListener {
            val roomTitle = roomTitleEditText.text.toString()
            // 방 만들기 로직 추가

            // Example: 방 정보를 EmptyFragment로 전달
            val intent = Intent()
            intent.putExtra("ROOM_TITLE", roomTitle)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
