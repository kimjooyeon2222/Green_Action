package com.example.green_action.remote;

import android.util.Log;

import com.example.green_action.Comment;
import com.example.green_action.DailyQuiz;
import com.example.green_action.Post;
import com.example.green_action.User;
import com.example.green_action.Ranking;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseClient {

    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    private static final String TAG = "FirebaseClient";

    // 사용자 데이터를 Firebase에 저장하는 메서드
    public void saveUserData(String userId, User user) {
        Log.d(TAG, "Saving user data for userId: " + userId);
        dbRef.child("users").child(userId).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "User data saved successfully.");
            } else {
                Log.e(TAG, "Failed to save user data", task.getException());
            }
        });
    }

    // 사용자 데이터를 Firebase에서 읽어오는 메서드
    public void loadUserData(String userId, ValueEventListener listener) {
        if (userId != null && !userId.isEmpty()) {
            Log.d(TAG, "Loading user data for userId: " + userId);
            DatabaseReference userRef = dbRef.child("users").child(userId);
            userRef.addListenerForSingleValueEvent(listener);
        } else {
            Log.e(TAG, "User ID is null or empty");
        }
    }

    // 퀴즈 진행도를 저장하는 메서드
    public void saveQuizProgress(String userId, int quizId, boolean isSolved) {
        DatabaseReference userQuizRef = dbRef.child("users").child(userId).child("quiz_progress").child(String.valueOf(quizId));
        userQuizRef.setValue(isSolved).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Quiz progress saved successfully.");
            } else {
                Log.e(TAG, "Failed to save quiz progress", task.getException());
            }
        });
    }

    // 퀴즈 진행도를 불러오는 메서드
    public void loadQuizProgress(String userId, int quizId, ValueEventListener listener) {
        DatabaseReference userQuizRef = dbRef.child("users").child(userId).child("quiz_progress").child(String.valueOf(quizId));
        userQuizRef.addListenerForSingleValueEvent(listener);
    }

    // 게시글을 저장하는 메서드
    public void savePostData(String postId, Post post) {
        dbRef.child("posts").child(postId).setValue(post).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Post data saved successfully.");
            } else {
                Log.e(TAG, "Failed to save post data", task.getException());
            }
        });
    }

    // 댓글을 저장하는 메서드
    public void saveCommentData(String commentId, Comment comment) {
        dbRef.child("comments").child(commentId).setValue(comment).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Comment data saved successfully.");
            } else {
                Log.e(TAG, "Failed to save comment data", task.getException());
            }
        });
    }

    // 랭킹 데이터를 저장하는 메서드
    public void saveRankingData(String userId, Ranking rank) {
        dbRef.child("ranking").child(userId).setValue(rank).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Ranking data saved successfully.");
            } else {
                Log.e(TAG, "Failed to save ranking data", task.getException());
            }
        });
    }

    // 일일 퀴즈를 저장하는 메서드
    public void saveDailyQuizData(String quizId, DailyQuiz dailyQuiz) {
        dbRef.child("daily_quiz").child(quizId).setValue(dailyQuiz).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Daily quiz data saved successfully.");
            } else {
                Log.e(TAG, "Failed to save daily quiz data", task.getException());
            }
        });
    }
}