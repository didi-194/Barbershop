package com.example.finalproject;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    ArrayList <Barbershop> vBarbershop;

    ListView home_list;
    ListAdapter listAdapter;
    TextView tv_user_name;

    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference db;

    //Location Stuff
    private ApiInterface apiInterface;
    double latitude, longitude;
    LocationManager lm;
    String bestProvider;
    String[] distance;
    Criteria criteria;

    //Enabling GPS
    public static final int REQUEST_CHECK_SETTING = 1001;
    LocationRequest locationRequest;
    Boolean gpsEnabled = false;
    ProgressDialog pd;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_user_name = view.findViewById(R.id.tv_user_name);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db = FirebaseDatabase
                .getInstance("https://finalproject-38e5c-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("User").child(id).child("name");


        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_user_name.setText(MessageFormat.format("Hi, {0}", Objects.requireNonNull(snapshot.getValue()).toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //List view
        home_list = view.findViewById(R.id.home_list);
        String[] name = {"Captain Barbershop Borobudur",
                "Captain Barbershop Citra Garden 2",
                "Captain Barbershop City Resort",
                "Captain Barbershop Duta Mas",
                "Captain Barbershop Greenville",
                "Captain Barbershop Semanan",
                "Captain Barbershop Palem"};
        String[] loc = {"Jl. Jelambar Baru Raya No.30-B, RT.4/RW.1, Jelambar Baru, Kec. Grogol petamburan, Kota Jakarta Barat, Daerah Khusus Ibukota Jakarta 11460",
                "Citra garden 2 blok, Gg. I1 No.3, RT.9/RW.12, Pegadungan, Kec. Kalideres, Kota Jakarta Barat, Daerah Khusus Ibukota Jakarta 11840",
                "Blok D no 6, Ruko Miami, City Resort, RT.7/RW.14, East Cengkareng, Cengkareng, West Jakarta City, Jakarta 11730",
                "Jl. Kusuma No.21, RT.4/RW.12, Jelambar Baru, Kec. Grogol petamburan, Kota Jakarta Barat, Daerah Khusus Ibukota Jakarta 11460",
                "Komplek green ville AY no.10 A, Duri Kepa, Jawa Barat 11510",
                "Jl. Taman Semanan Indah No.5, RT.2/RW.7, Duri Kosambi, Cengkareng, West Jakarta City, Jakarta 11750",
                "Jl. Taman Palem Lestari Jl. Perumahan Taman Surya No.8, RT.8/RW.5, Palem, Tegal Alur, Kec. Kalideres, Kota Jakarta Barat, Daerah Khusus Ibukota Jakarta 11820"};
        String[] d_lat_long = {"-6.152780855919324, 106.80461625032424",
                "-6.143642179640272, 106.70749153713382",
                "-6.135009039301629, 106.731024265292",
                "-6.143580664126097, 106.78038622791453",
                "-6.165678711811071, 106.7724294267512",
                "-6.16343796259957, 106.72409593096583",
                "-6.1283479450813445, 106.71535021462176"};
        int[] imageId = {R.drawable.barber_borobudur, R.drawable.citra, R.drawable.barber_city, R.drawable.barber_dutamas, R.drawable.barber_greenville, R.drawable.barber_semanan, R.drawable.barber_palm};
        distance = new String[name.length];

        vBarbershop = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/")
                .build();
        apiInterface = retrofit.create(ApiInterface.class);

        enableGPS();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Getting your location...");
        pd.setCancelable(false);
        pd.show();

        int delay = 5000;

        String[] s_lat_long = new String[1];
        new Handler().postDelayed((Runnable) () -> {
            if (gpsEnabled){
//                pd.dismiss();
                lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                criteria = new Criteria();
                bestProvider = String.valueOf(lm.getBestProvider(criteria,true));

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                    return;
                }

                Toast.makeText(getActivity(), "Searching...", Toast.LENGTH_SHORT).show();
                lm.requestLocationUpdates(bestProvider, 1000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        lm.removeUpdates(this);
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                        criteria = new Criteria();
                        bestProvider = String.valueOf(lm.getBestProvider(criteria,true));

                        s_lat_long[0] = "" + latitude + "," + longitude;
//                        Toast.makeText(getActivity(), "" + latitude + " ,  " + longitude, Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed((Runnable) () -> {
                            s_lat_long[0] = ""+latitude+","+longitude;
                            for (int i = 0; i < name.length; i++){
                                int finalI = i;
                                apiInterface.getDistance(getString(R.string.api_Key), s_lat_long[0], d_lat_long[i])
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new SingleObserver<Result>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(Result result) {

//                                                Toast.makeText(getActivity(), "Result Success", Toast.LENGTH_SHORT).show();
                                                distance[finalI] = result.getRows().get(0).getElements().get(0).getDistance().getText();
                                                System.out.println(distance);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                            new Handler().postDelayed((Runnable) () -> {
                                for (int i = 0; i < name.length; i++){
                                    vBarbershop.add(new Barbershop(name[i], loc[i], distance[i], imageId[i]));
                                }

                                listAdapter = new ListAdapter(getActivity(), vBarbershop);

                                home_list.setAdapter(listAdapter);
                                home_list.setClickable(true);
                                pd.dismiss();
                                home_list.setOnItemClickListener((adapterView, view1, i, l) -> {
                                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                                    intent.putExtra("name", name[i]);
                                    intent.putExtra("loc", loc[i]);
                                    intent.putExtra("imageId", imageId[i]);
                                    startActivity(intent);
                                });

                            }, 1000);

                        },1000);
                    }
                });
            }
            else {
                // Reload Activity
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        }, delay);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getActivity(), ""+gpsEnabled, Toast.LENGTH_SHORT).show();
//            }
//        },1000);
    }

    private String getDistance(String origin, String dest){
        final String[] res = new String[1];

        apiInterface.getDistance(getString(R.string.api_Key), origin, dest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Result result) {

                        Toast.makeText(getActivity(), "Result Success", Toast.LENGTH_SHORT).show();
                        res[0] = result.getRows().get(0).getElements().get(0).getDistance().getText();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        return res[0];
    }

    private void enableGPS() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity().getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                gpsEnabled = true;
                Toast.makeText(getActivity(), "GPS is On", Toast.LENGTH_SHORT).show();

            } catch (ApiException e) {

                switch (e.getStatusCode()){
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTING);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }

            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTING){

            switch (resultCode){

                case Activity.RESULT_OK :
                    if (!gpsEnabled){
                        pd.dismiss();
                    }
                    gpsEnabled = true;
                    Toast.makeText(getActivity(), "GPS is turned on", Toast.LENGTH_SHORT).show();
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(getActivity(), "GPS is required to be turned on", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


}