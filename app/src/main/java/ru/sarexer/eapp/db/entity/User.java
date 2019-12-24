package ru.sarexer.eapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "login")
    public String login;

    @ColumnInfo(name = "password")
    public String password;

    public User(int uid, String login, String password) {
        this.uid = uid;
        this.login = login;
        this.password = password;
    }
}
