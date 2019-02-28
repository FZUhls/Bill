package com.example.henry.bill;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class MyAdapter_2 extends RecyclerView.Adapter<MyAdapter_2.ViewHolder> {
    private List<Biller> mbillList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View billView;
        ImageView pin;
        TextView billText;
        public ViewHolder(View view){
            super(view);
            billView = view;
            billText = view.findViewById(R.id.bill_text);
            pin = view.findViewById(R.id.pin);
        }
    }
    public void setOnItemClickListener(MyAdapter_2.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public MyAdapter_2(List<Biller> billList){
        mbillList = billList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Biller biller = mbillList.get(position);
        String payorget;
        String sort = biller.getSort();
        switch (sort){
            case "Food":
                holder.pin.setImageResource(R.mipmap.ty_food);
                break;
            case "House":
                holder.pin.setImageResource(R.mipmap.ty_house);
                break;
            case "Clothes":
                holder.pin.setImageResource(R.mipmap.ty_clothes);
                break;
            case "Kids":
                holder.pin.setImageResource(R.mipmap.ty_kid);
                break;
            case "Medical":
                holder.pin.setImageResource(R.mipmap.ty_medical);
                break;
            case "Amuse":
                holder.pin.setImageResource(R.mipmap.ty_amusement);
                break;
            case "Shopping":
                holder.pin.setImageResource(R.mipmap.ty_shoping);
                break;
            case "Wages":
                holder.pin.setImageResource(R.mipmap.ty_wages);
                break;
            case "Gift":
                holder.pin.setImageResource(R.mipmap.ty_hongbao);
                break;
            case "Financing":
                holder.pin.setImageResource(R.mipmap.ty_financing);
                break;
                default:
                    break;
        }
        if(biller.getPay_or_get().equals("pay")){
            payorget = new String("支出");
        }
        else {
            payorget = new String("收入");
        }
        String string = new String(biller.getYear()+"年"+biller.getMonth()+"月"+biller.getDay()+"日"+payorget+""+biller.getCost()+" 来自 "+biller.getSort());
        holder.billText.setText(string);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.itemView,position);
                onItemClickListener.onItemLongClick(holder.itemView,position);
            }
        });
    }
    public  int getItemCount(){
        return mbillList.size();
    }
}

