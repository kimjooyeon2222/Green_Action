package com.example.green_action;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.green_action.R;

public class QuizActivity extends AppCompatActivity {

    private DataBaseHandler dbHandler;
    private int currentQuizId;
    private TextView questionText;
    private Button submitButton;
    private ImageView lockIcon, checkIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        dbHandler = new DataBaseHandler(this);
        questionText = findViewById(R.id.question_text);
        submitButton = findViewById(R.id.submit_button);
        lockIcon = findViewById(R.id.lock_icon);
        checkIcon = findViewById(R.id.check_icon);

        // 퀴즈 진행도를 불러와서 초기화
        currentQuizId = 1; // 첫 번째 퀴즈부터 시작
        loadQuizProgress(currentQuizId);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 퀴즈 제출 로직
                if (submitAnswer()) {
                    markQuizAsSolved(currentQuizId);
                    unlockNextQuiz(currentQuizId + 1); // 다음 퀴즈 잠금 해제
                    updateUI();
                } else {
                    Toast.makeText(QuizActivity.this, "정답이 아닙니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadQuizProgress(int quizId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query("Quiz", null, "id=?", new String[]{String.valueOf(quizId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String question = cursor.getString(cursor.getColumnIndexOrThrow("question"));
            int isLocked = cursor.getInt(cursor.getColumnIndexOrThrow("is_locked"));
            int isSolved = cursor.getInt(cursor.getColumnIndexOrThrow("is_solved"));

            questionText.setText(question);
            lockIcon.setVisibility(isLocked == 1 ? View.VISIBLE : View.INVISIBLE);
            checkIcon.setVisibility(isSolved == 1 ? View.VISIBLE : View.INVISIBLE);

            cursor.close();
        }
    }

    private boolean submitAnswer() {
        // 실제 답변 검증 로직 구현
        return true; // 이 예시에서는 항상 정답 처리
    }

    private void markQuizAsSolved(int quizId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_solved", 1);
        db.update("Quiz", values, "id=?", new String[]{String.valueOf(quizId)});
    }

    private void unlockNextQuiz(int nextQuizId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_locked", 0);
        db.update("Quiz", values, "id=?", new String[]{String.valueOf(nextQuizId)});
    }

    private void updateUI() {
        loadQuizProgress(currentQuizId);
    }
}