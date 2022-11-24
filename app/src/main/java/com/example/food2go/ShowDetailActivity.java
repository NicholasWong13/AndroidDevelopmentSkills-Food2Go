package com.example.food2go;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.food2go.Domain.FoodDomain;
import com.example.food2go.Helper.ManagementCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowDetailActivity extends AppCompatActivity {

    private TextView addToCardbtn;
    private TextView titleTxt,feeTxt,descriptionTxt,numberOrderTxt;
    private ImageView plusBtn,minusBtn,picFood;
    private FoodDomain object;
    private int numberOrder = 1;
    private FirebaseUser user;
    private String userId;
    private ManagementCard managementCard;
    private  int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdetail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();

        managementCard =new ManagementCard(this);
        initView();
        getBundle();



    }

    private void getBundle() {
        object = (FoodDomain) getIntent().getSerializableExtra("object");

        int drawableResourceId = this.getResources().getIdentifier(object.getPic(),"drawable",this.getPackageName());
        Glide.with(this)
                .load(drawableResourceId)
                .into(picFood);
        titleTxt.setText(object.getTitle());
        /*feeTxt.setText("$"+(object.getFee()));*/
        descriptionTxt.setText(object.getDescription());
        numberOrderTxt.setText(String.valueOf(numberOrder));



        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOrder = numberOrder + 1;
                numberOrderTxt.setText(String.valueOf(numberOrder));
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOrder > 1) {
                    numberOrder = numberOrder - 1;
                }
                numberOrderTxt.setText(String.valueOf(numberOrder));
            }
        });

        addToCardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count ==1 || count ==2){
                    Toast.makeText(ShowDetailActivity.this,"You can purchase more products once you receive your first order ",Toast.LENGTH_SHORT).show();
                }
                else{
                    object.setNumberInCard(numberOrder);
                    managementCard.insertFood(object);
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();

    }

    public void CheckOrderState()
    {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(userId);

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippingState = snapshot.child("state").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                        count = 1;
                    }
                    else if (shippingState.equals("not shipped"))
                    {
                        count = 2;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    private void initView() {
        addToCardbtn = findViewById(R.id.addToCardBtn);
        titleTxt = findViewById(R.id.titleTxt);
        feeTxt = findViewById(R.id.fee);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        numberOrderTxt = findViewById(R.id.numberOrder);
        plusBtn = findViewById(R.id.plusBtn);
        minusBtn = findViewById(R.id.minusBtn);
        picFood = findViewById(R.id.foodPic);

    }
}