package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Barbershop> {

    public ListAdapter(Context context, ArrayList <Barbershop> vBarbershop) {
        super(context, R.layout.list_item, vBarbershop);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Barbershop barbershop = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.picture);
        TextView name = convertView.findViewById(R.id.tv_salon_name);
        TextView location = convertView.findViewById(R.id.tv_address);
        TextView distance = convertView.findViewById(R.id.tv_dist);

        imageView.setImageResource(barbershop.imageId);
        name.setText(barbershop.barber_name);
        distance.setText(barbershop.barber_distance);
        location.setText(barbershop.barber_address);

        return convertView;
    }
}
