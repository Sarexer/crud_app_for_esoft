package ru.sarexer.eapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.sarexer.eapp.R;
import ru.sarexer.eapp.db.AppDatabase;
import ru.sarexer.eapp.db.DbSingleton;
import ru.sarexer.eapp.db.entity.User;

import static android.content.SharedPreferences.*;

public class LoginActivity extends AppCompatActivity {
    private Button btnEnter;
    private EditText txtLogin,txtPassword;

    private AppDatabase database;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(
                getResources().getString(R.string.app_preferences), MODE_PRIVATE);

        if(isSignedIn()){
            startMainActivity();
        }
        setContentView(R.layout.activity_login);

        database= DbSingleton.getInstance(this).database;
        generateUsers();

        initViews();
        setOnClickListeners();
    }

    private void initViews(){
        btnEnter = findViewById(R.id.btn_enter);
        txtLogin = findViewById(R.id.txt_login);
        txtPassword = findViewById(R.id.txt_password);
    }

    private void setOnClickListeners(){
        btnEnter.setOnClickListener(v -> login());
    }

    private void login(){
        String login = txtLogin.getText().toString();
        String password = txtPassword.getText().toString();

        selectUserFromDb(login, password);
    }

    private boolean isSignedIn(){
        int uid = preferences.getInt("userId", -1);
        return uid != -1;
    }

    private void startMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
    }

    private void saveUserId(int uid){
        Editor editor = preferences.edit();
        editor.putInt("userId", uid);
        editor.apply();
    }

    private void selectUserFromDb(String login, String pass){
        new AsyncTask<Void,Void,User>(){

            @Override
            protected User doInBackground(Void... voids) {
                System.out.println(database.userDao().getAll());
                User user = database.userDao().findByLogin(login, pass);
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                if(user!= null){
                    saveUserId(user.uid);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        }.execute();
    }

    private void generateUsers(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                User user1 = new User(0, "log1", "123");
                User user2 = new User(0,"log2", "123");

                database.userDao().insertAll(user1,user2);
                return null;
            }
        }.execute();
    }
}
