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

public class OrderAdapter extends ArrayAdapter<Order> {

    public OrderAdapter(Context context, ArrayList<Order> vOrder) {
        super(context, R.layout.order_item, vOrder);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Order order = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item, parent, false);
        }

        ImageView salon_pic = convertView.findViewById(R.id.picture);
        TextView tv_salon_name = convertView.findViewById(R.id.tv_salon_name);
        TextView tv_date = convertView.findViewById(R.id.tv_date);
        TextView tv_time = convertView.findViewById(R.id.tv_time);
        TextView tv_price = convertView.findViewById(R.id.tv_price);

        salon_pic.setImageResource(order.imageId);
        tv_salon_name.setText(order.salon_name);
        tv_date.setText(order.date_book);
        tv_time.setText(order.time_book);
        tv_price.setText(String.format("Price : %s", order.price));

        return convertView;
    }
}
