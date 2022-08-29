package com.example.lupl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.lupl.Helpers.InputValidation;
import com.example.lupl.SQL.SQLiteDatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
//sayfammız View.onClickListener dan implemente edilir
public class Login extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = Login.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextUsername;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView appCompatLinkRegister;

    private InputValidation inputValidation;

    private SQLiteDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //4-)use initialize methods
        initViews();
        initListeners();
        initObjects();

        appCompatLinkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createAcc = new Intent(Login.this,MainActivity.class);
                startActivity(createAcc);
            }
        });
    }
    //7-) Bu implemente edilmiş metod hangi viewa tıkladığımızı anlar
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                break;
        }
    }

    //1-)initializing views - viewlar ile java kodunu eşleştiriyoruz
    private void initViews(){
        nestedScrollView = findViewById(R.id.nestedScrollView);

        textInputEditTextUsername = findViewById(R.id.textInputEditTextUsername);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);

        textInputLayoutUsername = findViewById(R.id.textInputLayoutUsername);
        textInputLayoutPassword =  findViewById(R.id.textInputLayoutPassword);

        appCompatButtonLogin = findViewById(R.id.appCompatButtonLogin);
        appCompatLinkRegister = findViewById(R.id.textViewLinkRegister);
    }

    //2-)initializing listeners
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        appCompatLinkRegister.setOnClickListener(this);
    }

    //3-)initializing objects
    private void initObjects() {
        databaseHelper = new SQLiteDatabaseHelper(Login.this);
        inputValidation = new InputValidation(Login.this);
    }

    //5-)Bu metod, giriş verilerinini doğrulamak ve SQLite'tan oturum açma için gerekli kimlik bilgilerini doğrulamak içindir.
    private void verifyFromSQLite(){
        //Username verisi girilmişse devam et
        if(!inputValidation.isInputEditTextFilled(textInputEditTextUsername,textInputLayoutUsername,getString(R.string.error_message_username))){
            return;
        }
        //Şifre verisi girilmişse devam et
        if(!inputValidation.isInputEditTextFilled(textInputEditTextPassword,textInputLayoutPassword,getString(R.string.error_message_password))){
            return;
        }
        //Kullanıcı veri tabanında varsa  ve şifresi doğruysa giriş yap
        if(databaseHelper.checkUsername(textInputEditTextUsername.getText().toString().trim())){
            if(databaseHelper.checkUserPassword(textInputEditTextPassword.getText().toString().trim())){
                Intent homePageIntent = new Intent(Login.this,HomePage.class);
                homePageIntent.putExtra("USERNAME",textInputEditTextUsername.getText().toString().trim());
                emptyEditTexts();
                startActivity(homePageIntent);
            }
            else{
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                Snackbar.make(nestedScrollView, getString(R.string.error_valid_username_password), Snackbar.LENGTH_LONG).show();
            }
        }else{
            // Snack Bar to show success message that record is wrong
            try {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_username_password), Snackbar.LENGTH_LONG).show();
        }
    }
    //6-) EditTextledeki verileri silen metod
    private void emptyEditTexts(){
        textInputEditTextUsername.setText(null);
        textInputEditTextPassword.setText(null);
    }



}