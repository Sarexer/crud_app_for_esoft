package ru.sarexer.eapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.File;

import ru.sarexer.eapp.R;
import ru.sarexer.eapp.db.AppDatabase;
import ru.sarexer.eapp.db.DbSingleton;
import ru.sarexer.eapp.db.entity.Apartment;

public class CreateActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    private ImageView imageViewPhoto;
    private TextInputEditText txtAddress, txtArea, txtPrice, txtRooms, txtFloor;
    private MaterialButton btnSave;
    private Apartment apartment = new Apartment();
    private AppDatabase database;
    Toolbar toolBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        database = DbSingleton.getInstance(this).database;

        toolBar = findViewById(R.id.apartment_toolbar);
        setSupportActionBar(toolBar);

        initView();
        setOnClickListeners();
    }

    private void initView() {
        imageViewPhoto = findViewById(R.id.imageview_photo);
        txtAddress = findViewById(R.id.txt_address);
        txtArea = findViewById(R.id.txt_area);
        txtPrice = findViewById(R.id.txt_price);
        txtRooms = findViewById(R.id.txt_rooms);
        txtFloor = findViewById(R.id.txt_floor);
        btnSave = findViewById(R.id.btn_save);
    }

    private void setOnClickListeners() {
        imageViewPhoto.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });

        btnSave.setOnClickListener(v -> saveApartment());
    }

    private boolean isValidate(){
        String address = txtAddress.getText().toString();
        String area = (txtArea.getText().toString());
        String price = (txtPrice.getText().toString());
        String rooms = (txtRooms.getText().toString());
        String floor = (txtFloor.getText().toString());

        if(floor.equals("") || area.equals("") || apartment.photo == null
                || address.equals("") || price.equals("") || rooms.equals("")){
            showValidateMessage();
            return false;
        }
        return true;
    }

    private void showValidateMessage(){
        Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
    }
    private void saveApartment() {
        if(!isValidate()){
            return;
        }
        try {
            apartment.address = txtAddress.getText().toString();
            apartment.area = Float.parseFloat(txtArea.getText().toString());
            apartment.price = Float.parseFloat(txtPrice.getText().toString());
            apartment.numberOfRooms = Integer.parseInt(txtRooms.getText().toString());
            apartment.floor = Integer.parseInt(txtFloor.getText().toString());

            insertApartmentIntoDb();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startApartmentInfoActvity(){
        Intent intent = new Intent(this, ApartmentInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("apartment", apartment);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void insertApartmentIntoDb() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.apartmentDao().insertAll(apartment);
                apartment = database.apartmentDao().lastApartment();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startApartmentInfoActvity();
            }
        }.execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri uri = data.getData();
                apartment.photo = uri.toString();

                Picasso.get().load(Uri.parse(apartment.photo)).centerCrop().fit()
                        .into(imageViewPhoto);
            }

        }
    }
}
