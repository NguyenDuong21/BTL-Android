package com.example.testapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.Model.Category;
import com.example.testapplication.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

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
        }
    }
}
