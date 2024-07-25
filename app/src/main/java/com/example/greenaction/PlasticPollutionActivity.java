package com.example.greenaction;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class PlasticPollutionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plastic_pollution);

        // 상태 표시줄 색상 변경
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}