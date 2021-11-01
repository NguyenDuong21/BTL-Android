package com.example.MyBTL.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyBTL.LoginActivity;
import com.example.MyBTL.Model.Product;
import com.example.MyBTL.Model.ProductCart;
import com.example.MyBTL.ProductDetailActivity;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private Context mContext;
    private List<Product> products;

    public ProductAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View productView = inflater.inflate(R.layout.product_row_item, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(productView);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product =products.get(position);
        DecimalFormat format = new DecimalFormat("###,###,###");
        holder.Name.setText(product.getName());
        holder.Price.setText( format.format(product.getPrices()));
        holder.cardicon.setTag(product.gettag());
        holder.cardicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCart prCart = new ProductCart(product.getName(), product.getCategory(), product.getDescription(), product.getPrices(), product.gettag(),1, product.getImage());

                SharedPreferences sharedPreferences =  mContext.getSharedPreferences("shared preferences", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), null);
                Type type = new TypeToken<ArrayList<ProductCart>>() {}.getType();
                ArrayList<ProductCart> mExampleList;
                mExampleList = gson.fromJson(json, type);

                if (mExampleList == null) {
                    mExampleList = new ArrayList<>();
                    mExampleList.add(prCart);
                } else {
                    boolean check = false;
                    for(int i=0; i<mExampleList.size(); i++)
                    {
                        if(mExampleList.get(i).gettag().equals(prCart.gettag()))
                        {
                            mExampleList.get(i).setAmount(mExampleList.get(i).getAmount() + 1);
                            check = true;
                        }
                    }
                    if(!check)
                    {
                        mExampleList.add(prCart);
                    }
                }
                Toast.makeText(mContext, "Đã thêm vào giỏ hàng",
                        Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String jsonSave = gson.toJson(mExampleList);
                editor.putString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), jsonSave);
                editor.apply();

                Log.e("tag", "MyTag " + product.gettag());
            }
        });
        Picasso.get().load(product.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private TextView Name , Price;
        private ImageView imageView,cardicon;
        public ProductViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.productLabel);
            Price = itemView.findViewById(R.id.productPrice);
            imageView = itemView.findViewById(R.id.productImage);
            cardicon = itemView.findViewById(R.id.imageView7);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("idsp", cardicon.getTag().toString());
                    mContext.startActivity(intent);
                }
            });
            /*cardicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences =  mContext.getSharedPreferences("shared preferences", MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("listcart", null);
                    Type type = new TypeToken<ArrayList<ProductCart>>() {}.getType();
                    ArrayList<ProductCart> mExampleList;
                    mExampleList = gson.fromJson(json, type);

                    if (mExampleList == null) {
                        mExampleList = new ArrayList<>();
                    }

                    Log.e("tag", "MyTag " + cardicon.getTag());
                }
            });*/
        }
    }
}
