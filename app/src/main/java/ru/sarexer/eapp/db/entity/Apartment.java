package ru.sarexer.eapp.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "apartment")
public class Apartment implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "photo")
    public String photo;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "area")
    public float area;

    @ColumnInfo(name = "price")
    public float price;

    @ColumnInfo(name = "number_of_rooms")
    public  int numberOfRooms;

    @ColumnInfo(name = "floor")
    public int floor;

    @Ignore
    public Apartment() {
        this.uid = 0;
    }

    public Apartment(int uid, String photo, String address, float area, float price, int numberOfRooms, int floor) {
        this.uid = uid;
        this.photo = photo;
        this.address = address;
        this.area = area;
        this.price = price;
        this.numberOfRooms = numberOfRooms;
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "uid=" + uid +
                ", photo='" + photo + '\'' +
                ", address='" + address + '\'' +
                ", area=" + area +
                ", price=" + price +
                ", numberOfRooms=" + numberOfRooms +
                ", floor=" + floor +
                '}';
    }
}
