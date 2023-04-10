package com.example.MyBTL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyBTL.Model.Category;
import com.example.MyBTL.Model.Product;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class add_product extends AppCompatActivity {
    private EditText tensanpham, gia, mieuta;
    private Spinner theloai;
    private ImageView anhdaidien;
    private Button add;
    Uri image;
    private ImageView back2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        init();
        fillCategory();
        anhdaidien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image != null)
                {
                    String tensp = tensanpham.getText().toString();
                    int giatxt = Integer.parseInt(gia.getText().toString());
                    String mieutatxt = mieuta.getText().toString();
                    String theloaitxt = ((Category) theloai.getSelectedItem()).getTag();

                    DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Products");
                    String key = myref.push().getKey();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Products").child(key+".jpg");
                    StorageTask task = storageReference.putFile(image);
                    Task<Uri> uriTask = task.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull @NotNull Task task) throws Exception {
                            if(!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task task) {
                            if(task.isSuccessful())
                            {
                                Uri downloadUri = (Uri) task.getResult();

                                Product addProduct = new Product(tensp, theloaitxt, mieutatxt, giatxt, key);
                                addProduct.setImage(downloadUri.toString());
                                myref.child(key).setValue(addProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(add_product.this, "Thêm thành công", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });


                }

            }
        });
        back2 = findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(add_product.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                image = data.getData();
                anhdaidien.setImageURI(image);
            }
        }
    }

    private void init()
    {
        tensanpham = findViewById(R.id.editTextTextPersonName6);
        gia = findViewById(R.id.editTextTextPersonName2);
        mieuta = findViewById(R.id.editTextTextPersonName5);
        theloai = findViewById(R.id.spinner);
        anhdaidien = findViewById(R.id.imageView6);
        add = findViewById(R.id.add);

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
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(add_product.this, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                theloai.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}