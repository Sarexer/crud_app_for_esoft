package ru.sarexer.eapp.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import ru.sarexer.eapp.R;
import ru.sarexer.eapp.activities.ApartmentInfoActivity;
import ru.sarexer.eapp.activities.CreateActivity;
import ru.sarexer.eapp.db.DbSingleton;
import ru.sarexer.eapp.db.entity.Apartment;

import static android.content.DialogInterface.*;

public class ApartmentsAdapter extends RecyclerView.Adapter<ApartmentsAdapter.ApartmentViewHolder> {
    private List<Apartment> apartments;
    private static Context context;

    public static class ApartmentViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewPhoto;
        private TextView txtPrice, txtArea,txtAddress;
        private MaterialCardView cardView;

        public ApartmentViewHolder(@NonNull View itemView) {
            super(itemView);
             context = itemView.getContext();
            imageViewPhoto = itemView.findViewById(R.id.item_photo);
            txtPrice = itemView.findViewById(R.id.item_price);
            txtArea = itemView.findViewById(R.id.item_area);
            txtAddress = itemView.findViewById(R.id.item_address);
            cardView = itemView.findViewById(R.id.item_card);
        }
    }

    public ApartmentsAdapter(List<Apartment> apartments) {
        this.apartments = apartments;
    }

    @NonNull
    @Override
    public ApartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apartment_item,parent, false);
        return new ApartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApartmentViewHolder holder, int position) {
        Apartment apartment = apartments.get(position);

        Picasso.get().load(Uri.parse(apartment.photo)).centerCrop().fit().into(holder.imageViewPhoto);
        holder.txtPrice.setText(apartment.price + " руб.");
        holder.txtArea.setText(apartment.area + " кв.м");
        holder.txtAddress.setText(apartment.address +"");

        holder.cardView.setOnClickListener(v -> startApartmentInfoActivity(holder,apartment));
        holder.cardView.setOnLongClickListener(v -> showAlertDialog(position));
    }

    @Override
    public int getItemCount() {
        return apartments.size();
    }

    public void setApartments(List<Apartment> apartments) {
        this.apartments = apartments;
    }

    private void startApartmentInfoActivity(ApartmentViewHolder holder, Apartment apartment){
        Intent intent = new Intent(context, ApartmentInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("apartment", apartment);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    private void deleteApartment(int position){
        Apartment apartment = apartments.get(position);
        apartments.remove(apartment);
        deleteApartmentFromDb(context,apartment);
        notifyItemRemoved(position);
    }

    private boolean showAlertDialog(int position){
        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setTitle("Удалить квартиру?")
                .setPositiveButton("Да", createBtnOkListener(position))
                .setNegativeButton("Отмена", (dialog1, which) -> dialog1.dismiss())
                .show();
        return true;
    }

    private OnClickListener createBtnOkListener(int position){
        return (dialog, which) -> deleteApartment(position);
    }

    private void deleteApartmentFromDb(Context context,Apartment apartment){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                DbSingleton.getInstance(context).database.apartmentDao().delete(apartment);
                return null;
            }
        }.execute();

    }

}
