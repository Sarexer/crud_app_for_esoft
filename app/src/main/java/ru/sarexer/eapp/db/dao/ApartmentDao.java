package ru.sarexer.eapp.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import ru.sarexer.eapp.db.entity.Apartment;


@Dao
public interface ApartmentDao {
    @Query("SELECT * FROM apartment")
    List<Apartment> getAll();

    @Query("SELECT * FROM apartment WHERE uid IN (:apartmentIds)")
    List<Apartment> loadAllByIds(int[] apartmentIds);

    @Insert
    void insertAll(Apartment... users);

    @Update
    void updateApartment(Apartment apartment);

    @Delete
    void delete(Apartment user);
}
