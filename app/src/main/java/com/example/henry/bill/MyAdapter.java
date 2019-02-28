package com.example.henry.bill;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Sort> msortList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View sortView;
        ImageView sortImage;
        TextView sortText;
        public ViewHolder(View view){
            super(view);
            sortView = view;
            sortImage = view.findViewById(R.id.sort_image);
            sortText = view.findViewById(R.id.sort_text);
        }
    }
    public void setOnItemClickListener(MyAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public MyAdapter(List<Sort> sortList){
        msortList = sortList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sort sort = msortList.get(position);
        holder.sortImage.setImageResource(sort.getImageId());
        holder.sortText.setText(sort.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.itemView,position);
                onItemClickListener.onItemLongClick(holder.itemView,position);
            }
        });
    }
    public  int getItemCount(){
        return msortList.size();
    }
}

