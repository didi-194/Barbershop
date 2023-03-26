package com.example.finalproject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ConfirmationDialog extends AppCompatDialogFragment {

    DatabaseReference db;
    FirebaseAuth mAuth;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        db = FirebaseDatabase
                .getInstance("https://finalproject-38e5c-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Order");

        mAuth = FirebaseAuth.getInstance();

        String salon_name, barber_name, time_book, date_book, service_selected, price;
        int imageId, price_int;

        salon_name = getArguments().getString("salon_name");
        barber_name = getArguments().getString("barber_name");
        time_book = getArguments().getString("time_book");
        date_book = getArguments().getString("date_book");
        service_selected = getArguments().getString("service_selected");
        imageId = getArguments().getInt("imageId", 0);
        price_int = getArguments().getInt("price", 0);
        price = "" + price_int;

        Order order = new Order(salon_name, barber_name, time_book, date_book, service_selected, imageId, price);


        String message = String.format("Salon name : %s" +
                "\nBooking date : %s" +
                "\nBooking time : %s" +
                "\nBarber name : %s" +
                "\nService selected : %s"+
                "\nPrice : %s", salon_name, date_book, time_book, barber_name, service_selected, price);

        builder.setTitle("Booking Confirmation")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Booking has been placed", Toast.LENGTH_SHORT).show();

                        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        String id1 = db.push().getKey();
                        db.child(id).child(id1).setValue(order);

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.putExtra("status", "yes");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "Booking has been canceled", Toast.LENGTH_SHORT).show();
                //Reload Activity
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });
        return builder.create();
    }
}
