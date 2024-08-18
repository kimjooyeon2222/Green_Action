package com.example.green_action;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quiz_database";
    private static final int DATABASE_VERSION = 1;

    // 테이블 이름과 컬럼 정의
    private static final String TABLE_USER = "user_table";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_PASSWORD = "user_pw";
    private static final String COLUMN_USER_NAME = "username";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_CONTACT = "contact";
    private static final String COLUMN_USER_GENDER = "gender";
    private static final String COLUMN_USER_PROFILE_IMAGE = "profileImage";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 삭제 후 재생성
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
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
}