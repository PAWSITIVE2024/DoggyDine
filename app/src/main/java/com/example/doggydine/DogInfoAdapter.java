package com.example.doggydine;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DogInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    private List<Integer> mLayouts;

    public DogInfoAdapter() {
        mLayouts = new ArrayList<>();
    }

    public void addItem(int layoutResId) {
        mLayouts.add(layoutResId);
        notifyItemInserted(mLayouts.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mLayouts.size()) {
            return VIEW_TYPE_FOOTER; // Footer 뷰 타입
        } else {
            return VIEW_TYPE_ITEM; // Item 뷰 타입
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_profile_item, parent, false);
            return new DogSignUpViewHolder(itemView);
        } else { // Footer
            View footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adding_btn, parent, false);
            return new FooterViewHolder(footerView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DogSignUpViewHolder) {


        } else if (holder instanceof FooterViewHolder) {
            // Footer ViewHolder에 대한 처리
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            CircleImageView addButton = footerViewHolder.itemView.findViewById(R.id.adding_btn);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 추가 버튼 클릭 시 새로운 항목 추가
                    addItem(R.layout.dog_profile_item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        // 아이템 개수 + Footer (추가 버튼) 개수
        return mLayouts.size() + 1;
    }

    static class DogSignUpViewHolder extends RecyclerView.ViewHolder {


        DogSignUpViewHolder(View itemView) {
            super(itemView);
        }

    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
