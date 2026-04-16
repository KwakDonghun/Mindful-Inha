package com.example.mindfulinha

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mindfulinha.entity.Diary
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class DiaryViewModel : ViewModel() {
    private val _diaries = MutableLiveData<ArrayList<Diary>>(ArrayList())
    val diaries: LiveData<ArrayList<Diary>> get() = _diaries

    fun addDiary(diary: Diary) {
        val updatedList = _diaries.value ?: ArrayList()
        updatedList.add(diary)
        _diaries.value = updatedList
    }

    fun removeDiary(diary: Diary) {
        val updatedList = _diaries.value ?: ArrayList()
        updatedList.remove(diary)
        _diaries.value = updatedList
    }
}

class DiaryFragment : Fragment() {

    private lateinit var createDiaryBtn: Button
    private lateinit var inputEditText: EditText
    private var idx: Int = 1
    private lateinit var diaryViewModel: DiaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary, container, false)

        // ViewModel 초기화
        diaryViewModel = (activity as MainActivity).getDiaryViewModel()

        // View 초기화
        createDiaryBtn = view.findViewById(R.id.create_diary_btn)
        inputEditText = view.findViewById(R.id.diary_contents)

        // 다이어리 추가 버튼 클릭 리스너
        createDiaryBtn.setOnClickListener {
            val diaryContent = inputEditText.text.toString()
            if (diaryContent.isNotBlank()) {
                val diary = Diary(idx++, diaryContent, Date())
                diaryViewModel.addDiary(diary)
                inputEditText.text.clear()
            }
        }

        // LiveData 관찰
        diaryViewModel.diaries.observe(viewLifecycleOwner, Observer { diaries ->
            displayDiary(view, inflater, diaries)
        })

        return view
    }

    private fun displayDiary(view: View, inflater: LayoutInflater, diaries: List<Diary>) {
        val diaryContainer = view.findViewById<LinearLayout>(R.id.diaryContainer)
        diaryContainer.removeAllViews()

        for (diary in diaries) {
            val diaryView = inflater.inflate(R.layout.diary_item, null)

            val idxTextView = diaryView.findViewById<TextView>(R.id.diary_idx)
            val timeTextView = diaryView.findViewById<TextView>(R.id.diary_time)
            val contentTextView = diaryView.findViewById<TextView>(R.id.diary_content)
            val deleteButton = diaryView.findViewById<Button>(R.id.delete_button)

            idxTextView.text = diary.idx.toString()
            timeTextView.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(diary.createdAt)
            contentTextView.text = diary.contents

            deleteButton.setOnClickListener {
                // 삭제 버튼 클릭 시 실행할 코드
                diaryViewModel.removeDiary(diary)
            }

            diaryContainer.addView(diaryView)
        }
    }
}