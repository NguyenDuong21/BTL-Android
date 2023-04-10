package com.example.MyBTL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyBTL.Model.DetailOrder;
import com.example.MyBTL.Model.Product;
import com.example.MyBTL.Model.ProductCart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.HWLocation;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {
    TextView tv_cart_total_price;
    Button btn_cart_order;
    List<ProductCart> mExampleList;
    float total_Price = 0;
    private DecimalFormat format;
    private ImageView back;
    private EditText edt_cart_cust_name,edt_cart_cust_phone,edt_cart_cust_address;
    private Button getLocation;
    SettingsClient settingsClient;
    LocationRequest mLocationRequest;
    private EditText edt_cart_cust_lat;
    private Double la,lo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        grantPermission();
        /*getLocation();*/
        String address = getCompleteAddressString(1.3350799637580424,103.96510928086315);
        String latitude1 = "1.3350799637580424";
        String longitude1 = "103.96510928086315";
        tv_cart_total_price = findViewById(R.id.tv_cart_total_price);
        btn_cart_order = findViewById(R.id.btn_cart_order);
        edt_cart_cust_name = findViewById(R.id.edt_cart_cust_name);
        edt_cart_cust_phone = findViewById(R.id.edt_cart_cust_phone);
        edt_cart_cust_address = findViewById(R.id.edt_cart_cust_address);
        back = findViewById(R.id.back);
//        edt_cart_cust_lat = findViewById(R.id.edt_cart_cust_lat);

//        getLocation = findViewById(R.id.getLocation);
//        getLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String address = null;
//                    /*address = getCompleteAddressString(1.3350799637580424,103.96510928086315);
//                    edt_cart_cust_address.setText(address);*/
//                Toast.makeText(OrderActivity.this, "Kinh do: "+ latitude1+", Vi do "+ longitude1, Toast.LENGTH_LONG).show();
//                edt_cart_cust_lat.setText(latitude1 + " - " + longitude1);
//                FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(OrderActivity.this);
//                SettingsClient settingsClient = LocationServices.getSettingsClient(OrderActivity.this);
//                LocationRequest mLocationRequest = new LocationRequest();
//                mLocationRequest.setInterval(10000);
//                mLocationRequest.setNeedAddress(true);
//                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                fusedLocationProviderClient.getLastLocationWithAddress(mLocationRequest)
//                        .addOnSuccessListener(new com.huawei.hmf.tasks.OnSuccessListener<HWLocation>() {
//                            @SuppressLint("SetTextI18n")
//                            @Override
//                            public void onSuccess(HWLocation hwLocation) {
//                                /*System.out.println("CITY >>> " + hwLocation.getCity());*/
//                                Double latitude = hwLocation.getLatitude();
//                                Double longitude = hwLocation.getLongitude();
//                                Toast.makeText(OrderActivity.this, "Kinh do: "+ latitude+", Vi do "+ longitude, Toast.LENGTH_LONG).show();
//                                edt_cart_cust_lat.setText(latitude + " - " + longitude);
//                            }
//                        })
//                        .addOnFailureListener(new com.huawei.hmf.tasks.OnFailureListener() {
//                            @Override
//                            public void onFailure(Exception e) {
//
//                            }
//                        });
//            }
//        });
        btn_cart_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataOrder();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        format = new DecimalFormat("###,###,###");
        SharedPreferences sharedPreferences =  getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String uid= AGConnectAuth.getInstance().getCurrentUser().getUid();
        String json = sharedPreferences.getString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), null);
        Type type = new TypeToken<ArrayList<ProductCart>>() {}.getType();
        mExampleList = gson.fromJson(json, type);

        if (mExampleList != null) {

            for(ProductCart productCart : mExampleList)
            {
                total_Price += productCart.getAmount() * productCart.getPrices();
            }
            String total = format.format(total_Price);
            tv_cart_total_price.setText(total);
        }

    }
    private void getLocation()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        mLocationRequest = new LocationRequest();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
// Check the device location settings.
        settingsClient = LocationServices.getSettingsClient(OrderActivity.this);
        settingsClient.checkLocationSettings(locationSettingsRequest)
