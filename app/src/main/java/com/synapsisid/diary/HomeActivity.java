package com.synapsisid.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.synapsisid.diary.database.AppDatabase;
import com.synapsisid.diary.database.DiaryTable;
import com.synapsisid.diary.database.UserTable;
import com.synapsisid.diary.databinding.ActivityHomeBinding;
import com.synapsisid.diary.databinding.PasswordDialogBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    DiaryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareRc();
        listener();
        getData();

        LocalBroadcastManager.getInstance(this).registerReceiver(bc, new IntentFilter("LOGOUT"));

    }

    private void showPassDialog(){
        Dialog dl = new Dialog(this);
        PasswordDialogBinding bindingDl = PasswordDialogBinding.inflate(LayoutInflater.from(this));
        dl.setContentView(bindingDl.getRoot());

        bindingDl.btnOpenPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!bindingDl.etPassPrivate.getText().toString().isEmpty()) {

                    SharedPreferences sharedPreferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
                    int id = sharedPreferences.getInt("USER_ID",-1);

                    List<UserTable> data = AppDatabase.getInstance(HomeActivity.this).databaseDao().openPrivate(id,bindingDl.etPassPrivate.getText().toString());
                    if(data.size()>0){
                        dl.dismiss();
                        startActivity(new Intent(HomeActivity.this,PrivateActivity.class));
                    }else{
                        Toast.makeText(HomeActivity.this,"Password salah",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        dl.show();
    }

    private void getData(){
        List<DiaryTable> listDiary = AppDatabase.getInstance(this).databaseDao().getListPublicDiary();
        adapter.setNewData(listDiary);

        int id= getSharedPreferences("my_pref", Context.MODE_PRIVATE).getInt("USER_ID",-1);
        List<UserTable> user = AppDatabase.getInstance(this).databaseDao().getProfileData(id);
        binding.tvNameHome.setText(user.get(0).getNama());
    }

    private void prepareRc(){
        adapter = new DiaryListAdapter(this);
        binding.rcDiary.setLayoutManager(new LinearLayoutManager(this));
        binding.rcDiary.setAdapter(adapter);
    }

    private void listener(){
        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
            }
        });

        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                binding.swipeLayout.setRefreshing(false);
            }
        });

        binding.btnPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPassDialog();
            }
        });

        binding.btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,DiaryActivity.class));
            }
        });
    }

    BroadcastReceiver bc = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HomeActivity.this.finish();
        }
    };
}