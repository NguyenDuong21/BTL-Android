package com.example.MyBTL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyBTL.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ProfileActivity extends AppCompatActivity {
    TextView email, username;
    EditText editTextUserName, editTextPassword, editTextRepeatPassword;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private MaterialCardView logout;
    private ImageView userImg;
    private ImageView goBack;
    private StorageReference storageReference;
    private TextView payment_label;
    private MaterialCardView view_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AGConnectUser user = AGConnectAuth.getInstance().getCurrentUser();
        initElement();
        getUserProfile();
        payment_label = findViewById(R.id.payment_label);
        countOrder();
        logout = findViewById(R.id.logoutbn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FirebaseAuth.getInstance().signOut(); // logout firebase
                AGConnectAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        userImg = findViewById(R.id.user_img);
        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });
        goBack = findViewById(R.id.back);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view_history = findViewById(R.id.view_history);
        view_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, HistoryOrderActivity.class);
                startActivity(i);
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
                Uri imageUri = data.getData();
//                userImg.setImageURI(imageUri);
                UploadImageFireBase(imageUri);
            }
        }
    }

    private void UploadImageFireBase(Uri imageUri) {
        storageReference = FirebaseStorage.getInstance().getReference("ImageProfiles").child(AGConnectAuth.getInstance().getCurrentUser().getUid()+"jpg");
        StorageTask uploadTask = storageReference.putFile(imageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String uri = downloadUri.toString();
                    FirebaseDatabase.getInstance().getReference("Users").child(AGConnectAuth.getInstance().getCurrentUser().getUid())
                                                                        .child("imageProfile").setValue(uri);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    private void initElement()
    {
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        editTextUserName = findViewById(R.id.editTextUsername);
        /*editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);*/

    }
    private void getUserProfile()
    {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
        AGConnectUser currentUser = AGConnectAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            String id = currentUser.getUid();


            DatabaseReference fireRef = FirebaseDatabase.getInstance().getReference("Users").child(id);
            fireRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    email.setText(user.getEmail());
                    username.setText(user.getUserName());
                    editTextUserName.setText(user.getUserName());
                    Picasso.get().load(user.getImageProfile()).into(userImg);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }
    private void countOrder()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DbOrder").child(AGConnectAuth.getInstance().getCurrentUser().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int number_order = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    number_order++;
                }
                payment_label.setText(Integer.toString(number_order));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}