// Define callback for success in checking the device location settings.
                .addOnSuccessListener(new com.huawei.hmf.tasks.OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        LocationRequest mLocationRequest = new LocationRequest();
// Set the location update interval (in milliseconds).
                        mLocationRequest.setInterval(10000);
// Set the location type.
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        LocationCallback mLocationCallback;
                        mLocationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult != null) {
// Process the location callback result.
                                    Location lastLocation = locationResult.getLastLocation();
                                    la = lastLocation.getLatitude();
                                    lo = lastLocation.getLongitude();
                                    /*String str="Location Updates:\n\nLatitude: "+
                                            lastLocation.getLatitude()+"\n"+
                                            "Longitude: "+lastLocation.getLongitude()+ "\n"+
                                            "Time:"+lastLocation.getTime();
                                    String strAdd = "";
                                    Geocoder geocoder = new Geocoder(OrderActivity.this, Locale.getDefault());
                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(la, lo, 1);
                                        if (addresses != null) {
                                            Address returnedAddress = addresses.get(0);
                                            StringBuilder strReturnedAddress = new StringBuilder("");

                                            for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                                                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                                            }
                                            strAdd = strReturnedAddress.toString();
                                            Log.w("My Current loction", strReturnedAddress.toString());
                                        } else {
                                            Log.w("My Current loction", "No Address returned!");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.w("My Current loction", "Canont get Address!");
                                    }
                                    String addressStringddress = la + " Location test" + lo + strAdd;
                                    Toast.makeText(OrderActivity.this,addressStringddress,Toast.LENGTH_LONG).show();
                                    textView9.setText(str);*/
                                }
                            }
                        };
                        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(OrderActivity.this);
                        fusedLocationProviderClient
                                .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                                .addOnSuccessListener(new com.huawei.hmf.tasks.OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
// Processing when the API call is successful.
//                                        Toast.makeText(OrderActivity.this,"requestLocationUpdatesSuccess",Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new com.huawei.hmf.tasks.OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
// Processing when the API call fails.
//                                        Toast.makeText(OrderActivity.this,"requestLocationUpdatesfailed",Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                })
// Define callback for failure in checking the device location settings.
                .addOnFailureListener(new com.huawei.hmf.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
// Device location settings do not meet the requirements.
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
// Call startResolutionForResult to display a pop-up asking the user to enable related permission.
                                    rae.startResolutionForResult(OrderActivity.this, 0);
                                } catch (IntentSender.SendIntentException sie) {
// ...
                                }
                                break;
                        }
                    }
                });
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction", "Canont get Address!");
        }
        return strAdd;
    }
    private void    grantPermission(){
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i("TAG", "sdk < 28 Q");
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] strings =
                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                String[] strings = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"};
                ActivityCompat.requestPermissions(this, strings, 2);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "onRequestPermissionsResult: apply LOCATION PERMISSION successful");
            } else {
                Log.i("TAG", "onRequestPermissionsResult: apply LOCATION PERMISSSION  failed");
            }
        }

        if (requestCode == 2) {
            if (grantResults.length > 2 && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION successful");
            } else {
                Log.i("TAG", "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION  failed");
            }
        }
    }

    private void addDataOrder(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DbOrder").child(AGConnectAuth.getInstance().getCurrentUser().getUid());

        Map<String,Object> map = new HashMap<>();

        // Lấy ngày order = now
        Date date = new Date(System.currentTimeMillis());
        map.put("dateOrder", date.toString());
        map.put("custName",edt_cart_cust_name.getText().toString());
        map.put("custAddress",edt_cart_cust_address.getText().toString());
        map.put("custPhone",edt_cart_cust_phone.getText().toString());

        int num = 0;

        map.put("numProduct",mExampleList.size());
        map.put("totalPrice",total_Price);
        map.put("status","Đang chờ xác nhận");

        // Add thông tin order
        String odrKey = myRef.push().getKey();
        myRef.child(odrKey).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                List<DetailOrder> listDetailOrder = makeDetailOrder(odrKey);

                // Add thông tin detail order
                for (DetailOrder detailOrder : listDetailOrder){

                    myRef.child(odrKey).child("detail").child(myRef.push().getKey())
                            .setValue(detailOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(OrderActivity.this,"Đã đăng ký đơn hàng",Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences =  getSharedPreferences("shared preferences", MODE_PRIVATE);
                            Gson gson = new Gson();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("listcart"+ AGConnectAuth.getInstance().getCurrentUser().getUid(), null);
                            editor.apply();
                        }
                    });

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                        builder.setMessage("Đăng ký đơn hàng")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.setTitle("Đăng ký đơn hàng thành công");
                        dialog.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderActivity.this,"Đăng ký đơn hàng thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<DetailOrder> makeDetailOrder( String odrNo){
        List<DetailOrder> listDetailOrder = new ArrayList<>();
        for (ProductCart productCart : mExampleList){
            DetailOrder detailOrder = new DetailOrder();
            detailOrder.setOrderNo(odrNo);
            detailOrder.setProductName(productCart.getName());
            detailOrder.setProductPrice(productCart.getPrices());
            detailOrder.setUrlImg(productCart.getImage());
            detailOrder.setNumProduct(productCart.getAmount());
            detailOrder.setStatus("Đang chờ xác nhận");
            detailOrder.setTag(productCart.gettag());
            listDetailOrder.add(detailOrder);
        }
        return listDetailOrder;
    }
}