package com.example.doggydine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

public class CodeGenerator extends AppCompatActivity {
    private ImageView qrCodeImageView;
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    Button endBtn;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_code_generator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mFirebaseAuth = FirebaseAuth.getInstance();
        uid = mFirebaseAuth.getCurrentUser().getUid();
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        generateQRCode(uid);
        endBtn = findViewById(R.id.end_btn);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void generateQRCode(String userId) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            com.google.zxing.common.BitMatrix bitMatrix = qrCodeWriter.encode(userId, BarcodeFormat.QR_CODE, 500, 500);
            Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565);
            for (int x = 0; x < 500; x++) {
                for (int y = 0; y < 500; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}