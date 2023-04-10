package com.example.MyBTL.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyBTL.ListProductActivity;
import com.example.MyBTL.Model.Category;
import com.example.MyBTL.Model.Product;
import com.example.MyBTL.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapterViewHoler>{
    Context context;
    List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> list) {
        this.context = context;
        this.categoryList = list;
    }


    @NonNull
    @NotNull
    @Override
    public CategoryAdapterViewHoler onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_row_items, parent, false);
        return new CategoryAdapterViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryAdapter.CategoryAdapterViewHoler holder, int position) {
        Category category = categoryList.get(position);
        holder.textView.setText(category.getName());
        Picasso.get().load(category.getImage()).into(holder.imageView);
        holder.linearLayout.setTag(category.getTag());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryAdapterViewHoler extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        ImageView imageView;
        TextView textView;
        public CategoryAdapterViewHoler(@NonNull @NotNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.wrap_item);
            imageView = itemView.findViewById(R.id.categoryImage);
            textView = itemView.findViewById(R.id.categoryLabel);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String nameCat = linearLayout.getTag().toString();

                    Query databaseReference = FirebaseDatabase.getInstance().getReference("Products").orderByChild("category").
                            equalTo(nameCat);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            ArrayList<Product> productList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                Product product = dataSnapshot.getValue(Product.class);
                                productList.add(product);
                            }
//                Bundle extra = new Bundle();
//                extra.putSerializable("objects", productList);
                            Intent intent = new Intent(context, ListProductActivity.class);
                            intent.putExtra("products", productList);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }
            });
        }
    }
}
