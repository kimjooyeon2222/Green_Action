package com.example.green_action.Community;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.green_action.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommunityActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        ImageButton buttonback = findViewById(R.id.backButton);
        buttonback.setOnClickListener(v -> finish());

        // FirebaseAuth 인스턴스 초기화
        mAuth = FirebaseAuth.getInstance();

        // GoogleSignInClient 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        EditText id = findViewById(R.id.login_id);
        EditText pw = findViewById(R.id.login_password);

        Button login = findViewById(R.id.login_button);
        Button register = findViewById(R.id.join_button);
        com.google.android.gms.common.SignInButton googleSignInButton = findViewById(R.id.google_sign_in_button);

        login.setOnClickListener(v -> {
            String email = id.getText().toString().trim();
            String password = pw.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                loginUser(email, password);
            } else {
                Toast.makeText(CommunityActivity.this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(v -> {
            // 회원가입 화면으로 이동
            Intent intent = new Intent(CommunityActivity.this, UserJoin.class);
            startActivity(intent);
        });

        // 구글 로그인 버튼 클릭 리스너
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String displayName = user.getDisplayName();
                            String uid = user.getUid();

                            // 로그인 성공 후 SharedPreferences에 로그인 상태 저장
                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", uid);
                            editor.putString("username", displayName != null ? displayName : "Unknown User");
                            editor.apply();

                            startActivity(new Intent(CommunityActivity.this, CommunityPostActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(CommunityActivity.this, "로그인 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(CommunityActivity.this, "Google 로그인 성공", Toast.LENGTH_SHORT).show();

                        if (user != null) {
                            String uid = user.getUid();
                            String displayName = user.getDisplayName();
                            String displayNameWithId = displayName + " (" + uid.substring(0, 7) + ")";

                            // Firebase Realtime Database에 사용자 정보 저장
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
                            userRef.child("username").setValue(displayNameWithId)
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Username saved successfully"))
                                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to save username", e));

                            // 로그인 성공 후 SharedPreferences에 로그인 상태 저장
                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", uid); // Firebase User ID 저장
                            editor.putString("username", displayNameWithId); // 사용자 이름 저장
                            editor.apply();

                            // CommunityPostActivity로 이동
                            Intent intent = new Intent(CommunityActivity.this, CommunityPostActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(CommunityActivity.this, "Google 로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
