package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView textView = findViewById(R.id.redirectLogin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        ImageView registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser()
    {
        EditText txtEmail = findViewById(R.id.editTextMobile);
        String email = txtEmail.getText().toString().trim();
        EditText txtUserName = findViewById(R.id.editTextEmail);
        String userName = txtUserName.getText().toString().trim();
        EditText txtPassword = findViewById(R.id.editTextPassword);
        String password = txtPassword.getText().toString().trim();
        if(email.isEmpty())
        {
            Toast.makeText(this,"Email invalid", Toast.LENGTH_SHORT).show();
        } else if(userName.isEmpty())
        {
            Toast.makeText(this,"UserName invalid", Toast.LENGTH_SHORT).show();
        } else if(password.isEmpty())
        {
            Toast.makeText(this,"Password invalid", Toast.LENGTH_SHORT).show();
        } else {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                User user = new User(email, userName);
                                FirebaseDatabase.getInstance().getReference("Users").child(task.getResult().getUser().getUid())
                                        .setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(RegisterActivity.this,"Register faild", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
//        mAuth.createUserWithEmailAndPassword()
    }
}