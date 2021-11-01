package com.example.MyBTL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boolean handler = new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 3000);
    }

    private void nextActivity() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        String username = firebaseUser.getDisplayName();
//        String email = firebaseUser.getEmail();
//        String id = firebaseUser.getUid();
//        List<User> users = new ArrayList<>();
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users/"+id);
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                User user = snapshot.getValue(User.class);
//                users.add(user);
//                Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
//                intent.putExtra("size", user.getEmail());
//                startActivity(intent);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//        mDatabase.child(id).child("email").setValue("emailchangelan50@gmail.com")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<Void> task) {
//                        Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
//
//                        startActivity(intent);
//                    }
//                });
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren())
//                {
//                    User user = dataSnapshot.getValue(User.class);
//                    users.add(user);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//                String name = "abc";
//            }
//        });
        if(firebaseUser != null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }

}