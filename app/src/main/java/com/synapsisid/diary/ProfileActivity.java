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
import com.synapsisid.diary.database.DatabaseDao;
import com.synapsisid.diary.database.UserTable;
import com.synapsisid.diary.databinding.ActivityProfileBinding;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    private Boolean isEdit = false;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        id = getSharedPreferences("my_pref", Context.MODE_PRIVATE).getInt("USER_ID", -1);

        getData();
        listener();

        binding.ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.this.finish();
            }
        });

    }

    private void getData() {
        int id = getSharedPreferences("my_pref", Context.MODE_PRIVATE).getInt("USER_ID", -1);
        List<UserTable> user = AppDatabase.getInstance(this).databaseDao().getProfileData(id);
        binding.tvName.setText(user.get(0).getNama());
        binding.tvDtBirth.setText(user.get(0).getTanggalLahir());
        binding.tvGender.setText(user.get(0).getGender());
        binding.tvEmail.setText(user.get(0).getEmail());
        binding.tvPassword.setText(user.get(0).getPassword());
    }

    private void listener() {
        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.remove("USER_ID");
                edit.apply();

                LocalBroadcastManager.getInstance(ProfileActivity.this).sendBroadcast(new Intent("LOGOUT"));

                startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
                ProfileActivity.this.finish();

            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEdit) {
                    binding.btnEdit.setText("Save");
                    binding.tvName.setEnabled(true);
                    binding.tvGender.setEnabled(true);
                    binding.tvDtBirth.setEnabled(true);
                    binding.tvPassword.setVisibility(View.VISIBLE);
                    isEdit = true;
                } else {
                    if (validateData()) {
                        binding.btnEdit.setText("Edit");
                        binding.tvName.setEnabled(false);
                        binding.tvGender.setEnabled(false);
                        binding.tvDtBirth.setEnabled(false);
                        binding.tvPassword.setVisibility(View.GONE);

                        AppDatabase database = AppDatabase.getInstance(ProfileActivity.this);
                        DatabaseDao dao = database.databaseDao();

                        dao.updateUser(
                                new UserTable(
                                        id,
                                        binding.tvName.getText().toString(),
                                        binding.tvDtBirth.getText().toString(),
                                        binding.tvGender.getText().toString(),
                                        binding.tvEmail.getText().toString(),
                                        binding.tvPassword.getText().toString()
                                )
                        );

                        Toast.makeText(ProfileActivity.this,"Data has been changed",Toast.LENGTH_LONG).show();

                        isEdit = false;
                    }else{
                        Toast.makeText(ProfileActivity.this,"Isi data yang benar",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean validateData() {
        boolean isValidated = true;

        if (binding.tvName.getText().toString().isEmpty())
            isValidated = false;
        if (binding.tvGender.getText().toString().isEmpty())
            isValidated = false;
        if (binding.tvDtBirth.getText().toString().isEmpty())
            isValidated = false;
        if (binding.tvPassword.getText().toString().isEmpty() || binding.tvPassword.getText().toString().length()<5)
            isValidated = false;

        return isValidated;
    }
}