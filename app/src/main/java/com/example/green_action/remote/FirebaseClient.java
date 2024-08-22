package com.example.green_action.remote;

import android.util.Log;

import com.example.green_action.User;
import com.example.green_action.Ranking;
import com.example.green_action.DailyQuiz;
import com.example.green_action.Post;
import com.example.green_action.Comment;
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

    // 퀴즈 진행 상태 저장 메서드
    public void saveQuizProgress(String userId, int quizId, boolean isSolved) {
        DatabaseReference userQuizRef = dbRef.child("users").child(userId).child("quiz_progress").child(String.valueOf(quizId));
        userQuizRef.setValue(isSolved ? 1 : 0).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Quiz progress saved successfully.");
            } else {
                Log.e(TAG, "Failed to save quiz progress", task.getException());
            }
        });
    }

    // 퀴즈 진행 상태 전체를 Firebase에서 불러오는 메서드
    public void loadAllQuizProgress(String userId, ValueEventListener listener) {
        DatabaseReference quizProgressRef = dbRef.child("users").child(userId).child("quiz_progress");
        quizProgressRef.addListenerForSingleValueEvent(listener);
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

    // 게시글 데이터를 Firebase에서 불러오는 메서드
    public void loadPostData(String postId, ValueEventListener listener) {
        DatabaseReference postRef = dbRef.child("posts").child(postId);
        postRef.addListenerForSingleValueEvent(listener);
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

    // 댓글 데이터를 Firebase에서 불러오는 메서드
    public void loadCommentData(String commentId, ValueEventListener listener) {
        DatabaseReference commentRef = dbRef.child("comments").child(commentId);
        commentRef.addListenerForSingleValueEvent(listener);
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

    // 랭킹 데이터를 Firebase에서 불러오는 메서드
    public void loadRankingData(String userId, ValueEventListener listener) {
        DatabaseReference rankingRef = dbRef.child("ranking").child(userId);
        rankingRef.addListenerForSingleValueEvent(listener);
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

    // 일일 퀴즈 데이터를 Firebase에서 불러오는 메서드
    public void loadDailyQuizData(String quizId, ValueEventListener listener) {
        DatabaseReference dailyQuizRef = dbRef.child("daily_quiz").child(quizId);
        dailyQuizRef.addListenerForSingleValueEvent(listener);
    }
}
