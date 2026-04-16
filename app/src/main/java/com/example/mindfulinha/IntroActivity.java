package com.example.mindfulinha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(view -> {
            // MainActivity로 이동
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // 사용자가 이 화면으로 돌아올 수 없도록 현재 액티비티 종료

            // 첫 실행 여부 SharedPreferences에 저장
            SharedPreferences sharedPreferences = getSharedPreferences("IntroPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        });
    }
}
