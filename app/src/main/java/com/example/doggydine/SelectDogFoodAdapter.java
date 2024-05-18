    package com.example.doggydine;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.util.Log;
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

    public class SelectDogFoodAdapter extends RecyclerView.Adapter<SelectDogFoodAdapter.SelectDogFoodViewHolder> {

        private ArrayList<Food> arrayList;
        private Context context;

        public SelectDogFoodAdapter(ArrayList<Food> arrayList,Context context){
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public SelectDogFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            SelectDogFoodViewHolder holder = new SelectDogFoodViewHolder(view);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Food clickedFood = arrayList.get(position);
                        String foodName = clickedFood.getName();

                        if (!foodName.isEmpty()) {
                            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("FromSelectDogFood", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.putString("foodName", foodName);
                            editor.apply();
                            ((Activity) view.getContext()).finish();
                        }

                    }
                }
            });
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull SelectDogFoodViewHolder holder,int position) {
            Glide.with(holder.itemView)
                    .load(arrayList.get(position).getProfile())
                    .into(holder.iv_profile);
            holder.mCheckHeart.setVisibility(View.GONE);


            // 사료 이름 설정
            holder.tv_name.setText(arrayList.get(position).getName());

            // 사료 평점 설정
            holder.tv_score.setText(arrayList.get(position).getScore());

            // 사료 가격 설정
            holder.tv_price.setText("(100g당) "+arrayList.get(position).getPrice()+"원");

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


        public class SelectDogFoodViewHolder extends RecyclerView.ViewHolder{
            ImageView iv_profile;
            TextView tv_name;
            TextView tv_score;
            TextView tv_price;
            TextView tv_material;
            ImageView mCheckHeart;

            public SelectDogFoodViewHolder(@NonNull View itemView){
                super(itemView);
                this.iv_profile = itemView.findViewById(R.id.iv_profile);
                this.tv_name = itemView.findViewById(R.id.Tv_i_Name);
                this.tv_score = itemView.findViewById(R.id.Tv_i_Score);
                this.tv_price = itemView.findViewById(R.id.Tv_i_Price);
                this.tv_material = itemView.findViewById(R.id.Tv_i_Material);
                this.mCheckHeart = itemView.findViewById(R.id.check_heart);
            }
        }

    }
