package com.example.MyBTL.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyBTL.Model.DetailOrder;
import com.example.MyBTL.Model.Order;
import com.example.MyBTL.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryProductAdapter extends RecyclerView.Adapter<HistoryProductAdapter.HistoryProductViewHolder> {
    private DecimalFormat formatPrice = new DecimalFormat("###,###,###");
    private List<DetailOrder> listDetailOrder;
    private List<Order> listOrder;
    private Order orderInfo;

    public void setData(List<DetailOrder> listDetailOrder, List<Order> listOrder) {
        this.listDetailOrder = listDetailOrder;
        this.listOrder = listOrder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new  HistoryProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryProductViewHolder holder, int position) {
        DetailOrder detailOrder = listDetailOrder.get(position);
        if (detailOrder == null){
            return;
        }
        else {
            Picasso.get().load(detailOrder.getUrlImg()).into(holder.imgHitoryProduct);
            holder.tvHitoryProductName.setText(detailOrder.getProductName());
            holder.tvHitoryProductNum.setText(String.valueOf(detailOrder.getNumProduct()));
            holder.tvHitoryProductPrice.setText(formatPrice.format(detailOrder.getProductPrice()) + "VNĐ");
            holder.tvHitoryProductStatus.setText(detailOrder.getStatus());
            holder.tvHitoryProductOrderNo.setText(detailOrder.getOrderNo().toUpperCase());
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
            for (Order order : listOrder){
                if (order.getOrderNo().equals(detailOrder.getOrderNo() ) ){
                    try {
                        Date dateConverted = dateFormat1.parse(order.getDateOrder());
                        String dateformated = dateFormat2.format(dateConverted);
                        holder.tvHitoryProductDate.setText(dateformated);
                        break;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }

        }
    }

    @Override
    public int getItemCount() {
        if (listDetailOrder.size() != 0){
            return listDetailOrder.size();
        }else {
            return 0;
        }
    }

    public class HistoryProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHitoryProduct;
        TextView tvHitoryProductName,tvHitoryProductNum,tvHitoryProductPrice,tvHitoryProductDate
                ,tvHitoryProductStatus,tvHitoryProductOrderNo;

        public HistoryProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHitoryProduct = itemView.findViewById(R.id.img_hitory_product);
            tvHitoryProductName = itemView.findViewById(R.id.tv_hitory_product_name);
            tvHitoryProductNum = itemView.findViewById(R.id.tv_hitory_product_num);
            tvHitoryProductPrice = itemView.findViewById(R.id.tv_hitory_product_price);
            tvHitoryProductDate = itemView.findViewById(R.id.tv_hitory_product_date);
            tvHitoryProductStatus = itemView.findViewById(R.id.tv_hitory_product_status);
            tvHitoryProductOrderNo = itemView.findViewById(R.id.tv_hitory_product_orderNo);
        }
    }
}
