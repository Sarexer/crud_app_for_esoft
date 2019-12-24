package ru.sarexer.eapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.sarexer.eapp.db.dao.ApartmentDao;
import ru.sarexer.eapp.db.dao.UserDao;
import ru.sarexer.eapp.db.entity.Apartment;
import ru.sarexer.eapp.db.entity.User;


@Database(entities = {User.class, Apartment.class},  version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ApartmentDao apartmentDao();

}
