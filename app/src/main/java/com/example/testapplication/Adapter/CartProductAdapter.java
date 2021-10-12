package com.example.testapplication.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.Model.Product;
import com.example.testapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>{
    private Context mContext;
    private List<Product> products;

    public CartProductAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View productView = inflater.inflate(R.layout.cart_item, parent, false);
        CartProductViewHolder productViewHolder = new CartProductViewHolder(productView);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull CartProductAdapter.CartProductViewHolder holder, int position) {
        Product product =products.get(position);
        holder.Name.setText(product.getName());
        holder.Price.setText(Integer.toString(product.getPrices()));
        Picasso.get().load(product.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartProductViewHolder extends RecyclerView.ViewHolder{
        private TextView Name , Price;
        private ImageView imageView;
        public CartProductViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name);
            Price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.imageView5);
        }
    }
}

