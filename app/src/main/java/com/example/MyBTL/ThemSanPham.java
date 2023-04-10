package com.example.MyBTL;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.MyBTL.Adapter.UploadRecyclerAdaptor;
import com.example.MyBTL.Model.Category;
import com.example.MyBTL.Model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThemSanPham extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    CardView selectImg;
    ArrayList<Uri> list;
    UploadRecyclerAdaptor adaptor;
    List<String> urlStrings;
    ImageView selectedImg;
    Button btn_them_sp;
    Dialog processDialog;
    private Spinner theloai;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Products");
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK
                        && result.getData() != null) {
                    Uri photoUri = result.getData().getData();
                    selectedImg.setImageURI(photoUri);
                }
            }
    );

    public void launch_func(ImageView imgView) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
        selectedImg = imgView;
    }

    String colum[]={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);
        list=new ArrayList<>();
        recyclerView=findViewById(R.id.recycler);
        selectImg=findViewById(R.id.selectImg);
        theloai = findViewById(R.id.spinner);
        urlStrings = new ArrayList<>();
        processDialog = new Dialog(this);
        processDialog.setContentView(R.layout.custom_dialog_process);
        processDialog.setCancelable(false);
        btn_them_sp = findViewById(R.id.btn_them_sp);
        btn_them_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processDialog.show();
                for(int upload_count = 0; upload_count < list.size(); upload_count++){
                    Uri IndividualFile = list.get(upload_count);
                    final StorageReference ImageName = storageReference.child("Product"+IndividualFile.getLastPathSegment());
                    byte[] bytes = new byte[0];
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), IndividualFile);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                        bytes = byteArrayOutputStream.toByteArray();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageName.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlStrings.add(String.valueOf(uri));
                                    if (urlStrings.size() == list.size()){
                                        StoreLink(urlStrings);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        fillCategory();
        adaptor=new UploadRecyclerAdaptor(this, list);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(adaptor);
        selectImg.setOnClickListener(this);

        if((ActivityCompat.checkSelfPermission(
                this,colum[0])!= PackageManager.PERMISSION_GRANTED)&&
                (ActivityCompat.checkSelfPermission(
                        this,colum[1])!= PackageManager.PERMISSION_GRANTED)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(colum,123);
            }
        }
    }

    private void StoreLink(List<String> urls) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UserOne");
            String key = databaseReference.push().getKey();
            Product addProduct = new Product("tensp", "theloaitxt", "mieutatxt", 10, key);
            addProduct.setImages(urls);
            databaseReference.child(key).setValue(addProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        processDialog.dismiss();
                        list.clear();
                        adaptor.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.selectImg:
                openGalley();
                break;
        }
    }

    private void fillCategory()
    {
        List<Category> list = new ArrayList<>();
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Category");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Category category = dataSnapshot.getValue(Category.class);
                    list.add(category);
                }
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(ThemSanPham.this, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                theloai.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void openGalley() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selcet Picture"),123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123 && resultCode==RESULT_OK){
            if(data.getClipData()!=null){
                int x=data.getClipData().getItemCount();
                for(int i=0;i<x;i++){
                    list.add(data.getClipData().getItemAt(i).getUri());
                }
                adaptor.notifyDataSetChanged();
            }else if(data.getData()!=null){
                String imgurl=data.getData().getPath();
                list.add(Uri.parse(imgurl));
            }
        }
    }

}