package com.example.mindfulinha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private DiaryViewModel diaryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 여기서 만약 IntroActivity를 이미 보았는지 체크하고 MainActivity로 리다이렉트하게 합니다.
        SharedPreferences sharedPreferences = getSharedPreferences("IntroPref", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        diaryViewModel = new ViewModelProvider(this).get(DiaryViewModel.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Intent에서 SELECTED_NAV_ITEM 데이터를 가져와서 처리
        Intent intent = getIntent();
        int selectedNavItem = R.id.navigation_home; // 기본값 설정
        if (intent != null) {
            selectedNavItem = intent.getIntExtra("SELECTED_NAV_ITEM", R.id.navigation_home);
        }

        // 선택된 항목에 따라 프래그먼트를 설정합니다.
        setFragment(selectedNavItem);
        bottomNavigationView.setSelectedItemId(selectedNavItem);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                setFragment(itemId);
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 앱이 종료될 때, isFirstRun을 true로 설정
        SharedPreferences sharedPreferences = getSharedPreferences("IntroPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstRun", true);
        editor.apply();
    }

    private void setFragment(int itemId) {
        Fragment selectedFragment = null;
        if (itemId == R.id.navigation_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.navigation_diary) {
            selectedFragment = new DiaryFragment();
        } else if (itemId == R.id.navigation_lock) {
            selectedFragment = new LockFragment();
        } else if (itemId == R.id.navigation_consult) {
            selectedFragment = new EmptyFragment();
        } else if (itemId == R.id.navigation_sleep) {
            selectedFragment = new SleepFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }
    }

    public DiaryViewModel getDiaryViewModel() {
        return diaryViewModel;
    }

}
