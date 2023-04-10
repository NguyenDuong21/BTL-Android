package com.example.MyBTL.Adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyBTL.Model.Product;
import com.example.MyBTL.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAnimationAdapter extends RecyclerView.Adapter<RecyclerAnimationAdapter.ViewHolder> {


    Context context;
    List<Product> list;

    public RecyclerAnimationAdapter(Context context) {

        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_add, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        holder.name.setText(list.get(i).getName());
        Picasso.get().load(list.get(i).getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setProducts(List<Product> list) {
        this.list = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name;
        View parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_hitory_product);
            name = itemView.findViewById(R.id.tv_hitory_product_name);
//            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}