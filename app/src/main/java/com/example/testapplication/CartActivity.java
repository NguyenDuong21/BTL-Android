package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.testapplication.Adapter.CartProductAdapter;
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

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProduct;
    DatabaseReference databaseReference;
    private CartProductAdapter cartProductAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerViewProduct = findViewById(R.id.list_products_cart);
        setProductView();
    }
    private void setProductView()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewProduct.setLayoutManager(layoutManager);
        cartProductAdapter = new CartProductAdapter(this);
        cartProductAdapter.setProducts(getListProduct());
        recyclerViewProduct.setAdapter(cartProductAdapter);
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
                cartProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return list;
    }
}