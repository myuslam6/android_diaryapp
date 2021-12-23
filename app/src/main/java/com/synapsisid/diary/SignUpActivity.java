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
import com.synapsisid.diary.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Jika tombol sign in dipencet, maka akan menutup halaman signup
        binding.btnSignInReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity.this.finish();

            }
        });

        // Jika tombol signup dipencet maka akan melakukan pendaftaran akun
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validasiData() == true){
                    Toast.makeText(SignUpActivity.this,"Tolong isi data yang benar",Toast.LENGTH_LONG).show();
                }else{

                    // Buat untuk ngasih akses ke database
                    AppDatabase database = AppDatabase.getInstance(SignUpActivity.this);

                    String nama = binding.etName.getText().toString();
                    String tanggalLahir = binding.etDtbirth.getText().toString();
                    String gender = binding.etGender.getText().toString();
                    String email = binding.etEmail.getText().toString();
                    String password = binding.etPassword.getText().toString();

                    // Untuk transaksi dengan database
                    DatabaseDao dao = database.databaseDao();

                    if(dao.checkEmail(email).size() > 0){
                        Toast.makeText(SignUpActivity.this,"Email sudah digunakan",Toast.LENGTH_LONG).show();
                    }else{

                        dao.signUp(
                                new UserTable(
                                        0,
                                        nama,
                                        tanggalLahir,
                                        gender,
                                        email,
                                        password
                                )
                        );

                        // Untuk menyimpan user id ke dalam shared preference
                        saveLoginId(dao.getLastId());

                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class ));

                        SignUpActivity.this.finish();
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
        Boolean apakahKosong = false;

        if(binding.etName.getText().toString().isEmpty()){
            apakahKosong = true;
        }

        if(binding.etDtbirth.getText().toString().isEmpty()){
            apakahKosong = true;
        }

        if(binding.etGender.getText().toString().isEmpty()){
            apakahKosong = true;
        }

        if(binding.etEmail.getText().toString().isEmpty()){
            apakahKosong = true;
        }

        if(binding.etPassword.getText().toString().isEmpty()){
            apakahKosong = true;
        }

        if(binding.etPassword.getText().toString().length() < 8 ){
            apakahKosong = true;
        }

        return  apakahKosong;

    }
}