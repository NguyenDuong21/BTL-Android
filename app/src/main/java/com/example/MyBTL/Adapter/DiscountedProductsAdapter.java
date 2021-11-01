package com.example.MyBTL.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyBTL.Model.DiscountedProducts;
import com.example.MyBTL.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DiscountedProductsAdapter extends RecyclerView.Adapter<DiscountedProductsAdapter.DiscountedProductsViewHoler>{
    Context context;
    List<DiscountedProducts> discountedProductsList;

    public DiscountedProductsAdapter(Context context, List<DiscountedProducts> discountedProductsList) {
        this.context = context;
        this.discountedProductsList = discountedProductsList;
    }

    public void setDiscountedProductsList(List<DiscountedProducts> discountedProductsList) {
        this.discountedProductsList = discountedProductsList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public DiscountedProductsViewHoler onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discounted_row_items, parent, false);
        return new DiscountedProductsViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DiscountedProductsAdapter.DiscountedProductsViewHoler holder, int position) {
        DiscountedProducts discountedProduct = discountedProductsList.get(position);
        holder.imageView.setImageResource(discountedProduct.getImageurl());
    }

    @Override
    public int getItemCount() {
        return discountedProductsList.size();
    }

    public class DiscountedProductsViewHoler extends RecyclerView.ViewHolder{
        ImageView imageView;
        public DiscountedProductsViewHoler(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.discountImage);

        }
    }
}
