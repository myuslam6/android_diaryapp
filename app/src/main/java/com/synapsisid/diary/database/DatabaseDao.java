package com.synapsisid.diary.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DatabaseDao {

    @Insert
    Long signUp(UserTable data);

    @Query("SELECT * FROM usertable WHERE id = :id AND password = :inputPassword")
    List<UserTable> openPrivate(int id,String inputPassword);

    @Query("SELECT * FROM usertable WHERE email = :inputEmail AND password = :inputPassword")
    List<UserTable> signIn(String inputEmail,String inputPassword);

    @Query("SELECT * FROM usertable WHERE email = :inputEmail")
    List<UserTable> checkEmail(String inputEmail);

    @Query("SELECT MAX(id) FROM usertable")
    int getLastId();

    @Insert
    Long saveDiary(DiaryTable data);

    @Update
    int updateDiary(DiaryTable data);

    @Query("SELECT * FROM diarytable WHERE id = :inputId")
    DiaryTable getDiary(int inputId);

    @Query("DELETE FROM diarytable WHERE id = :inputId")
    int deleteDiary(int inputId);

    @Query("SELECT * FROM diarytable WHERE isPrivate = 0 ORDER BY lastUpdate DESC")
    List<DiaryTable> getListPublicDiary();

    @Query("SELECT * FROM diarytable WHERE isPrivate = 1 ORDER BY lastUpdate DESC")
    List<DiaryTable> getListPrivateDiary();



}
