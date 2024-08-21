package com.example.green_action.air_pollution;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.green_action.DataBaseHandler;
import com.example.green_action.R;
import com.example.green_action.remote.FirebaseClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AirQuizListFragment extends Fragment {

    private static final int NUM_COLUMNS = 5;  // Define the number of columns
    private static final int NUM_ROWS = 6;     // Define the number of rows
    private GridLayout gridLayout;
    private FirebaseClient firebaseClient;
    private DataBaseHandler dbHandler;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_air_pollution_quiz_list, container, false);

        gridLayout = view.findViewById(R.id.gridLayout);
        gridLayout.setColumnCount(NUM_COLUMNS);
        gridLayout.setRowCount(NUM_ROWS);

        firebaseClient = new FirebaseClient();
        dbHandler = new DataBaseHandler(getContext());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }

        setupQuizButtons();

        // Register FragmentResultListener to handle results from AirQuizSectionFragment
        getParentFragmentManager().setFragmentResultListener("quiz_result", getViewLifecycleOwner(), (requestKey, result) -> {
            int unlockQuizNumber = result.getInt("UNLOCK_QUIZ_NUMBER");
            unlockNextQuiz(unlockQuizNumber);
        });

        // Load quiz progress from Firebase and local database
        loadQuizProgress();

        return view;
    }

    private void setupQuizButtons() {
        for (int i = 1; i <= NUM_COLUMNS * NUM_ROWS; i++) {
            Button button = new Button(getActivity());
            button.setText(String.valueOf(i));

            // 기본적으로 모든 버튼을 잠금 상태로 설정
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_lock, 0, 0);
            button.setEnabled(false); // 기본적으로 비활성화

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.setMargins(4, 4, 4, 4);
            button.setBackgroundColor(getResources().getColor(R.color.actionGreen));
            button.setTextColor(getResources().getColor(R.color.white));
            button.setLayoutParams(params);

            button.setOnClickListener(v -> handleButtonClick(Integer.parseInt(button.getText().toString())));

            gridLayout.addView(button);
        }
    }

    private void handleButtonClick(int quizNumber) {
        AirQuizStudyFragment studyFragment = new AirQuizStudyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("QUIZ_NUMBER", quizNumber);
        studyFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, studyFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadQuizProgress() {
        if (userId != null) {
            // Load quiz progress from Firebase
            firebaseClient.loadAllQuizProgress(userId, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        int quizId = Integer.parseInt(snapshot.getKey());
                        boolean isSolved = snapshot.getValue(Integer.class) == 1;
                        dbHandler.addOrUpdateQuizProgress(quizId, isSolved); // Update local database
                        updateButtonState(quizId, isSolved);
                    }
                    // 첫 번째 퀴즈는 기본적으로 잠금 해제
                    updateButtonState(1, true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }

    private void updateButtonState(int quizNumber, boolean isSolved) {
        Button button = (Button) gridLayout.getChildAt(quizNumber - 1); // 0-based index
        if (button != null) {
            if (isSolved) {
                button.setCompoundDrawables(null, null, null, null);
                button.setEnabled(true);
            } else {
                button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_lock, 0, 0);
                button.setEnabled(false);
            }
        }
    }

    public void unlockNextQuiz(int quizNumber) {
        if (quizNumber <= NUM_COLUMNS * NUM_ROWS) {
            Button button = (Button) gridLayout.getChildAt(quizNumber - 1); // 0-based index
            if (button != null) {
                button.setCompoundDrawables(null, null, null, null);
                button.setEnabled(true);

                // Save progress to Firebase and local database
                if (userId != null) {
                    firebaseClient.saveQuizProgress(userId, quizNumber, true);
                    dbHandler.addOrUpdateQuizProgress(quizNumber, true);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getParentFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack(); // Go back to the previous fragment
                } else {
                    requireActivity().getOnBackPressedDispatcher().onBackPressed(); // Default back action
                }
            }
        });
    }
}