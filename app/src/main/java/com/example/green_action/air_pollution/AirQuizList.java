package com.example.green_action.air_pollution;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.green_action.R;

public class AirQuizList extends Fragment {

    private static final int NUM_COLUMNS = 5;
    private static final int NUM_ROWS = 6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // air_pollution_quiz_list.xml 레이아웃을 설정
        View view = inflater.inflate(R.layout.air_pollution_quiz_list, container, false);

        GridLayout gridLayout = view.findViewById(R.id.gridLayout);

        // GridLayout의 열과 행 수를 설정
        gridLayout.setColumnCount(NUM_COLUMNS);
        gridLayout.setRowCount(NUM_ROWS);

        // 버튼 개수만큼 루프를 돌며 버튼 생성 및 설정
        for (int i = 1; i <= NUM_COLUMNS * NUM_ROWS; i++) {
            Button button = new Button(getActivity());
            button.setText(String.valueOf(i));

            // 버튼의 레이아웃 파라미터 설정
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.setMargins(4, 4, 4, 4); // 버튼 간격 조정
            button.setLayoutParams(params);

            // 버튼 클릭 시 처리
            button.setOnClickListener(v -> handleButtonClick(button.getText().toString()));

            // GridLayout에 버튼 추가
            gridLayout.addView(button);
        }

        return view;
    }

    private void handleButtonClick(String buttonText) {
        // 클릭된 버튼의 텍스트를 기반으로 처리
        Toast.makeText(getActivity(), "Clicked: " + buttonText, Toast.LENGTH_SHORT).show();
    }
}