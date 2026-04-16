package com.example.mindfulinha

import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import android.graphics.Color

class EmptyFragment : Fragment() {

    private var progressDialog: AlertDialog? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_empty, container, false)

        // 텍스트 입력 필드
        val textInputEditText = view.findViewById<TextInputEditText>(R.id.textInputEditText)

        // 익명 매칭 버튼
        val anonymousMatchingButton = view.findViewById<Button>(R.id.anonymousMatchingButton)
        anonymousMatchingButton.setOnClickListener {
            val inputText = textInputEditText.text.toString()

            // AlertDialog 보이기
            showMatchingProgressDialog()

            // 익명 매칭 로직을 비동기적으로 처리
            handler.postDelayed({
                // 매칭이 완료되었을 때 AlertDialog 닫고 완료 메시지 보이기
                dismissMatchingProgressDialog()
                showMatchingCompleteDialog()
            }, 3000) // 예시로 3초 후에 매칭이 완료되었다고 가정
        }

        // 전문가 매칭 버튼
        val expertMatchingButton = view.findViewById<Button>(R.id.expertMatchingButton)
        expertMatchingButton.setOnClickListener {
            val inputText = textInputEditText.text.toString()

            // AlertDialog 보이기
            showMatchingProgressDialog()

            // 전문가 매칭 로직을 비동기적으로 처리
            handler.postDelayed({
                // 매칭이 완료되었을 때 AlertDialog 닫고 완료 메시지 보이기
                dismissMatchingProgressDialog()
                showMatchingCompleteDialog()
            }, 2000) // 예시로 2초 후에 매칭이 완료되었다고 가정
        }

        // 방 만들기 버튼
        val createRoomButton = view.findViewById<Button>(R.id.createRoomButton)
        createRoomButton.setOnClickListener {
            // MakingRoom Activity로 이동
            val intent = Intent(activity, MakingRoom::class.java)
            startActivityForResult(intent, CREATE_ROOM_REQUEST_CODE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_ROOM_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val roomTitle = data?.getStringExtra("ROOM_TITLE") ?: return
            createRoomView(roomTitle)
        }
    }

    private fun createRoomView(roomTitle: String) {
        // [방 만들기] 버튼의 높이를 가져와서 [만들어진 방]의 높이로 설정
        val createRoomButton = view?.findViewById<Button>(R.id.createRoomButton)
        val buttonHeight = createRoomButton?.height ?: 0

        // [만들어진 방] 레이아웃 설정
        val roomLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(173, 255, 173)) // 좀 더 연한 초록색 설정
            setPadding(16, 16, 16, 16)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                buttonHeight * 2 // [방 만들기] 버튼 높이의 2배로 설정
            ).apply {
                setMargins(0, 16, 0, 16) // 적절한 간격 설정
            }
        }

        // 방 제목 설정
        val roomTitleTextView = TextView(context).apply {
            text = "방 제목: $roomTitle"
            textSize = 18f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 16, 0, 0) // 왼쪽 상단 정렬
            }
        }

        // 입장하기 버튼 설정
        val enterRoomButton = Button(context).apply {
            text = "입장하기"
            setTextColor(Color.WHITE)
            setBackgroundResource(R.drawable.button_background)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 16, 0) // 오른쪽 중앙 정렬
            }
            setOnClickListener {
                val intent = Intent(context, ChattingRoom::class.java)
                startActivity(intent)
            }
        }

        // [만들어진 방] 레이아웃에 추가
        roomLayout.addView(roomTitleTextView)
        roomLayout.addView(enterRoomButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = android.view.Gravity.END or android.view.Gravity.CENTER_VERTICAL
        })

        // [만들어진 방]을 컨테이너에 추가
        val container = view?.findViewById<LinearLayout>(R.id.container)
        container?.addView(roomLayout)
    }

    private fun showMatchingProgressDialog() {
        progressDialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.matching_loading_message))
            .setCancelable(false)
            .create()
        progressDialog?.show()
    }

    private fun dismissMatchingProgressDialog() {
        progressDialog?.dismiss()
    }

    private fun showMatchingCompleteDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("매칭이 완료되었습니다!")
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    companion object {
        const val CREATE_ROOM_REQUEST_CODE = 1
    }
}
