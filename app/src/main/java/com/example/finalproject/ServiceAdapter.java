package com.example.finalproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    ArrayList<Service> vService;
    Context context;
    onServiceListener onServiceListener;

    public ServiceAdapter(ArrayList<Service> vService, Context context, onServiceListener onServiceListener) {
        this.vService = vService;
        this.context = context;
        this.onServiceListener = onServiceListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_service, parent, false);
        return new ViewHolder(view, onServiceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image_service.setImageResource(vService.get(position).getPic());
        holder.tv_service_name.setText(vService.get(position).getName());
        holder.tv_service_price.setText(vService.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return vService.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image_service;
        TextView tv_service_name, tv_service_price;
        CardView cv_service;
        onServiceListener onServiceListener;

        public ViewHolder(@NonNull View itemView, onServiceListener onServiceListener) {
            super(itemView);
            image_service = itemView.findViewById(R.id.image_barber);
            tv_service_name = itemView.findViewById(R.id.tv_barber_name);
            tv_service_price = itemView.findViewById(R.id.tv_service_price);
            cv_service = itemView.findViewById(R.id.cv_barber);
            this.onServiceListener = onServiceListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onServiceListener.onServiceClick(getAdapterPosition());

//            System.out.println(cv_service.getCardBackgroundColor().getDefaultColor());

            if (cv_service.getCardBackgroundColor().getDefaultColor() == -12858881){
                cv_service.setCardBackgroundColor(Color.parseColor("#ffffff"));
            }
            else {
                cv_service.setCardBackgroundColor(Color.parseColor("#3BC9FF"));
            }

//            cv_service.setCardBackgroundColor(Color.parseColor("#3BC9FF"));
        }
    }
}
