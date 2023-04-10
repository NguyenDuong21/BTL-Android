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
import com.example.MyBTL.Model.DetailOrder;
import com.example.MyBTL.Model.Product;
import com.example.MyBTL.Model.ProductCart;
import com.example.MyBTL.ProductDetailActivity;
import com.example.MyBTL.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

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
        /*holder.productView.setText(product.getLuotxem());*/
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
        private TextView Name , Price,productView;
        private ImageView imageView,cardicon;
        public ProductViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.productLabel);
            Price = itemView.findViewById(R.id.productPrice);
            imageView = itemView.findViewById(R.id.productImage);
            cardicon = itemView.findViewById(R.id.imageView7);
            /*productView = itemView.findViewById(R.id.productView);*/
            /*DatabaseReference myref = FirebaseDatabase.getInstance().getReference("DbOrder").child(AGConnectAuth.getInstance().getCurrentUser().getUid());
            final int[] solanmua = {0};
            myref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        String keyOrder = dataSnapshot.getKey();
                        myref.child(keyOrder).child("detail").orderByChild("tag").equalTo(cardicon.getTag().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {
                                for (DataSnapshot dataSnapshot1 : snapshot1.getChildren())
                                {
                                    DetailOrder detailOrder = dataSnapshot1.getValue(DetailOrder.class);
                                    solanmua[0] += detailOrder.getNumProduct();
                                    notifyDataSetChanged();
                                }
                                productView.setText(String.valueOf(solanmua[0]));

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Products").child(cardicon.getTag().toString()).child("luotxem");
                    myref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if(!snapshot.exists())
                            {
                                myref.setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                                        intent.putExtra("idsp", cardicon.getTag().toString());
                                        mContext.startActivity(intent);
                                    }
                                });
                            } else {
                                int luotxem = Integer.parseInt(snapshot.getValue().toString());
                                String luot_xemupdate = String.valueOf(luotxem + 1);
                                myref.setValue(luot_xemupdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                                        intent.putExtra("idsp", cardicon.getTag().toString());
                                        mContext.startActivity(intent);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });*/
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("idsp", cardicon.getTag().toString());
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
