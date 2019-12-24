package ru.sarexer.eapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import ru.sarexer.eapp.R;
import ru.sarexer.eapp.db.AppDatabase;
import ru.sarexer.eapp.db.DbSingleton;
import ru.sarexer.eapp.db.entity.Apartment;

public class EditActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_edit);

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

    private void initView() {
        imageViewPhoto = findViewById(R.id.imageview_edit_photo);
        txtAddress = findViewById(R.id.txt_edit_address);
        txtArea = findViewById(R.id.txt_edit_area);
        txtPrice = findViewById(R.id.txt_edit_price);
        txtRooms = findViewById(R.id.txt_edit_rooms);
        txtFloor = findViewById(R.id.txt_edit_floor);
        btnSave = findViewById(R.id.btn_save_edit);
    }

    private void fillViews() {
        Picasso.get().load(Uri.parse(apartment.photo)).centerCrop().fit()
                .into(imageViewPhoto);
        txtAddress.setText(apartment.address);
        txtArea.setText(Float.toString(apartment.area));
        txtPrice.setText(Float.toString(apartment.price));
        txtRooms.setText(Integer.toString(apartment.numberOfRooms));
        txtFloor.setText(Integer.toString(apartment.floor));
    }

    private void setOnClickListeners() {
        imageViewPhoto.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });

        btnSave.setOnClickListener(v -> updateApartment());
    }

    private void updateApartment() {
        try {
            apartment.address = txtAddress.getText().toString();
            apartment.area = Float.parseFloat(txtArea.getText().toString());
            apartment.price = Float.parseFloat(txtPrice.getText().toString());
            apartment.numberOfRooms = Integer.parseInt(txtRooms.getText().toString());
            apartment.floor = Integer.parseInt(txtFloor.getText().toString());

            updateApartmentInDb();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateApartmentInDb() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.apartmentDao().updateApartment(apartment);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
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
