package com.example.green_action;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.green_action.remote.FirebaseClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    private FirebaseAuth mAuth;
    private FirebaseClient firebaseClient;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Layout을 인플레이트하고 View를 반환합니다
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // FirebaseAuth 인스턴스 초기화
        mAuth = FirebaseAuth.getInstance();
        firebaseClient = new FirebaseClient();

        // UI 요소 참조
        ImageView userProfileImage = view.findViewById(R.id.user_profile_image);
        TextView userIdText = view.findViewById(R.id.user_id_text);
        TextView userEmailText = view.findViewById(R.id.user_email_text);
        TextView userNameText = view.findViewById(R.id.user_name_text);
        TextView userContactText = view.findViewById(R.id.user_contact_text);
        Button logoutButton = view.findViewById(R.id.logout_button);

        // 사용자 ID 가져오기
        userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (userId != null) {
            // 사용자 데이터 읽기
            firebaseClient.loadUserData(userId, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        // 사용자 데이터 UI에 설정
                        userIdText.setText(user.id);
                        userEmailText.setText(user.email);
                        userNameText.setText(user.name);
                        userContactText.setText(user.contact);

                        // 프로필 이미지 로드
                        if (user.profileImage != null && !user.profileImage.isEmpty()) {
                            Glide.with(SettingsFragment.this)
                                    .load(user.profileImage)
                                    .placeholder(R.drawable.ic_profile_placeholder)
                                    .into(userProfileImage);
                        } else {
                            Log.w(TAG, "Profile image URL is null or empty");
                        }
                    } else {
                        Log.e(TAG, "User data is null");
                        Toast.makeText(getActivity(), "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Database error: " + databaseError.getMessage());
                    Toast.makeText(getActivity(), "데이터베이스 오류: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "User ID is null");
            Toast.makeText(getActivity(), "사용자 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        // 로그아웃 버튼 클릭 리스너
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut(); // Firebase에서 로그아웃
            Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

            // 로그인 화면으로 이동
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

            // 현재 액티비티 종료 (MainActivity에서 SettingsFragment를 사용하는 경우)
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        return view;
    }
}