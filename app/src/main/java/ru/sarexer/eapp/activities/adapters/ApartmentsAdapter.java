package ru.sarexer.eapp.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.sarexer.eapp.R;
import ru.sarexer.eapp.activities.ApartmentActivity;
import ru.sarexer.eapp.db.entity.Apartment;

public class ApartmentsAdapter extends RecyclerView.Adapter<ApartmentsAdapter.ApartmentViewHolder> {
    private List<Apartment> apartments;
    private Activity activity;

    public static class ApartmentViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageViewPhoto;
        public TextView txtPrice, txtArea,txtAddress;
        public MaterialCardView cardView;
        public Context context;

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
        holder.txtPrice.setText(apartment.price+"");
        holder.txtArea.setText(apartment.area +"");
        holder.txtAddress.setText(apartment.address +"");

        holder.cardView.setOnClickListener(v -> startApartmentActivity(holder,apartment));
    }

    @Override
    public int getItemCount() {
        return apartments.size();
    }

    public void setApartments(List<Apartment> apartments) {
        this.apartments = apartments;
    }

    private void startApartmentActivity(ApartmentViewHolder holder,Apartment apartment){
        Intent intent = new Intent(holder.context, ApartmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("apartment", apartment);
        intent.putExtras(bundle);

        holder.context.startActivity(intent);
    }

    private Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
