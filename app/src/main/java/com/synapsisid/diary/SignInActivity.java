package com.synapsisid.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.synapsisid.diary.database.AppDatabase;
import com.synapsisid.diary.database.UserTable;
import com.synapsisid.diary.databinding.ActivitySignInBinding;

import java.util.List;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        int savedId = sharedPreferences.getInt("USER_ID",-1);
        Log.w("WEDEBUG",Integer.toString(savedId));

        if(savedId > 0){

            startActivity(new Intent(SignInActivity.this,HomeActivity.class));
            this.finish();
        }

        binding.btnGotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
                SignInActivity.this.finish();
            }
        });

        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validasiData() == true){
                    Toast.makeText(SignInActivity.this, "Isi data yang benar", Toast.LENGTH_LONG).show();
                }else{

                    String email = binding.etEmail.getText().toString();
                    String pass = binding.etPassword.getText().toString();

                    List<UserTable> hasil = AppDatabase.getInstance(SignInActivity.this).databaseDao().signIn(
                            email,pass
                    );

                    if(hasil.size() > 0){
                        AppDatabase.getInstance(SignInActivity.this).databaseDao().getLastId();
                        saveLoginId(hasil.get(0).getId());
                        Log.w("WEDEBUG",Integer.toString(savedId));
                        startActivity(new Intent(SignInActivity.this, HomeActivity.class ));
                    }else{
                        Toast.makeText(SignInActivity.this, "Email atau password salah",Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

    }

    private void saveLoginId(int userId){
        SharedPreferences sharedPreferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("USER_ID",userId);

        editor.apply();
    }

    private boolean validasiData(){
        boolean apakahKosong = false;

        if(binding.etEmail.getText().toString().isEmpty()){
            apakahKosong = true;
        }

        if(binding.etPassword.getText().toString().isEmpty()){
            apakahKosong = true;
        }

        return apakahKosong;
    }

}