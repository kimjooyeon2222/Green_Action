package com.example.green_action.Community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.green_action.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserJoin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_join);

        ImageButton buttonback = findViewById(R.id.backButton);
        buttonback.setOnClickListener(v -> finish());

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        EditText userEmail = findViewById(R.id.join_id);
        EditText userPassword = findViewById(R.id.join_password);
        EditText userPwCk = findViewById(R.id.join_pwck);
        EditText userName = findViewById(R.id.join_name);
        EditText userContact = findViewById(R.id.join_Phone);
        RadioGroup userGenderGroup = findViewById(R.id.join_gender_group);

        Button checkButton = findViewById(R.id.check_button);
        Button confirmButton = findViewById(R.id.confirm_button);

        checkButton.setOnClickListener(v -> {
            String email = userEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(UserJoin.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().getSignInMethods().isEmpty()) {
                    Toast.makeText(UserJoin.this, "이메일이 이미 사용 중입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserJoin.this, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        confirmButton.setOnClickListener(v -> {
            String email = userEmail.getText().toString().trim();
            String pw = userPassword.getText().toString().trim();
            String pwCk = userPwCk.getText().toString().trim();
            String name = userName.getText().toString().trim();
            String contact = userContact.getText().toString().trim();
            int selectedGenderId = userGenderGroup.getCheckedRadioButtonId();
            String gender = selectedGenderId != -1 ? ((RadioButton) findViewById(selectedGenderId)).getText().toString() : "";

            // 입력값이 모두 채워졌는지 확인
            if (email.isEmpty() || pw.isEmpty() || pwCk.isEmpty() || name.isEmpty() || contact.isEmpty() || gender.isEmpty()) {
                Toast.makeText(UserJoin.this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 비밀번호 확인
            if (pw.equals(pwCk)) {
                mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            String displayNameWithId = name + " (" + uid.substring(0, 7) + ")";

                            DatabaseReference userRef = usersRef.child(uid);
                            userRef.child("pw").setValue(pw);  // 비밀번호 저장 (보안상 권장되지 않음)
                            userRef.child("username").setValue(displayNameWithId);
                            userRef.child("contact").setValue(contact);
                            userRef.child("gender").setValue(gender);

                            Toast.makeText(UserJoin.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                            // "다시 로그인 해주세요" 메시지 표시
                            Toast.makeText(UserJoin.this, "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(UserJoin.this, CommunityActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(UserJoin.this, "회원가입 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(UserJoin.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
