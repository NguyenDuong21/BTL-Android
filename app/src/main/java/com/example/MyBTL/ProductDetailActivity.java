package com.example.MyBTL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.MyBTL.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView big_image, back;
    private TextView productName, prodDesc, prodPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        big_image = findViewById(R.id.big_image);
        productName = findViewById(R.id.productName);
        prodDesc = findViewById(R.id.prodDesc);
        prodPrice = findViewById(R.id.prodPrice);
        back = findViewById(R.id.back2);
        Intent i = getIntent();
        String id_sp = i.getStringExtra("idsp");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Query databaseReference = FirebaseDatabase.getInstance().getReference("Products").orderByChild("tag").equalTo(id_sp);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Product product = dataSnapshot.getValue(Product.class);
                    productName.setText(product.getName());
                    prodDesc.setText(product.getDescription());
                    prodPrice.setText(product.getPrices() + " VNĐ");
                    Picasso.get().load(product.getImage()).into(big_image);
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}