package com.example.MyBTL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.MyBTL.Adapter.CartProductAdapter;
import com.example.MyBTL.Model.Product;
import com.example.MyBTL.Model.ProductCart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.agconnect.auth.AGConnectAuth;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProduct;
    DatabaseReference databaseReference;
    private CartProductAdapter cartProductAdapter;
    private ImageView back;
    private List<ProductCart> mExampleList;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerViewProduct = findViewById(R.id.list_products_cart);
        back = findViewById(R.id.back);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this, OrderActivity.class);
                startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), null);
        Type type = new TypeToken<ArrayList<ProductCart>>() {}.getType();
        mExampleList = gson.fromJson(json, type);

        if (mExampleList == null) {
            mExampleList = new ArrayList<>();
        } else {
            /*Toast.makeText(this, "Số lượng sp" + mExampleList.size(),
                    Toast.LENGTH_SHORT).show();*/
        }

        setProductView(mExampleList);
    }
    private void setProductView(List<ProductCart> listData)
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewProduct.setLayoutManager(layoutManager);
        cartProductAdapter = new CartProductAdapter(this);
        cartProductAdapter.setProducts(listData);
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