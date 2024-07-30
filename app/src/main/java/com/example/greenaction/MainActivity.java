package com.example.greenaction;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    // Toolbar와 DrawerLayout 선언
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar 초기화 및 앱의 액션바로 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // DrawerLayout과 NavigationView 초기화
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 네비게이션 드로어 토글 설정
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView의 항목 선택 리스너 설정
        navigationView.setNavigationItemSelectedListener(this::handleNavigation);
        // BottomNavigationView의 아이템 선택 리스너 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(this::handleBottomNavigation);
    }

    // NavigationView의 항목 선택을 처리하는 메서드
    private boolean handleNavigation(@NonNull MenuItem item) {
        return navigateTo(item.getItemId());
    }

    // BottomNavigationView의 아이템 선택을 처리하는 메서드
    private boolean handleBottomNavigation(MenuItem item) {
        return navigateTo(item.getItemId());
    }

    // 화면 전환을 처리하는 공통 메서드
    private boolean navigateTo(int id) {
        Intent intent = null;
        if (id == R.id.nav_home) {
            // Home 화면으로 이동
            intent = new Intent(MainActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else if (id == R.id.nav_quiz) {
            // Quiz 화면으로 이동
            intent = new Intent(MainActivity.this, QuizActivity.class);
        } else if (id == R.id.nav_leaderboard) {
            // Leaderboard 화면으로 이동
            intent = new Intent(MainActivity.this, LeaderboardActivity.class);
        } else if (id == R.id.nav_settings) {
            // Settings 화면으로 이동
            intent = new Intent(MainActivity.this, SettingsActivity.class);
        }

        // Intent가 null이 아닐 경우 화면 전환 시도
        if (intent != null) {
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // 예외 발생 시 사용자에게 알림 및 로그 기록
                Toast.makeText(this, "활동을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Activity not found: " + e.getMessage());
            }
            drawerLayout.closeDrawer(GravityCompat.START); // 드로어 닫기
            return true;
        }
        return false;
    }

    // 각 오염 유형 버튼 클릭 시 실행될 코드
    public void onPartitionClick(View view) {
        int id = view.getId();
        Intent intent = null;
        if (id == R.id.air_pollution) {
            // 대기 오염 화면으로 이동
            intent = new Intent(this, AirPollutionActivity.class);
        } else if (id == R.id.water_pollution) {
            // 수질 오염 화면으로 이동
            intent = new Intent(this, WaterPollutionActivity.class);
        } else if (id == R.id.soil_pollution) {
            // 토양 오염 화면으로 이동
            intent = new Intent(this, SoilPollutionActivity.class);
        } else if (id == R.id.plastic_pollution) {
            // 플라스틱 오염 화면으로 이동
            intent = new Intent(this, PlasticPollutionActivity.class);
        }

        // Intent가 null이 아닐 경우 화면 전환 시도
        if (intent != null) {
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // 예외 발생 시 사용자에게 알림 및 로그 기록
                Toast.makeText(this, "활동을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Activity not found: " + e.getMessage());
            }
        }
    }

    // "더 보기" 버튼 클릭 시 실행될 코드
    public void onMoreClick(View view) {
        Intent intent = new Intent(this, CommunityActivity.class);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // 예외 발생 시 사용자에게 알림 및 로그 기록
            Toast.makeText(this, "활동을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Activity not found: " + e.getMessage());
        }
    }
}