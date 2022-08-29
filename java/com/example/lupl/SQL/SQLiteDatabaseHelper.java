package com.example.lupl.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.lupl.Models.User;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    //Veriitabanı Versiyonu
    private static final int DATABASE_VERSION = 1;

    //Veritabanı Adı
    private static final String DATABASE_NAME = "UserManager.db";

    //User Tablosu İsmi
    private static final String TABLE_USER = "user";

    //User Tablosu Sütun İsimleri
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_XP = "user_xp";
    private static final String COLUMN_USER_HP = "user_hp";
    private static final String COLUMN_USER_COIN = "user_coin";

    //Tablo Oluşturma SQL Sorgusu
    private String CREATE_USER_TABLE =
            "CREATE TABLE "+TABLE_USER +"" +
                    "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_USER_NAME + " TEXT,"
                        + COLUMN_USER_EMAIL + " TEXT,"
                        + COLUMN_USER_PASSWORD + " TEXT,"
                        + COLUMN_USER_XP + " INT,"
                        + COLUMN_USER_HP + " INT,"
                        + COLUMN_USER_COIN + " INT" + ")";
    //Veritabanını Kapatma
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Eğer tablo varsa düşür
        db.execSQL(DROP_USER_TABLE);
        //Tekrar oluştur
        onCreate(db);
    }

    //Veritabanına Kullanıcı Ekleme
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getMail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_XP, 0);
        values.put(COLUMN_USER_HP, 50);
        values.put(COLUMN_USER_COIN, 0);

        //Verileri Veritabanına Ekleme
        db.insert(TABLE_USER,null, values);
        db.close();
    }

    //Veritabanındaki Verileri Çekme ve Listeye Atma
    public List<User> getAllUser(){
        //Sütunları bir array şekline getiriyorum
        String[] columns ={
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_XP,
                COLUMN_USER_HP,
                COLUMN_USER_COIN
        };

        //Verileri Çekme
        String sortOrder = COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER,
                columns,null,null,null,null,null,sortOrder);

        // Tüm kayıtları dönüştürüp listeye ekleme
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setMail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setXp(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_XP)));
                user.setHp(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_HP)));
                user.setCoin(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_COIN)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // Listeyi döndür
        return userList;
    }

    //Kullanıcı adı bilinen kullanıcının mail adresini getirme
    public String getEmail(String username){
        String[] columns = {
                COLUMN_USER_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String mailAddress=" ";
        String selection = COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery("SELECT "+COLUMN_USER_EMAIL+" FROM "+TABLE_USER+" WHERE "+COLUMN_USER_NAME+" = ?",selectionArgs);
        /*Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        */
        // Tüm kayıtları dönüştürüp listeye ekleme
        if (cursor.moveToFirst()) {
            do {
                mailAddress = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mailAddress;
    }

    //User güncelleme
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getMail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_XP, user.getXp());
        values.put(COLUMN_USER_HP, user.getHp());
        values.put(COLUMN_USER_COIN, user.getCoin());
        // Kaydı güncelleme
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //User Silme
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }


    //Kulanıcının usernamei var mı yok mu kontrol eder
    public boolean checkUsername(String username) {
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";
        // selection argument
        String[] selectionArgs = {username};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    //Kullanıcının e mail adresi var mı yok mu diye kontrol eder
    public boolean checkMail(String mail) {
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {mail};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    //Kullanıcının Şifresini Kontrol eder
    public boolean checkUserPassword(String password) {
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_PASSWORD + " = ?";
        // selection argument
        String[] selectionArgs = {password};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}
