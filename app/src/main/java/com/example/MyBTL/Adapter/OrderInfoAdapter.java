package com.example.MyBTL.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyBTL.Model.DetailOrder;
import com.example.MyBTL.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class OrderInfoAdapter extends RecyclerView.Adapter<OrderInfoAdapter.OrderInfoViewHolder>{
    private DecimalFormat formatPrice = new DecimalFormat("###,###,###");
    private List<DetailOrder> listDetailOrder;
    private Context mContext;
    public OrderInfoAdapter(Context mContext)
    {
        this.mContext = mContext;
    }
    public void setDetailOrder(List<DetailOrder> listDetailOrder)
    {
        this.listDetailOrder = listDetailOrder;
        notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public OrderInfoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_info,parent,false);
        return new OrderInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderInfoAdapter.OrderInfoViewHolder holder, int position) {
        DetailOrder detailOrder = listDetailOrder.get(position);
        if (detailOrder == null){
            return;
        }
        else {
            Picasso.get().load(detailOrder.getUrlImg()).into(holder.imgOrderInfo);
            holder.tvOrderInfoName.setText(detailOrder.getProductName());
            holder.tvOrderInfoNum.setText(String.valueOf(detailOrder.getNumProduct()));
            holder.tvOrderInfoPrice.setText(formatPrice.format(detailOrder.getProductPrice()) + " VNĐ");
            holder.tvOrderInfoStatus.setText(detailOrder.getStatus());
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrderInfoViewHolder extends RecyclerView.ViewHolder{

        ImageView imgOrderInfo;
        TextView tvOrderInfoName,tvOrderInfoNum,tvOrderInfoPrice,tvOrderInfoStatus;

        public OrderInfoViewHolder(@NonNull View itemView) {
            super(itemView);

            imgOrderInfo = itemView.findViewById(R.id.img_order_info);
            tvOrderInfoName = itemView.findViewById(R.id.tv_order_info_name);
            tvOrderInfoNum = itemView.findViewById(R.id.tv_order_info_num);
            tvOrderInfoPrice = itemView.findViewById(R.id.tv_order_info_price);
            tvOrderInfoStatus = itemView.findViewById(R.id.tv_order_info_status);
        }
    }
}
