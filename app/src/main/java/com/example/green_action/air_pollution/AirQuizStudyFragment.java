package com.example.green_action.air_pollution;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.green_action.R;

public class AirQuizStudyFragment extends Fragment {

    private int quizNumber;
    private TextView textView;
    private Button buttonStartQuiz;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_air_pollution_quiz_study, container, false);
        textView = view.findViewById(R.id.textViewAirPollution);
        buttonStartQuiz = view.findViewById(R.id.buttonQuizAndLearn);

        if (getArguments() != null) {
            quizNumber = getArguments().getInt("QUIZ_NUMBER");
            textView.setText("퀴즈 " + quizNumber + "에 대한 학습 내용입니다.");
        }

        buttonStartQuiz.setOnClickListener(v -> startQuiz());

        return view;
    }

    private void startQuiz() {
        AirQuizSectionFragment sectionFragment = new AirQuizSectionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("QUIZ_NUMBER", quizNumber);
        sectionFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, sectionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getParentFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack(); // 이전 프래그먼트로 돌아가기
                } else {
                    requireActivity().getOnBackPressedDispatcher().onBackPressed(); // 기본 뒤로가기 동작
                }
            }
        });
    }
}