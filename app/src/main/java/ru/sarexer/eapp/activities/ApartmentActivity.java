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

public class ApartmentActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    private ImageView imageViewPhoto;
    private TextInputEditText txtAddress, txtArea, txtPrice, txtRooms, txtFloor;
    private MaterialButton btnSave;
    private Apartment apartment;
    private AppDatabase database;
    Toolbar toolBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            apartment = (Apartment) bundle.getSerializable("apartment");
        }

        database = DbSingleton.getInstance(this).database;

        toolBar = findViewById(R.id.apartment_toolbar);
        setSupportActionBar(toolBar);

        initView();
        fillViews();
        setOnClickListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_apartment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                setEnableView(true);
                setToolBarTitle(R.string.apartment_toolbar_edit_title);
                break;
        }
        return true;
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

    private void fillViews() {
        if (apartment != null) {
            Picasso.get().load(Uri.parse(apartment.photo)).centerCrop().fit()
                    .into(imageViewPhoto);
            txtAddress.setText(apartment.address);
            txtArea.setText(Float.toString(apartment.area));
            txtPrice.setText(Float.toString(apartment.price));
            txtRooms.setText(Integer.toString(apartment.numberOfRooms));
            txtFloor.setText(Integer.toString(apartment.floor));
            setEnableView(false);

            btnSave.setOnClickListener(v -> saveApartment("edit"));
        } else {
            apartment = new Apartment();
            btnSave.setOnClickListener(v -> saveApartment("create"));
        }
    }

    private void setEnableView(boolean bool) {
        imageViewPhoto.setEnabled(bool);
        txtAddress.setEnabled(bool);
        txtArea.setEnabled(bool);
        txtPrice.setEnabled(bool);
        txtRooms.setEnabled(bool);
        txtFloor.setEnabled(bool);
        btnSave.setVisibility(bool ? View.VISIBLE : View.GONE);
    }

    private void setToolBarTitle(int resourceId) {
        String title = getResources().getString(resourceId);
        toolBar.setTitle(title);
    }

    private void setOnClickListeners() {
        imageViewPhoto.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });
    }

    private void saveApartment(String mode) {
        try {
            apartment.address = txtAddress.getText().toString();
            apartment.area = Float.parseFloat(txtArea.getText().toString());
            apartment.price = Float.parseFloat(txtPrice.getText().toString());
            apartment.numberOfRooms = Integer.parseInt(txtRooms.getText().toString());
            apartment.floor = Integer.parseInt(txtFloor.getText().toString());

            if (mode.equals("create")) {
                insertApartmentIntoDb();
                finish();
            } else if (mode.equals("edit")) {
                updateApartmentInDb();
                setEnableView(false);
            }

            setToolBarTitle(R.string.apartment_toolbar_info_title);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void insertApartmentIntoDb() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.apartmentDao().insertAll(apartment);
                System.out.println(database.apartmentDao().getAll());
                return null;
            }
        }.execute();
    }

    private void updateApartmentInDb() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.apartmentDao().updateApartment(apartment);
                return null;
            }
        }.execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                //Get ImageURi and load with help of picasso
                //Uri selectedImageURI = data.getData();
                Uri uri = data.getData();
                apartment.photo = uri.toString();
                Picasso.get().load(Uri.parse(apartment.photo)).centerCrop().fit()
                        .into(imageViewPhoto);
            }

        }
    }
}
