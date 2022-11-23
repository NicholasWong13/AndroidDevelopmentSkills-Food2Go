package com.example.food2go;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BillingDetailsActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private String totalAmount;
    TextView confirmOrderBtn;
    Button current_location;
    ManagementCard managementCard;

    FusedLocationProviderClient fusedLocationProviderClient;

    EditText nameEditText,phoneEditText,adresseEditText,cityEditText,textView_Location,editTextTextPersonName;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_details);
        Intent intent = getIntent();
        totalAmount = intent.getStringExtra("total Amount");
        managementCard = new ManagementCard(this);



        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        userId=user.getUid();
        nameEditText = findViewById(R.id.editTextTextPersonName6);
        phoneEditText = findViewById(R.id.editTextTextPersonName5);
        adresseEditText = findViewById(R.id.textView_Location);
        cityEditText = findViewById(R.id.editTextTextPersonName);
        textView_Location =  findViewById(R.id.textView_Location);
        editTextTextPersonName =  findViewById(R.id.editTextTextPersonName);

        confirmOrderBtn = (TextView) findViewById(R.id.button1);
        current_location = (Button) findViewById(R.id.button_location);



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);




        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmOrder();
            }
        });

        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(BillingDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                    getLocation();

                }
                else{
                    ActivityCompat.requestPermissions(BillingDetailsActivity.this,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },100);
                }

            }
        });

    }


    @SuppressLint("MissingPermission")
    private void getLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null){
                    Geocoder geocoder = new Geocoder(BillingDetailsActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );

                        textView_Location.setText(addresses.get(0).getAdminArea()+ "," + addresses.get(0).getLocality() + "," + addresses.get(0).getSubLocality());
                        editTextTextPersonName.setText(addresses.get(0).getLocality());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private  void ConfirmOrder(){
        final String saveCurrentTime,saveCurrentDate;
        Calendar CalForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(CalForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH::mm:ss a");
        saveCurrentTime = currentTime.format(CalForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(userId);

        HashMap<String,Object> ordersMap = new HashMap<>();
        ordersMap.put("total_amount",totalAmount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneEditText.getText().toString());
        ordersMap.put("adresse",adresseEditText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state","not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    managementCard.FreeCard();
                    Toast.makeText(BillingDetailsActivity.this,"Your final order has been confirmed ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BillingDetailsActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }



}