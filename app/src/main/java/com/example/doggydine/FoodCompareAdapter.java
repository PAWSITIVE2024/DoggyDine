package com.example.doggydine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class FoodCompareAdapter extends RecyclerView.Adapter<FoodCompareAdapter.FoodCompareViewHolder> {
    private ArrayList<Food> arrayList;
    private String foodName;
    private Context context;

    public FoodCompareAdapter(ArrayList<Food>arrayList,Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodCompareViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_compare_item,parent,false);
        FoodCompareViewHolder holder = new FoodCompareViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCompareViewHolder holder , int position){
        if (position >= 1) {
            holder.fc_score_and_sales_title.setText("");
            holder.fc_manu_title.setText("");
            holder.fc_calorie_title.setText("");
            holder.fc_material_title.setText("");
            holder.fc_price_title.setText("");
            holder.fc_nutrient_title.setText("");
        } else {
            holder.fc_score_and_sales_title.setText("평점/판매량");
            holder.fc_manu_title.setText("제조사");
            holder.fc_calorie_title.setText("칼로리");
            holder.fc_material_title.setText("주원료");
            holder.fc_price_title.setText("가격");
            holder.fc_nutrient_title.setText("영양소");
        }

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.fc_profile_image);
        holder.fc_dog_food_name.setText(arrayList.get(position).getName());
        foodName = arrayList.get(position).getName();
        holder.fc_score.setText(arrayList.get(position).getScore());
        holder.fc_sales_rate.setText("("+arrayList.get(position).getSales_Volume()+")");
        holder.fc_manu.setText(arrayList.get(position).getManu());
        holder.fc_cal.setText("(250g 기준)"+arrayList.get(position).getKcal());

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
        holder.fc_material.setText(materialText.toString());

        holder.tv_fc_cost.setText("(100g당) "+arrayList.get(position).getPrice()+"원");

        Map<String, String> nutrientMap = arrayList.get(position).getNutrient();
        if (nutrientMap != null) {
            holder.fc_protein.setText("조단백질: " + nutrientMap.get("protein")+"%");
            holder.fc_fat.setText("조지방: " + nutrientMap.get("fat")+"%");
            holder.fc_calcium.setText("칼슘: " + nutrientMap.get("calcium")+"%");
            holder.fc_fiber.setText("조섬유: " + nutrientMap.get("fiber")+"%");
            holder.fc_ash.setText("조회분: " + nutrientMap.get("ash")+"%");
            holder.fc_phosphorus.setText("인: " + nutrientMap.get("phosphorus")+"%");
            holder.fc_moisture.setText("수분: " + nutrientMap.get("moisture")+"%");
            holder.fc_omega6.setText("오메가6: " + nutrientMap.get("omega6")+"%");
            holder.fc_omega3.setText("오메가3: " + nutrientMap.get("omega3")+"%");
        }


        holder.fc_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Food food = arrayList.get(clickedPosition);
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference userCheckRef = FirebaseDatabase.getInstance().getReference("DoggyDine")
                            .child("UserAccount")
                            .child(userID)
                            .child("check")
                            .child(food.getName());
                    userCheckRef.setValue(false)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "선택 해제", Toast.LENGTH_SHORT).show();
                                    notifyItemChanged(clickedPosition);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class FoodCompareViewHolder extends RecyclerView.ViewHolder{
        ImageView fc_profile_image;
        TextView fc_score_and_sales_title,fc_score,fc_sales_rate,fc_manu_title,fc_manu,
                fc_calorie_title,fc_cal,fc_material_title,fc_material,fc_price_title,
                tv_fc_cost,fc_nutrient_title,fc_protein,fc_fat,fc_calcium,fc_fiber,fc_ash,
                fc_phosphorus,fc_moisture,fc_omega6,fc_omega3,fc_del,fc_dog_food_name;
        public FoodCompareViewHolder(@NonNull View itemView){
            super(itemView);
            this.fc_dog_food_name = itemView.findViewById(R.id.fc_dog_food_name);
            this.fc_profile_image = itemView.findViewById(R.id.fc_profile_image);
            this.fc_score_and_sales_title = itemView.findViewById(R.id.fc_score_and_sales_title);
            this.fc_score = itemView.findViewById(R.id.fc_score);
            this.fc_sales_rate = itemView.findViewById(R.id.fc_sales_rate);
            this.fc_manu_title = itemView.findViewById(R.id.fc_manu_title);
            this.fc_manu = itemView.findViewById(R.id.fc_manu);
            this.fc_calorie_title = itemView.findViewById(R.id.fc_calorie_title);
            this.fc_cal = itemView.findViewById(R.id.fc_cal);
            this.fc_material_title = itemView.findViewById(R.id.fc_material_title);
            this.fc_material = itemView.findViewById(R.id.fc_material);
            this.fc_price_title = itemView.findViewById(R.id.fc_price_title);
            this.tv_fc_cost = itemView.findViewById(R.id.tv_fc_cost);
            this.fc_nutrient_title = itemView.findViewById(R.id.fc_nutrient_title);
            this.fc_protein = itemView.findViewById(R.id.fc_protein);
            this.fc_fat = itemView.findViewById(R.id.fc_fat);
            this.fc_calcium = itemView.findViewById(R.id.fc_calcium);
            this.fc_fiber = itemView.findViewById(R.id.fc_fiber);
            this.fc_ash = itemView.findViewById(R.id.fc_ash);
            this.fc_phosphorus = itemView.findViewById(R.id.fc_phosphorus);
            this.fc_moisture = itemView.findViewById(R.id.fc_moisture);
            this.fc_omega6 = itemView.findViewById(R.id.fc_omega6);
            this.fc_omega3 = itemView.findViewById(R.id.fc_omega3);
            this.fc_del = itemView.findViewById(R.id.fc_del);

        }

    }
}
