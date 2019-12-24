package ru.sarexer.eapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ru.sarexer.eapp.R;
import ru.sarexer.eapp.activities.adapters.ApartmentsAdapter;
import ru.sarexer.eapp.db.AppDatabase;
import ru.sarexer.eapp.db.DbSingleton;
import ru.sarexer.eapp.db.entity.Apartment;

import static android.content.SharedPreferences.*;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private RecyclerView recyclerView;
    private List<Apartment> apartments;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartments);

        preferences = getSharedPreferences(getResources().getString(R.string.app_preferences),
                MODE_PRIVATE);

        database = DbSingleton.getInstance(this).database;

        Toolbar actionBarToolbar = findViewById(R.id.apartments_toolbar);
        setSupportActionBar(actionBarToolbar);

        initRecyclerView();
        loadApartments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_apartments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                startActivity(new Intent(this, ApartmentActivity.class));
                break;
            case R.id.action_exit:
                forgetUser();
                finish();
                startLoginActivity();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadApartments();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apartments = new ArrayList<>();
        recyclerView.setAdapter(new ApartmentsAdapter(apartments));

    }

    private void updateRecyclerView() {
        ApartmentsAdapter adapter = (ApartmentsAdapter) recyclerView.getAdapter();
        adapter.setApartments(apartments);
        recyclerView.getAdapter().notifyDataSetChanged();
        System.out.println("");
    }

    private void startLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private void forgetUser() {
        Editor editor = preferences.edit();
        editor.remove("userId");
        editor.apply();
    }

    private void loadApartments() {
        new AsyncTask<Void, Void, List<Apartment>>() {

            @Override
            protected List<Apartment> doInBackground(Void... voids) {
                apartments = database.apartmentDao().getAll();
                return null;
            }

            @Override
            protected void onPostExecute(List<Apartment> apartments) {
                super.onPostExecute(apartments);
                updateRecyclerView();
            }
        }.execute();
    }
}
