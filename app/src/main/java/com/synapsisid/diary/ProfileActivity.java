package com.synapsisid.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.synapsisid.diary.database.AppDatabase;
import com.synapsisid.diary.database.UserTable;
import com.synapsisid.diary.databinding.ActivityProfileBinding;
import android.os.Bundle;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();
        listener();

        binding.ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.this.finish();
            }
        });

    }

    private void getData(){
        int id= getSharedPreferences("my_pref", Context.MODE_PRIVATE).getInt("USER_ID",-1);
        List<UserTable> user = AppDatabase.getInstance(this).databaseDao().getProfileData(id);
        binding.tvName.setText(user.get(0).getNama());
        binding.tvDtBirth.setText(user.get(0).getTanggalLahir());
        binding.tvGender.setText(user.get(0).getGender());
        binding.tvEmail.setText(user.get(0).getEmail());
    }

    private void listener(){
        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.remove("USER_ID");
                edit.apply();

                LocalBroadcastManager.getInstance(ProfileActivity.this).sendBroadcast(new Intent("LOGOUT"));

                startActivity(new Intent(ProfileActivity.this,SignInActivity.class));
                ProfileActivity.this.finish();

            }
        });
    }
}