package com.example.doggydine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DogInfoAdapter extends RecyclerView.Adapter<DogInfoAdapter.DogSignUpViewHolder> {
    private List<Integer> mLayouts;
    public DogInfoAdapter() {
        mLayouts = new ArrayList<>();
    }
    public void addItem(int layoutResId) {
        mLayouts.add(layoutResId);
        notifyItemInserted(mLayouts.size() - 1);
    }
    @NonNull
    @Override
    public DogSignUpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new DogSignUpViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DogSignUpViewHolder holder, int position) {
        int layoutResId = mLayouts.get(position);
        if (layoutResId == R.layout.dog_sign_up) {
            Button saveButton = holder.itemView.findViewById(R.id.Btn_d_s_register);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.itemView.getContext(), "일단 저장하는 기능 필요!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return mLayouts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mLayouts.get(position);
    }

    static class DogSignUpViewHolder extends RecyclerView.ViewHolder {
        DogSignUpViewHolder(View itemView) {
            super(itemView);
        }
    }
}