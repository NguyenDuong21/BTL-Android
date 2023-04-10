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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.MyBTL.Model.Category;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import org.jetbrains.annotations.NotNull;

public class add_category extends AppCompatActivity {
    ImageView andaidien;
    EditText tentheloai, tag;
    Uri imageCategory;
    Button add_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        init();
        andaidien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageCategory != null)
                {
                    DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Category");
                    String key = myref.push().getKey();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Categories").child(key+".jpg");
                    StorageTask task = storageReference.putFile(imageCategory);
                    Task<Uri> uriTask =task.continueWithTask(new Continuation() {
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

                                String namecate = tentheloai.getText().toString();
                                String tag_txt = tag.getText().toString();
//                                String tag_pro = tag.getText().toString();
                                String uri =downloadUri.toString();
                                DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Category");
                                Query query = myref.orderByChild("Tag").equalTo(tag_txt);
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if(snapshot.exists())
                                        {
                                            Toast.makeText(add_category.this, "Tag đã tồn tại, xin hãy nhập tag khác", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Category category = new Category(namecate, uri, tag_txt);
                                            myref.child(key).setValue(category).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(add_category.this, "Thêm thể loại thành công", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            } else {
                                task.getException();
                            }
                        }
                    });
                }
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
                imageCategory = data.getData();
                andaidien.setImageURI(imageCategory);
            }
        }
    }

    private void init()
    {
        andaidien = findViewById(R.id.anddaidien);
        tentheloai = findViewById(R.id.tentheloai);
        tag = findViewById(R.id.editTextTextPersonName7);
        add_btn = findViewById(R.id.btnThemCate);
    }
}