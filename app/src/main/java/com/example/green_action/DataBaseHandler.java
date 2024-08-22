package com.example.green_action;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quiz_database";
    private static final int DATABASE_VERSION = 1;

    // 유저 테이블 이름과 컬럼 정의
    private static final String TABLE_USER = "user_table";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_PASSWORD = "user_pw";
    private static final String COLUMN_USER_NAME = "username";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_CONTACT = "contact";
    private static final String COLUMN_USER_GENDER = "gender";
    private static final String COLUMN_USER_PROFILE_IMAGE = "profileImage";

    // 퀴즈 진행 상태 테이블 이름과 컬럼 정의
    private static final String TABLE_QUIZ_PROGRESS = "quiz_progress";
    private static final String COLUMN_QUIZ_ID = "quiz_id";
    private static final String COLUMN_QUIZ_STATUS = "is_solved"; // 0: 미해결, 1: 해결됨

    // 생성자
    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 유저 테이블 생성 쿼리
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_USER_ID + " TEXT PRIMARY KEY," +
                COLUMN_USER_PASSWORD + " TEXT," +
                COLUMN_USER_NAME + " TEXT," +
                COLUMN_USER_EMAIL + " TEXT," +
                COLUMN_USER_CONTACT + " TEXT," +
                COLUMN_USER_GENDER + " TEXT," +
                COLUMN_USER_PROFILE_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        // 퀴즈 진행 상태 테이블 생성 쿼리
        String CREATE_QUIZ_PROGRESS_TABLE = "CREATE TABLE " + TABLE_QUIZ_PROGRESS + " (" +
                COLUMN_QUIZ_ID + " INTEGER PRIMARY KEY," +
                COLUMN_QUIZ_STATUS + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_QUIZ_PROGRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 삭제 후 재생성
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_PROGRESS);
        onCreate(db);
    }

    // 사용자 데이터 추가 메서드
    public long addUserData(String id, String pw, String name, String email, String contact, String gender, String profileImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, id);
        values.put(COLUMN_USER_PASSWORD, pw);
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_CONTACT, contact);
        values.put(COLUMN_USER_GENDER, gender);
        values.put(COLUMN_USER_PROFILE_IMAGE, profileImage);

        // 테이블에 데이터 삽입
        long result = db.insert(TABLE_USER, null, values);
        db.close(); // 데이터베이스 닫기
        return result;
    }

    // 퀴즈 진행 상태 추가/업데이트 메서드
    public long addOrUpdateQuizProgress(int quizId, boolean isSolved) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUIZ_ID, quizId);
        values.put(COLUMN_QUIZ_STATUS, isSolved ? 1 : 0);

        // 퀴즈 진행 상태를 업데이트하거나 삽입
        long result = db.replace(TABLE_QUIZ_PROGRESS, null, values);
        db.close(); // 데이터베이스 닫기
        return result;
    }

    // 퀴즈 진행 상태 업데이트 메서드
    public void updateQuizStatus(int quizId, boolean isSolved) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUIZ_STATUS, isSolved ? 1 : 0);

        int rowsUpdated = db.update(TABLE_QUIZ_PROGRESS, values, COLUMN_QUIZ_ID + "=?", new String[]{String.valueOf(quizId)});
        if (rowsUpdated == 0) {
            Log.e("DatabaseError", "Failed to update quiz status. No rows updated.");
        }
        db.close();
    }

    // 퀴즈 진행 상태 로드 메서드
    public boolean getQuizStatus(int quizId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_QUIZ_STATUS + " FROM " + TABLE_QUIZ_PROGRESS + " WHERE " + COLUMN_QUIZ_ID + "=?";
        Cursor cursor = null;
        boolean status = false;

        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(quizId)});
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(COLUMN_QUIZ_STATUS);
                if (columnIndex != -1) {
                    int statusValue = cursor.getInt(columnIndex);
                    status = statusValue == 1;
                } else {
                    Log.e("DatabaseError", "Column '" + COLUMN_QUIZ_STATUS + "' not found");
                }
            } else {
                Log.e("DatabaseError", "No quiz progress found for quizId: " + quizId);
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error retrieving quiz status for quizId: " + quizId, e);
        } finally {
            if (cursor != null) {
                cursor.close(); // Cursor를 닫습니다.
            }
            db.close(); // 데이터베이스 연결을 닫습니다.
        }

        return status;
    }

    // 마지막으로 푼 퀴즈 번호 가져오기
    public int getLastSolvedQuiz() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT MAX(" + COLUMN_QUIZ_ID + ") FROM " + TABLE_QUIZ_PROGRESS + " WHERE " + COLUMN_QUIZ_STATUS + "=1";
        Cursor cursor = null;
        int lastSolvedQuiz = 0;

        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("MAX(" + COLUMN_QUIZ_ID + ")");
                if (columnIndex != -1) {
                    lastSolvedQuiz = cursor.getInt(columnIndex);
                } else {
                    Log.e("DatabaseError", "Column 'MAX(" + COLUMN_QUIZ_ID + ")' not found");
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error retrieving last solved quiz", e);
        } finally {
            if (cursor != null) {
                cursor.close(); // Cursor를 닫습니다.
            }
            db.close(); // 데이터베이스 연결을 닫습니다.
        }

        return lastSolvedQuiz;
    }
}