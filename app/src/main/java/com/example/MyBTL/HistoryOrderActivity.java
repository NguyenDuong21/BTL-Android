package com.example.MyBTL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.MyBTL.Adapter.HistoryProductAdapter;
import com.example.MyBTL.Adapter.OrderInfoAdapter;
import com.example.MyBTL.Model.DetailOrder;
import com.example.MyBTL.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.agconnect.auth.AGConnectAuth;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HistoryOrderActivity extends AppCompatActivity {
    private RecyclerView rcvHistory;
    private List<Order> listOrder;
    private List<DetailOrder> listDetailOrder;
    private HistoryProductAdapter historyProductAdapter;
    private EditText editText;
    private ImageButton searchBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        rcvHistory = findViewById(R.id.list_history);
        editText = findViewById(R.id.editText);
        searchBtn = findViewById(R.id.search_btn);
        listOrder = new ArrayList<>();
        listDetailOrder = new ArrayList<>();
        historyProductAdapter = new HistoryProductAdapter();
        findOrder();

/*

         search by sdt
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textSearch = editText.getText().toString();
                if(textSearch != null && textSearch.length() > 0)
                {

                        List<Order> searchlistOrder = new ArrayList<>();
                        List<DetailOrder> searchlistDetailOrder = new ArrayList<>();
                        for (Order order : listOrder)
                        {
                            if(order.getCustPhone().equals(textSearch)) // affter date input
                            {
                                searchlistOrder.add(order);
                            }
                        }


                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DbOrder").child(AGConnectAuth.getInstance().getCurrentUser().getUid());

                        for (int i = 0; i<searchlistOrder.size(); i++){
                            Order order = searchlistOrder.get(i);
                            databaseReference.child(order.getOrderNo()).child("detail").addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataDetail : snapshot.getChildren()){
                                        DetailOrder detailOrder = dataDetail.getValue(DetailOrder.class);
                                        searchlistDetailOrder.add(detailOrder);
                                    }

                                    // set data HistoryProductAdapter
                                    if (searchlistDetailOrder.size() > 0){
                                        historyProductAdapter.setData(searchlistDetailOrder,searchlistOrder);
                                        historyProductAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(HistoryOrderActivity.this,"Không lấy được chi tiết đơn hàng từ firebase",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                } else {
                    historyProductAdapter.setData(listDetailOrder,listOrder);
                    historyProductAdapter.notifyDataSetChanged();
                }
            }
        });

         end search by sdt
*/


/*

         search by date greate and less than
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textSearch = editText.getText().toString();
                if(textSearch != null && textSearch.length() > 0)
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date dateConverted = dateFormat.parse(textSearch);
                        String dateStrConverted = dateFormat1.format(dateConverted);
                        List<Order> searchlistOrder = new ArrayList<>();
                        List<DetailOrder> searchlistDetailOrder = new ArrayList<>();
                        for (Order order : listOrder)
                        {
                            Date dateCompare = dateFormat1.parse(order.getDateOrder());
                            if(dateCompare.compareTo(dateConverted) > 0) // affter date input
                            {
                                searchlistOrder.add(order);
                            }
                            if(dateCompare.compareTo(dateConverted) < 0) // Before date input
                            {
                                searchlistOrder.add(order);
                            }
                        }


                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DbOrder").child(AGConnectAuth.getInstance().getCurrentUser().getUid());

                        for (int i = 0; i<searchlistOrder.size(); i++){
                            Order order = searchlistOrder.get(i);
                            databaseReference.child(order.getOrderNo()).child("detail").addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataDetail : snapshot.getChildren()){
                                        DetailOrder detailOrder = dataDetail.getValue(DetailOrder.class);
                                        searchlistDetailOrder.add(detailOrder);
                                    }

                                    // set data HistoryProductAdapter
                                    if (searchlistDetailOrder.size() > 0){
                                        historyProductAdapter.setData(searchlistDetailOrder,searchlistOrder);
                                        historyProductAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(HistoryOrderActivity.this,"Không lấy được chi tiết đơn hàng từ firebase",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    historyProductAdapter.setData(listDetailOrder,listOrder);
                    historyProductAdapter.notifyDataSetChanged();
                }
            }
        });

         end search by date greate and less than
*/


         /*search by date

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textSearch = editText.getText().toString();
                if(textSearch != null && textSearch.length() > 0)
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date dateConverted = dateFormat.parse(textSearch);
                        String dateStrConverted = dateFormat1.format(dateConverted);
                        List<Order> searchlistOrder = new ArrayList<>();
                        List<DetailOrder> searchlistDetailOrder = new ArrayList<>();
                            for (Order order : listOrder)
                            {
                                if(order.getDateOrder().equals(dateStrConverted))
                                {
                                    searchlistOrder.add(order);
                                }

                            }


                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DbOrder").child(AGConnectAuth.getInstance().getCurrentUser().getUid());

                        for (int i = 0; i<searchlistOrder.size(); i++){
                            Order order = searchlistOrder.get(i);
                            databaseReference.child(order.getOrderNo()).child("detail").addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataDetail : snapshot.getChildren()){
                                        DetailOrder detailOrder = dataDetail.getValue(DetailOrder.class);
                                        searchlistDetailOrder.add(detailOrder);
                                    }

                                    // set data HistoryProductAdapter
                                    if (searchlistDetailOrder.size() > 0){
                                        historyProductAdapter.setData(searchlistDetailOrder,searchlistOrder);
                                        historyProductAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(HistoryOrderActivity.this,"Không lấy được chi tiết đơn hàng từ firebase",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    historyProductAdapter.setData(listDetailOrder,listOrder);
                    historyProductAdapter.notifyDataSetChanged();
                }
            }
        });

         end search by date

          search by name product*/
/*

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textSearch = editText.getText().toString();
                if(textSearch != null && textSearch.length() > 0)
                {
                    List<DetailOrder> searchlistDetailOrder = new ArrayList<>();
                    for(DetailOrder detailOrder : listDetailOrder)
                    {
                        if(detailOrder.getProductName().toLowerCase().contains(textSearch.toLowerCase()))
                        {
                            searchlistDetailOrder.add(detailOrder);
                        }
                    }
                    historyProductAdapter.setData(searchlistDetailOrder,listOrder);
                    historyProductAdapter.notifyDataSetChanged();
                } else {
                    historyProductAdapter.setData(listDetailOrder,listOrder);
                    historyProductAdapter.notifyDataSetChanged();
                }
            }
        });
*/

         // end search by product

    }
    private void setDataHistoryProductAdapter(){

        historyProductAdapter.setData(listDetailOrder,listOrder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rcvHistory.setLayoutManager(linearLayoutManager);
        rcvHistory.setAdapter(historyProductAdapter);
    }
    private void findOrder(){

        // Clear các list dữ liệu khi tìm kiếm
        listOrder.clear();
        listDetailOrder.clear();

        // Kết nối tới data base
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DbOrder").child(AGConnectAuth.getInstance().getCurrentUser().getUid());

        // Lấy thông tin order
        myRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        historyProductAdapter.notifyDataSetChanged();
                        for (DataSnapshot dataOrder : snapshot.getChildren()){
                            Order order = dataOrder.getValue(Order.class);
                            order.setOrderNo(dataOrder.getKey());
                            listOrder.add(order);
                        }

                        if (listOrder.size() > 0){
                            // Lấy thông tin detail order
                            findDetailOrder(myRef);
                        }
                        else {
                            Toast.makeText(HistoryOrderActivity.this,"Không tìm thấy lịch sử đặt hàng",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HistoryOrderActivity.this,"Không lấy được thông tin đơn hàng từ firebase",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Lấy thông tin detail order
    private void findDetailOrder( DatabaseReference myRef){
        if (listOrder.size() > 0){
            /*Collections.sort(listOrder, ((o1, o2) -> {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dateConvert1 = dateFormat.parse(o1.getDateOrder());
                    Date dateConvert2 = dateFormat.parse(o2.getDateOrder());
                    return dateConvert2.compareTo(dateConvert1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }));*/
            for (int i = 0; i<listOrder.size(); i++){
                Order order = listOrder.get(i);
                myRef.child(order.getOrderNo()).child("detail").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataDetail : snapshot.getChildren()){
                            historyProductAdapter.notifyDataSetChanged();
                            DetailOrder detailOrder = dataDetail.getValue(DetailOrder.class);
                            listDetailOrder.add(detailOrder);
                        }

                        // set data HistoryProductAdapter
                        if (listDetailOrder.size() > 0){
                            /*Collections.sort(listDetailOrder, (do1, do2) -> {
                                return (do1.getNumProduct() < do2.getNumProduct()) ? 1 : -1;
                            });*/
                            /*Collections.sort(listDetailOrder, ((o1, o2) -> {
                                return o2.getProductName().compareTo(o1.getProductName());
                            }));*/
                            setDataHistoryProductAdapter();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HistoryOrderActivity.this,"Không lấy được chi tiết đơn hàng từ firebase",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}