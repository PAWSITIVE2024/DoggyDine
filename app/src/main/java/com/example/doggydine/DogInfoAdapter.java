package com.example.doggydine;

import android.content.Context;
import android.content.Intent;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class DogInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    private ArrayList<PetAccount> arrayList;
    private Context context;

    public DogInfoAdapter(ArrayList<PetAccount> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_profile_item, parent, false);
            return new DogInfoViewHolder(view);
        } else { // Footer
            View footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adding_btn, parent, false);
            return new FooterViewHolder(footerView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DogInfoViewHolder) {
            DogInfoViewHolder viewHolder = (DogInfoViewHolder) holder;
            PetAccount pet = arrayList.get(position);
            viewHolder.bind(pet);
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            // Footer ViewHolder에 대한 처리
        }
    }

    @Override
    public int getItemCount() {
        // 아이템 개수 + Footer (추가 버튼) 개수
        return arrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == arrayList.size()) {
            return VIEW_TYPE_FOOTER; // Footer 뷰 타입
        } else {
            return VIEW_TYPE_ITEM; // Item 뷰 타입
        }
    }

    public class DogInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_dog_profile;
        TextView tv_dog_name;

        public DogInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_dog_profile = itemView.findViewById(R.id.cv_dog_profile);
            tv_dog_name = itemView.findViewById(R.id.d_p_dogname);

            // 클릭 리스너 설정
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // 아이템 클릭 시 처리
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                PetAccount pet = arrayList.get(position);
                String dogName = pet.getDog_name();
                // Intent를 생성하여 DogDetailActivity에 전달
                Intent intent = new Intent(context, DogDetailsActivity.class);
                intent.putExtra("dog_name", dogName);
                context.startActivity(intent);
                Log.d("DogInfoAdapter", "Clicked item position: " + position + ", dog name: " + dogName);
            }
        }

        public void bind(PetAccount pet) {
            // 데이터를 뷰에 바인딩하는 작업
            Glide.with(context).load(pet.getProfile1()).into(iv_dog_profile);
            tv_dog_name.setText(pet.getDog_name());
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        CircleImageView madding_btn;
        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            madding_btn = itemView.findViewById(R.id.adding_btn);
            madding_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DogSignUp.class);
                    view.getContext().startActivity(intent);

                }
            });

            // Footer ViewHolder에 대한 처리
        }
    }
}
