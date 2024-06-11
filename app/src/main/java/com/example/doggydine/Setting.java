package com.example.doggydine;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Setting extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PetAccount> arrayList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    Button logOut, connecting;
    private static final String TAG = "BluetoothApp";
    private static final String DEVICE_ADDRESS = "00:14:03:05:59:43"; // 라즈베리파이 블루투스 MAC 주소
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LottieAnimationView lottieAnimationView = findViewById(R.id.LT_s_dog_sleep);
        lottieAnimationView.setAnimation(R.raw.dog_sleep); // .json 파일을 로드
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();

        logOut = findViewById(R.id.logout_btn);
        connecting = findViewById(R.id.bluetooth_btn);

        recyclerView = findViewById(R.id.DogInfoEdit);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        uid = mFirebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("DoggyDine").child("UserAccount").child(uid).child("pet");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();

                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    PetAccount petaccount = new PetAccount();
                    Map<String, String> profileMap = petaccount.getProfile();
                    petaccount.setProfile1(snapshot.child("profile").child("profile1").getValue(String.class));
                    petaccount.setDog_name(snapshot.child("dog_name").getValue(String.class));
                    arrayList.add(petaccount); // 데이터를 리스트에 추가
                    Log.d("PetAccount", petaccount.toString());
                }
                adapter.notifyDataSetChanged(); // 데이터 변경을 어댑터에 알림
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                builder.setTitle("로그아웃 확인");
                builder.setMessage("정말 로그아웃하시겠습니까?");
                builder.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();

                        SharedPreferences sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("username");
                        editor.remove("password");
                        editor.apply();

                        Intent intent = new Intent(Setting.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        adapter = new DogInfoAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);
        connecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting.this, CodeGenerator.class);
                startActivity(intent);
                connectBluetooth();
            }
        });
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }
    private void connectBluetooth() {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            Toast.makeText(this, "Bluetooth connected", Toast.LENGTH_SHORT).show();
            sendData(uid);
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Bluetooth", e);
            Toast.makeText(this, "Bluetooth connection failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendData(String data) {
        try {
            outputStream = bluetoothSocket.getOutputStream();
            outputStream.write(data.getBytes());
            Toast.makeText(this, "Data sent", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, "Error sending data", e);
            Toast.makeText(this, "Failed to send data", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error closing Bluetooth connection", e);
        }
    }
}