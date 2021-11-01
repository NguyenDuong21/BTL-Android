package com.example.MyBTL.Adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyBTL.Model.Product;
import com.example.MyBTL.Model.ProductCart;
import com.example.MyBTL.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>{
    private Context mContext;
    private List<ProductCart> products;

    public CartProductAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setProducts(List<ProductCart> products) {
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
        ProductCart product =products.get(position);
        DecimalFormat format = new DecimalFormat("###,###,###");
        holder.Name.setText(product.getName());
        holder.Price.setText(format.format(product.getPrices()));
        holder.amount.setText(Integer.toString(product.getAmount()));
        Picasso.get().load(product.getImage()).into(holder.imageView);

        holder.imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = (String) holder.amount.getText();
                if(amount.equals("1"))
                {
                    SharedPreferences sharedPreferences =  mContext.getSharedPreferences("shared preferences", MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), null);
                    Type type = new TypeToken<ArrayList<ProductCart>>() {}.getType();
                    List<ProductCart> mExampleList;
                    mExampleList = gson.fromJson(json, type);

                    if (mExampleList != null) {
                        for(int i=0; i<mExampleList.size(); i++)
                        {
                            if(mExampleList.get(i).gettag().equals(product.gettag()))
                            {
                                mExampleList.remove(i);
                                setProducts(mExampleList);
                                notifyDataSetChanged();
                            }
                        }

                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String jsonSave = gson.toJson(mExampleList);
                    editor.putString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), jsonSave);
                    editor.apply();
                } else {
                    SharedPreferences sharedPreferences =  mContext.getSharedPreferences("shared preferences", MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), null);
                    Type type = new TypeToken<ArrayList<ProductCart>>() {}.getType();
                    List<ProductCart> mExampleList;
                    mExampleList = gson.fromJson(json, type);

                    if (mExampleList != null) {
                        for(int i=0; i<mExampleList.size(); i++)
                        {
                            if(mExampleList.get(i).gettag().equals(product.gettag()))
                            {
                                mExampleList.get(i).setAmount(mExampleList.get(i).getAmount() - 1);
                            }
                        }

                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String jsonSave = gson.toJson(mExampleList);
                    editor.putString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), jsonSave);
                    editor.apply();
                }
                holder.amount.setText( Integer.toString(Integer.parseInt(amount) - 1));
            }
        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = (String) holder.amount.getText();
                SharedPreferences sharedPreferences =  mContext.getSharedPreferences("shared preferences", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), null);
                Type type = new TypeToken<ArrayList<ProductCart>>() {}.getType();
                List<ProductCart> mExampleList;
                mExampleList = gson.fromJson(json, type);

                if (mExampleList != null) {
                    for(int i=0; i<mExampleList.size(); i++)
                    {
                        if(mExampleList.get(i).gettag().equals(product.gettag()))
                        {
                            mExampleList.get(i).setAmount(mExampleList.get(i).getAmount() + 1);
                        }
                    }

                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String jsonSave = gson.toJson(mExampleList);
                editor.putString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), jsonSave);
                editor.apply();
                holder.amount.setText( Integer.toString(Integer.parseInt(amount) + 1));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences =  mContext.getSharedPreferences("shared preferences", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), null);
                Type type = new TypeToken<ArrayList<ProductCart>>() {}.getType();
                ArrayList<ProductCart> mExampleList;
                mExampleList = gson.fromJson(json, type);

                if (mExampleList != null) {
                    for(int i=0; i<mExampleList.size(); i++)
                    {
                        if(mExampleList.get(i).gettag().equals(product.gettag()))
                        {
                            mExampleList.remove(i);
                            setProducts(mExampleList);
                        }
                    }

                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String jsonSave = gson.toJson(mExampleList);
                editor.putString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), jsonSave);
                editor.apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartProductViewHolder extends RecyclerView.ViewHolder{
        private TextView Name , Price,amount;
        private ImageView imageView;
        private ImageButton imageButton2, imageButton,delete;
        public CartProductViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name);
            Price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.imageView5);
            amount = itemView.findViewById(R.id.amount);
            imageButton2 = itemView.findViewById(R.id.imageButton2);
            imageButton = itemView.findViewById(R.id.imageButton);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}

