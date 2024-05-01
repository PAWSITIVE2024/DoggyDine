package com.example.doggydine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private CircleImageView circleImageView;
    private EditText etName, etAge, etWeight, etActiveRate, etAllergy;
    private TextView tvGo;
    private String imageUrl = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView mRecyclerView;
    private ImageButton mAddButton;
    private DogInfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mRecyclerView = findViewById(R.id.DogInfoEdit);
        mAddButton = findViewById(R.id.adding_btn);

        mAdapter = new DogInfoAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.addItem(R.layout.dog_sign_up);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 추가 버튼 클릭 시 새로운 항목 추가
                mAdapter.addItem(R.layout.dog_sign_up);
            }
        });
    }
}
