package com.synapsisid.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.synapsisid.diary.database.AppDatabase;
import com.synapsisid.diary.database.DiaryTable;
import com.synapsisid.diary.databinding.ActivityHomeBinding;
import com.synapsisid.diary.databinding.ActivityPrivateBinding;

import java.util.List;

public class PrivateActivity extends AppCompatActivity {

    private ActivityPrivateBinding binding;
    DiaryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareRc();
        getData();

        binding.toolbarPrivate.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrivateActivity.this.finish();
            }
        });
    }

    private void getData(){
        List<DiaryTable> listDiary = AppDatabase.getInstance(this).databaseDao().getListPrivateDiary();
        adapter.setNewData(listDiary);
    }

    private void prepareRc(){
        adapter = new DiaryListAdapter(this);
        binding.rcDiary.setLayoutManager(new LinearLayoutManager(this));
        binding.rcDiary.setAdapter(adapter);
    }
}