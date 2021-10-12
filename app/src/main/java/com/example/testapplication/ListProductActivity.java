package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.testapplication.Adapter.ProductAdapter;
import com.example.testapplication.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListProductActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProduct;
    DatabaseReference databaseReference;
    private ProductAdapter productAdapter;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        Intent i = getIntent();
        List<Product> list = (ArrayList<Product>) i.getSerializableExtra("products");

        recyclerViewProduct = findViewById(R.id.list_products);
        setProductView(list);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setProductView(List<Product> list)
    {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewProduct.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(this);
        productAdapter.setProducts(list);
        int spanCount = 2;
        int spacing = 25;
        boolean includeEdge = true;
        recyclerViewProduct.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerViewProduct.setAdapter(productAdapter);
    }
    private List<Product> getListProduct()
    {
        List<Product> list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Product product = dataSnapshot.getValue(Product.class);
                    list.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return list;
    }
}