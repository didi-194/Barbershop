 package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

 public class OrderActivity extends AppCompatActivity implements onBarberListener, onServiceListener{

    TextView tv_salon_name, tv_price;
    Button btn_book;
    int imageId;

    //RecyclerView
    RecyclerView rv_barber, rv_service;
    ArrayList<Barber> vBarber;
    ArrayList<Service> vService;
    MainAdapter mainAdapter;
    ServiceAdapter serviceAdapter;
    String[] barberName, serviceName;
    int[] priceList;
    int price;
    String selected_barber, salon_name;
    List<String> selected_service;

    //Time Picker
    EditText et_date_book, et_time_book;
    int t1Hour, t1Minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        salon_name = intent.getStringExtra("name");
        imageId = intent.getIntExtra("imageId", 0);

        tv_salon_name = findViewById(R.id.tv_salon_name);
        tv_salon_name.setText(salon_name);
        btn_book = findViewById(R.id.btn_book);

        //Date and Time Picker
        et_time_book = findViewById(R.id.et_time_book);
        et_date_book = findViewById(R.id.et_date_book);

        //RecyclerView
        rv_barber = findViewById(R.id.rv_barber);
        rv_service = findViewById(R.id.rv_service);

        //Barber
        int[] barberPic = {R.drawable.jacky, R.drawable.john, R.drawable.ryan, R.drawable.martin, R.drawable.ben, R.drawable.stephen, R.drawable.edwin};
        barberName = new String[]{"Jack", "John", "Ryan", "Martin", "Ben", "Stephen", "Edwin"};
        vBarber = new ArrayList<>();

        selected_barber = barberName[0];

        //Service
        int[] servicePic = {R.drawable.haircut, R.drawable.shaving, R.drawable.hair_spa, R.drawable.smooth, R.drawable.curl};
        serviceName = new String[]{"Haircut", "Shave", "Hair Spa", "Smoothing", "Curling"};
        vService = new ArrayList<>();

        priceList = new int[]{50000, 25000, 60000, 150000, 150000};

        selected_service = new ArrayList<>();


        for (int i = 0; i < barberName.length; i++){
            vBarber.add(new Barber(barberPic[i], barberName[i]));
        }

        for (int i = 0; i < serviceName.length; i++){
            vService.add(new Service(servicePic[i], serviceName[i], ""+priceList[i]));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_barber.setLayoutManager(layoutManager);
        rv_service.setLayoutManager(layoutManager1);
        rv_barber.setItemAnimator(new DefaultItemAnimator());
        rv_service.setItemAnimator(new DefaultItemAnimator());

        mainAdapter = new MainAdapter(vBarber, this, this);
        rv_barber.setAdapter(mainAdapter);

        serviceAdapter = new ServiceAdapter(vService, this, this);
        rv_service.setAdapter(serviceAdapter);



        //Disable Keyboard
        et_time_book.setKeyListener(null);
        et_date_book.setKeyListener(null);

        //Setting Time Dialog on Focus
        et_time_book.setOnFocusChangeListener((view, b) -> {
            if (b){
                TimePickerDialog timePickerDialog = new TimePickerDialog(OrderActivity.this, (TimePickerDialog.OnTimeSetListener) (timePicker, i, i1) -> {
                    t1Hour = i;
                    t1Minute = i1;
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(0,0,0,t1Hour,t1Minute);
                    et_time_book.setText(DateFormat.format("hh:mm aa", calendar));

                }, 12, 0, false);

                timePickerDialog.updateTime(t1Hour, t1Minute);
                timePickerDialog.show();
            }
        });

        //Setting Time Dialog on Click
        et_time_book.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (TimePickerDialog.OnTimeSetListener) (timePicker, i, i1) -> {
                t1Hour = i;
                t1Minute = i1;
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,t1Hour,t1Minute);
                et_time_book.setText(DateFormat.format("hh:mm aa", calendar));

            }, 12, 0, false);

            timePickerDialog.updateTime(t1Hour, t1Minute);
            timePickerDialog.show();
        });


        //Date Picker
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Setting Date Dialog on Focus
        et_date_book.setOnFocusChangeListener((view, b) -> {
            if (b){
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        et_date_book.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //Setting Date Dialog on Click
        et_date_book.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month+1;
                    String date = day+"/"+month+"/"+year;
                    et_date_book.setText(date);
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        btn_book.setOnClickListener(view -> {
            String barber_name, time_book, date_book, service_selected;

            if (et_time_book.getText().toString().isEmpty() || et_date_book.getText().toString().isEmpty()){
                Toast.makeText(this, "Tidak boleh ada yang kosong !", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selected_service.isEmpty()){
                Toast.makeText(this, "Harus memilih service", Toast.LENGTH_SHORT).show();
                return;
            }

            barber_name = selected_barber;
            time_book = et_time_book.getText().toString();
            date_book = et_date_book.getText().toString();
            service_selected = selected_service.toString();

            //Date validation
            Date d = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String currDate = df.format(d);
            String[] arrCurrDate = currDate.split("/");
            String[] chosenDate = date_book.split("/");

            if (Integer.parseInt(arrCurrDate[2]) > Integer.parseInt(chosenDate[2])) {
                Toast.makeText(this, "Cannot make reservation", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Integer.parseInt(arrCurrDate[2]) == Integer.parseInt(chosenDate[2]) &&
                    Integer.parseInt(arrCurrDate[1]) > Integer.parseInt(chosenDate[1])){
                Toast.makeText(this, "Cannot make reservation", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Integer.parseInt(arrCurrDate[2]) == Integer.parseInt(chosenDate[2]) &&
                    Integer.parseInt(arrCurrDate[1]) == Integer.parseInt(chosenDate[1]) &&
                    Integer.parseInt(arrCurrDate[0]) > Integer.parseInt(chosenDate[0])){
                Toast.makeText(this, "Cannot make reservation", Toast.LENGTH_SHORT).show();
                return;
            }

            //Opening Clock Validation
            String[] time = time_book.split(":");
            String[] AmPm = time_book.split(" ");

            if ((Integer.parseInt(time[0]) < 9 || Integer.parseInt(time[0]) == 12) && AmPm[1].equalsIgnoreCase("Am")){
                Toast.makeText(this, "Salon hasn't opened yet! (09:00 - 21:00)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Integer.parseInt(time[0]) > 9 && AmPm[1].equalsIgnoreCase("Pm")){
                Toast.makeText(this, "Salon has already closed! (09:00 - 21:00)", Toast.LENGTH_SHORT).show();
                return;
            }


            Bundle bundle = new Bundle();
            bundle.putString("salon_name", salon_name);
            bundle.putString("barber_name", barber_name);
            bundle.putString("time_book", time_book);
            bundle.putString("date_book", date_book);
            bundle.putString("service_selected", service_selected);
            bundle.putInt("price", price);
            bundle.putInt("imageId", imageId);
            ConfirmationDialog confirmationDialog = new ConfirmationDialog();
            confirmationDialog.setArguments(bundle);
            confirmationDialog.show(getSupportFragmentManager(), "confirmation dialog");


        });

    }

    @Override
    public void onBarberClick(int position) {
//        Toast.makeText(this, "" + barberName[position], Toast.LENGTH_SHORT).show();
        selected_barber = barberName[position];

    }

    @Override
    public void onServiceClick(int position) {
//        Toast.makeText(this, "" + serviceName[position], Toast.LENGTH_SHORT).show();
        if (selected_service.contains(serviceName[position])){
//            Toast.makeText(this, "Removing", Toast.LENGTH_SHORT).show();
            selected_service.remove(serviceName[position]);
            price -= priceList[position];
        }
        else {
//            Toast.makeText(this, "Adding", Toast.LENGTH_SHORT).show();
            selected_service.add(serviceName[position]);
            price += priceList[position];
        }
        tv_price = findViewById(R.id.tv_price);
        tv_price.setText("Harga : " + price);
    }
}