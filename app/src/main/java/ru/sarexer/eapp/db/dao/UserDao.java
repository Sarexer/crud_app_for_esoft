package ru.sarexer.eapp.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ru.sarexer.eapp.db.entity.User;


@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE uid = :uid")
    User loadUserById(int uid);


    @Query("SELECT * FROM user WHERE login = :login and password = :pass" )
    User findByLogin(String login, String pass);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}
