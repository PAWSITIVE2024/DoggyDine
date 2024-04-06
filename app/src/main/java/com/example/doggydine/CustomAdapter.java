package com.example.doggydine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doggydine.Food;

import java.util.ArrayList;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Food> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<Food> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             int position = holder.getAdapterPosition();
             if(position != RecyclerView.NO_POSITION) {
                 Food clickedFood = arrayList.get(position);
                 String foodName = clickedFood.getName();

                 Intent intent = new Intent(context,FoodDetailsActivity.class);
                 intent.putExtra("foodName",foodName);
                 context.startActivity(intent);
             }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        //아이템 매칭
        // Glide를 사용하여 프로필 이미지 설정
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);

        // 사료 이름 설정
        holder.tv_name.setText(arrayList.get(position).getName());

        // 사료 평점 설정
        holder.tv_score.setText(arrayList.get(position).getScore());

        // 사료 가격 설정
        holder.tv_price.setText(arrayList.get(position).getPrice()+"원");

        // 주재료 설정 (true인것만)
        Map<String, Boolean> materialMap = arrayList.get(position).getMaterial();
        StringBuilder materialText = new StringBuilder();
        for (Map.Entry<String, Boolean> entry : materialMap.entrySet()) {
            if (entry.getValue()) {
                if (materialText.length() > 0) {
                    materialText.append(", ");
                }
                materialText.append(entry.getKey());
            }
        }
        holder.tv_material.setText(materialText.toString());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        TextView tv_name;
        TextView tv_score;
        TextView tv_price;
        TextView tv_material;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_name = itemView.findViewById(R.id.Tv_i_Name);
            this.tv_score = itemView.findViewById(R.id.Tv_i_Score);
            this.tv_price = itemView.findViewById(R.id.Tv_i_Price);
            this.tv_material = itemView.findViewById(R.id.Tv_i_Material);

        }
    }
}
