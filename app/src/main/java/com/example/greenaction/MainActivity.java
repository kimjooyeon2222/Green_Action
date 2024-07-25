package com.example.greenaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 상태 표시줄 색상 변경
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        // 네비게이션 드로어 토글 설정
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                // Home 화면으로 이동
                return true;
            } else if (id == R.id.navigation_dashboard) {
                // Dashboard 화면으로 이동
                return true;
            } else if (id == R.id.navigation_notifications) {
                // Notifications 화면으로 이동
                return true;
            }
            return false;
        });
    }

    public void onPartitionClick(View view) {
        Intent intent;
        int id = view.getId();
        if (id == R.id.air_pollution) {
            intent = new Intent(this, AirPollutionActivity.class);
        } else if (id == R.id.water_pollution) {
            intent = new Intent(this, WaterPollutionActivity.class);
        } else if (id == R.id.soil_pollution) {
            intent = new Intent(this, SoilPollutionActivity.class);
        } else if (id == R.id.plastic_pollution) {
            intent = new Intent(this, PlasticPollutionActivity.class);
        } else {
            return;
        }
        startActivity(intent);
    }

    public void onMoreClick(View view) {
        // "더 보기" 클릭 시 실행할 코드 작성
    }
}