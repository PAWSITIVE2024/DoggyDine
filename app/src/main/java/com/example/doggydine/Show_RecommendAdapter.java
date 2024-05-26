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

import java.util.ArrayList;
import java.util.Map;

public class Show_RecommendAdapter extends RecyclerView.Adapter<Show_RecommendAdapter.Show_RecommendViewHolder>{
    private ArrayList <Food> arrayList;
    private Context context;

    public Show_RecommendAdapter(ArrayList<Food>arrayList,Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Show_RecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_list_item,parent,false);
        Show_RecommendViewHolder holder = new Show_RecommendViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
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
    public void onBindViewHolder(@NonNull Show_RecommendViewHolder holder , int position){
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);

        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_score.setText(arrayList.get(position).getScore());
        holder.tv_price.setText("(100g당)"+arrayList.get(position).getPrice()+"원");
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


    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class Show_RecommendViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_profile;
        TextView tv_name,tv_score,tv_price,tv_material;
        public Show_RecommendViewHolder (@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_name = itemView.findViewById(R.id.Tv_i_Name);
            this.tv_score = itemView.findViewById(R.id.Tv_i_Score);
            this.tv_price = itemView.findViewById(R.id.Tv_i_Price);
            this.tv_material = itemView.findViewById(R.id.Tv_i_Material);
        }
    }
}
