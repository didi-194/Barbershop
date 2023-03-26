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

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    ArrayList<Barber> vBarber;
    Context context;
    onBarberListener onBarberListener;
    int globalPosition;

    public MainAdapter(ArrayList<Barber> vBarber, Context context, onBarberListener onBarberListener) {
        this.vBarber = vBarber;
        this.context = context;
        this.onBarberListener = onBarberListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view, onBarberListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Setting the variable in xml
        holder.image_barber.setImageResource(vBarber.get(position).getPic());
        holder.tv_barber_name.setText(vBarber.get(position).getName());

        if (position == globalPosition){
            holder.cv_barber.setCardBackgroundColor(Color.parseColor("#3BC9FF"));
        }
        else {
            holder.cv_barber.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public int getItemCount() {
        return vBarber.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image_barber;
        TextView tv_barber_name;
        CardView cv_barber;
        onBarberListener onBarberListener;

        public ViewHolder(@NonNull View itemView, onBarberListener onBarberListener) {
            super(itemView);
            image_barber = itemView.findViewById(R.id.image_barber);
            tv_barber_name = itemView.findViewById(R.id.tv_barber_name);
            cv_barber = itemView.findViewById(R.id.cv_barber);
            this.onBarberListener = onBarberListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onBarberListener.onBarberClick(getAdapterPosition());
            globalPosition = getAdapterPosition();

//            notifyItemRangeChanged(0, 7);
            notifyDataSetChanged();
//            System.out.println(cv_barber.getCardBackgroundColor().getDefaultColor());

//            if (cv_barber.getCardBackgroundColor().getDefaultColor() == -16777216){
//                cv_barber.setCardBackgroundColor(Color.parseColor("#ffffff"));
//            }
//            else {
//                cv_barber.setCardBackgroundColor(Color.parseColor("#000000"));
//            }

//            cv_barber.setCardBackgroundColor(Color.parseColor("#3BC9FF"));
        }
    }

}
