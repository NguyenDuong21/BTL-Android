package com.example.MyBTL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.MyBTL.Adapter.CategoryAdapter;
import com.example.MyBTL.Adapter.DiscountedProductsAdapter;
import com.example.MyBTL.Adapter.ProductAdapter;
import com.example.MyBTL.Model.Category;
import com.example.MyBTL.Model.DiscountedProducts;
import com.example.MyBTL.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.huawei.agconnect.auth.AGConnectAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcvView;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private DiscountedProductsAdapter discountedProductsAdapter;
    private List<DiscountedProducts> discountedProductsList;
    private RecyclerView discountRecyclerView;
    private RecyclerView recyclerViewProduct;
    DatabaseReference databaseReference;
    ImageButton searchBtn;
    EditText searchInputText;
    ImageView openCart;
    Button themsanpham;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        discountRecyclerView = findViewById(R.id.discountedRecycler);
        recyclerViewProduct = findViewById(R.id.product_item);
        discountedProductsList = new ArrayList<>();
        discountedProductsList = new ArrayList<>();
        discountedProductsList.add(new DiscountedProducts(1, R.drawable.discountberry));
        discountedProductsList.add(new DiscountedProducts(2, R.drawable.discountbrocoli));
        discountedProductsList.add(new DiscountedProducts(3, R.drawable.discountmeat));
        discountedProductsList.add(new DiscountedProducts(4, R.drawable.discountberry));
        discountedProductsList.add(new DiscountedProducts(5, R.drawable.discountbrocoli));
        discountedProductsList.add(new DiscountedProducts(6, R.drawable.discountmeat));
        rcvView = findViewById(R.id.categoryRecycler);
        themsanpham = findViewById(R.id.btnthemsanpham);

        themsanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, add_product.class);
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Category");

        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        rcvView.setLayoutManager(linearLayout);
        List<Category> categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this,categoryList);
        rcvView.setAdapter(categoryAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Category category = dataSnapshot.getValue(Category.class);

                    categoryList.add(category);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        setDiscountedProductView();
//        setProductView();
        getListProduct();
        searchBtn = findViewById(R.id.search_btn);
        searchInputText = findViewById(R.id.editText);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = String.valueOf(searchInputText.getText());
                if(!searchText.isEmpty())
                {
                    searchProduct(searchText);
                }
            }
        });
        openCart = findViewById(R.id.imageView);
        openCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AGConnectAuth.getInstance().getCurrentUser() == null)
                {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                }

            }
        });

    }
    private void searchProduct(String nameProduct)
    {
//        ArrayList<Product> productList = new ArrayList<>();
//        Query databaseReference = FirebaseDatabase.getInstance().getReference("Products").orderByChild("Name").
//                startAt(nameProduct.toUpperCase()).endAt(nameProduct.toLowerCase()+"\uf8ff");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren())
//                {
//                    Product product = dataSnapshot.getValue(Product.class);
//                    productList.add(product);
//                }
////                Bundle extra = new Bundle();
////                extra.putSerializable("objects", productList);
//                Intent intent = new Intent(MainActivity.this, ListProductActivity.class);
//                intent.putExtra("products", productList);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<Product> products = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Product product =  dataSnapshot.getValue(Product.class);
                    String nameproduct_firebase = product.getName().toLowerCase();
                    if(nameproduct_firebase.contains(nameProduct.toLowerCase()))
                    {
                        products.add(product);
                    }
                }
                Intent intent = new Intent(MainActivity.this, ListProductActivity.class);
                intent.putExtra("products", products);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    private void setDiscountedProductView()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        discountRecyclerView.setLayoutManager(layoutManager);
        discountedProductsAdapter = new DiscountedProductsAdapter(this,discountedProductsList);
        discountRecyclerView.setAdapter(discountedProductsAdapter);
    }
    /*private void setProductView()
    {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewProduct.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(this);
        productAdapter.setProducts(getListProduct());
        int spanCount = 2;
        int spacing = 20;
        boolean includeEdge = true;
        recyclerViewProduct.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerViewProduct.setAdapter(productAdapter);
    }*/
    private void getListProduct()
    {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewProduct.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(MainActivity.this);

        int spanCount = 2;
        int spacing = 20;
        boolean includeEdge = true;
        recyclerViewProduct.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<Product> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Product product = dataSnapshot.getValue(Product.class);
                    list.add(product);
                }
                /*Collections.sort(list, (p1,p2) -> {
                    return (p1.getPrices() < p2.getPrices()) ? -1 : 1; //   negative p1 before p2, positive p2 before p1
                });*/
                productAdapter.setProducts(list);
                recyclerViewProduct.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    public void openProfile(View view)
    {
        if(AGConnectAuth.getInstance().getCurrentUser() == null)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }

    }
}