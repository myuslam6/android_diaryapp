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
import com.synapsisid.diary.database.DatabaseDao;
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

        // jika id yg di save itu ada, maka akan langsung diteruskan ke ActivityHome
        if(savedId > 0){
            startActivity(new Intent(SignInActivity.this,HomeActivity.class));
            this.finish();
        }

        // Jika tombol signup di pecnet, maka akan membuka halaman SignUp
        binding.btnGotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });

        // Jika tombol Signin dipencet, maka akan melakukan sign in akun yg sudah ada
        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validasiData() == true){
                    Toast.makeText(SignInActivity.this, "Isi data yang benar", Toast.LENGTH_LONG).show();
                }else{

                    String email = binding.etEmail.getText().toString();
                    String pass = binding.etPassword.getText().toString();

                    AppDatabase databse = AppDatabase.getInstance(SignInActivity.this);

                    DatabaseDao dao = databse.databaseDao();

                    List<UserTable> hasil = dao.signIn(
                            email,pass
                    );

                    if(hasil.size() > 0){
                        AppDatabase.getInstance(SignInActivity.this).databaseDao().getLastId();
                        //save ke dalam shared preference
                        saveLoginId(hasil.get(0).getId());

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