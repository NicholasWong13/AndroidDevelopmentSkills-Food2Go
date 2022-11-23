package com.example.food2go;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartListActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private  RecyclerView recyclerViewList;
    public  ManagementCard managementCard;
    private FirebaseUser user;
    private String userId;
    private TextView totalFeeTxt,taxTxt,deliveryTxt,totalTxt,emptyTxt,msgTxt,checkoutBtn;
    private  double tax, total;
    private String totalAmount;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlist);
        managementCard = new ManagementCard(this);
        checkoutBtn =(TextView) findViewById(R.id.textView18);
        msgTxt =(TextView) findViewById(R.id.MessageTxt);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();

        initView();
        initList();
        calculateCard();
        bottomNavigation();



        totalAmount = "$"+total;
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartListActivity.this,BillingDetailsActivity.class);
                intent.putExtra("total Amount",totalAmount);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managementCard.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                calculateCard();

            }
        });
        recyclerViewList.setAdapter(adapter);

        if(managementCard.getListCart().isEmpty()){
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else{
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

        }
    }
    private void bottomNavigation() {
        FloatingActionButton floatingActionButton = findViewById(R.id.card_btn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this,CartListActivity.class));
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this,MainActivity.class));
            }
        });
    }
    private void calculateCard(){
        double percentTax=0.02;
        double delivery=10;
        tax = Math.round((managementCard.getTotalFee()*percentTax)*100.0)/100.0;
        total = Math.round((managementCard.getTotalFee() + tax +delivery )*100.0)/100.0;
        double itemTotal = Math.round(managementCard.getTotalFee()*100.0)/100.0;
        totalFeeTxt.setText("$" + itemTotal);
        taxTxt.setText("$"+ tax);
        deliveryTxt.setText("$" + delivery);
        totalTxt.setText("$"+total);
    }
    private void initView() {
        recyclerViewList = findViewById(R.id.recyclerview);
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.TaxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        emptyTxt = findViewById(R.id.emptyTxt);
        scrollView = findViewById(R.id.scrollView4);

    }

    private void CheckOrderState()
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
                        recyclerViewList.setVisibility(View.GONE);
                        msgTxt.setVisibility(View.VISIBLE);
                        emptyTxt.setVisibility(View.GONE);
                        msgTxt.setText("Congratulations , your final product has been shipped successfully. Soon you will receive your order at your door step");
                        checkoutBtn.setVisibility(View.GONE);
                        Toast.makeText(CartListActivity.this,"You can purchase more products once you receive your first order ",Toast.LENGTH_SHORT).show();
                    }
                    else if (shippingState.equals("not shipped"))
                    {
                        recyclerViewList.setVisibility(View.GONE);
                        msgTxt.setVisibility(View.VISIBLE);
                        checkoutBtn.setVisibility(View.GONE);
                        emptyTxt.setVisibility(View.GONE);
                        Toast.makeText(CartListActivity.this,"You can purchase more products once you receive your first order ",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
