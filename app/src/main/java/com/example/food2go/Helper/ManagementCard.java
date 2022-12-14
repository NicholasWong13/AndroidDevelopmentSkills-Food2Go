package com.example.food2go.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.food2go.Domain.FoodDomain;
import com.example.food2go.Interface.ChangeNumberItemsListener;

import java.util.ArrayList;


public class ManagementCard {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCard(Context context){
        this.context=context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(FoodDomain item) {
        ArrayList<FoodDomain> listFood = getListCard();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listFood.size(); i++) {
            if (listFood.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if (existAlready) {
            listFood.get(n).setNumberInCard(item.getNumberInCard());
        } else {
            listFood.add(item);

        }

        tinyDB.putListObject("CartList",listFood);
        Toast.makeText(context,"Added to your card",Toast.LENGTH_SHORT).show();


    }

    public  ArrayList<FoodDomain> getListCard(){
        return tinyDB.getListObject("CartList");
    }

    public void plusNumberFood(ArrayList<FoodDomain> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener){
        listfood.get(position).setNumberInCard(listfood.get(position).getNumberInCard()+1);
        tinyDB.putListObject("CartList",listfood);
        changeNumberItemsListener.changed();
    }
    public  void MinusNumberFood(ArrayList<FoodDomain> listfood,int position,ChangeNumberItemsListener changeNumberItemsListener){
        if(listfood.get(position).getNumberInCard()==1){
            listfood.remove(position);
        }
        else{
            listfood.get(position).setNumberInCard(listfood.get(position).getNumberInCard() - 1);
        }
        tinyDB.putListObject("CartList",listfood);
        changeNumberItemsListener.changed();
    }
    public  Double getTotalFee() {
        ArrayList<FoodDomain> listfood2 = getListCard();
        double fee= 0;
        for (int i=0;i<listfood2.size();i++){
            fee = fee + (listfood2.get(i).getFee()*listfood2.get(i).getNumberInCard());
        }
        return fee;
    }
    public  void FreeCard(){
        tinyDB.clear();
    }


}
