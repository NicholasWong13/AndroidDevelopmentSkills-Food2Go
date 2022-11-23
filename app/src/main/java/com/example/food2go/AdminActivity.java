package com.example.food2go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food2go.Domain.AdminOrders;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ordersRef  = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.orders_list);

        ordersList.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef,AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, int position, @NonNull AdminOrders model)
            {
                holder.userName.setText("Name : " + model.getName());
                holder.userPhoneNumber.setText("Phone Number : " + model.getPhone());
                holder.userTotalPrice.setText("Total Price : " + model.getTotal_amount());
                holder.userAddress.setText("Shipping Address : " + model.getAdresse() + " , " + model.getCity());
                holder.userDateTime.setText("Ordered At : " + model.getDate() + "  -  " + model.getTime());
                holder.ship_order_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                        builder.setTitle("Is this Item shipped ?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which ==0)
                                {
                                    String UID =getRef(position).getKey();
                                    RemoveOrder(UID);
                                }
                                else
                                {
                                    finish();
                                }

                            }
                        });
                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout , parent,false);
                return new AdminOrdersViewHolder(view);
            }

        };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }




    public static class AdminOrdersViewHolder extends  RecyclerView.ViewHolder{
        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userAddress;
        public androidx.appcompat.widget.AppCompatButton ship_order_btn ;
        public AdminOrdersViewHolder(@NonNull View itemView)
        {
            super(itemView);
            userName = itemView.findViewById(R.id.order_username);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userAddress = itemView.findViewById(R.id.order_address_city);
            ship_order_btn = itemView.findViewById(R.id.ship_order_btn);
        }
    }


    private void RemoveOrder(String uid)
    {
        ordersRef.child(uid).removeValue();

    }

}