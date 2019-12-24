package ru.sarexer.eapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
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

public class ApartmentInfoActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    private ImageView imageViewPhoto;
    private TextInputEditText txtAddress, txtArea, txtPrice, txtRooms, txtFloor,txtPricePerMeter;
    private Apartment apartment = new Apartment();
    private AppDatabase database;
    Toolbar toolBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actviity_apartment_info);

        Bundle bundle = getIntent().getExtras();
        apartment = (Apartment) bundle.getSerializable("apartment");

        database = DbSingleton.getInstance(this).database;

        toolBar = findViewById(R.id.apartment_toolbar);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_white_black_24dp);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(v -> onBackPressed());


        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadApartment();
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
                Intent intent = new Intent(this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("apartment", apartment);
                intent.putExtras(bundle);
                startActivity(intent);
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
        txtPricePerMeter = findViewById(R.id.txt_price_per_meter);
    }


    private void fillViews() {
            Picasso.get().load(Uri.parse(apartment.photo)).centerCrop().fit()
                    .into(imageViewPhoto);
            txtAddress.setText(apartment.address);
            txtArea.setText(Float.toString(apartment.area));
            txtPrice.setText(Float.toString(apartment.price));
            txtRooms.setText(Integer.toString(apartment.numberOfRooms));
            txtFloor.setText(Integer.toString(apartment.floor));
            txtPricePerMeter.setText((apartment.price/apartment.area)+"");
    }

    private void loadApartment(){
        new AsyncTask<Void,Void,Apartment>(){

            @Override
            protected Apartment doInBackground(Void... voids) {
                Apartment loadedApartment = database.apartmentDao().findById(apartment.uid);
                return loadedApartment;
            }

            @Override
            protected void onPostExecute(Apartment newApartment) {
                super.onPostExecute(apartment);
                apartment = newApartment;
                fillViews();
            }
        }.execute();
    }


}
