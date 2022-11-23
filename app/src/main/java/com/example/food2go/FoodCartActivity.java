package com.example.food2go;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodCartActivity extends AppCompatActivity {
    private RecyclerView recyclerViewItemsList;
    private RecyclerView.Adapter adapter2;
    private String pos;
    private static final String TAG = "FoodCartActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cart);


        if(getIntent().hasExtra("selected_cat")) {
            CategoryDomain categoryDomain = getIntent().getParcelableExtra("selected_cat");
            pos=categoryDomain.getTitle();
            Log.d(TAG, "onCreate: " + categoryDomain.getTitle());
        }

        recyclerViewPopularList();

    }


    private void recyclerViewPopularList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewItemsList=findViewById(R.id.recyclerView3);
        recyclerViewItemsList.setLayoutManager(linearLayoutManager);
        ArrayList<FoodDomain> foodlist = new ArrayList<>();

        switch (pos){
            case "Pizza":
                foodlist.add(new FoodDomain("Papperoni pizza" ,"pizza1","",9.76));
                foodlist.add(new FoodDomain("Vegetable pizza" , "pizza2","",9.76));
                foodlist.add(new FoodDomain("Vegetable pizza","pizza3","",9.76));
                foodlist.add(new FoodDomain("Vegetable pizza","pizza4","",9.76));
                adapter2= new ItemsAdapter(foodlist);
                recyclerViewItemsList.setAdapter(adapter2);
                break;
            case "Burger":
                foodlist.add(new FoodDomain("Hamburger Veggie" ,"burger","",6.45));
                foodlist.add(new FoodDomain("Cheeseburger Whopper", "burger1", "", 6.24));
                foodlist.add(new FoodDomain("burger with ham and cheese","burger2","",5.99));
                foodlist.add(new FoodDomain("Bacon cheeseburger","burger3","",7.01));
                adapter2= new ItemsAdapter(foodlist);
                recyclerViewItemsList.setAdapter(adapter2);
                break;
            case "Hotdog":
                foodlist.add(new FoodDomain("Hotdog" ,"hotdog","",5.01));
                foodlist.add(new FoodDomain(" Yerevan Hot dog" , "hotdog1","",5.25));
                foodlist.add(new FoodDomain("Hotdog on bun with cheese sauce","hotdog2","",5.99));
                foodlist.add(new FoodDomain("Hotdog with bread and lettuce","hotodog3","",4.99));
                adapter2= new ItemsAdapter(foodlist);
                recyclerViewItemsList.setAdapter(adapter2);
                break;

            case "Drink":
                foodlist.add(new FoodDomain("Sprite" ,"sprite","",1.25));
                foodlist.add(new FoodDomain(" Cola" , "cola","",1.25));
                foodlist.add(new FoodDomain("Fanta","fanta","",1.25));
                foodlist.add(new FoodDomain("All In One","allinone","",3.00));
                adapter2= new ItemsAdapter(foodlist);
                recyclerViewItemsList.setAdapter(adapter2);
                break;

            case "Donut":
                foodlist.add(new FoodDomain("Straweberry Donut" ,"donut","",1.25));
                foodlist.add(new FoodDomain(" Dessert Donut" , "donut1","",1.25));
                foodlist.add(new FoodDomain("Chocolat Donut","donut2","",1.25));
                foodlist.add(new FoodDomain("White and black Donut","donut3","",3.00));
                adapter2= new ItemsAdapter(foodlist);
                recyclerViewItemsList.setAdapter(adapter2);
                break;

        }


    }

}

