package com.example.MyBTL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyBTL.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.EmailUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.TaskExecutors;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button getCode;
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
                registerWithEmail();
            }
        });
        getCode = findViewById(R.id.getCode);
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtEmail = findViewById(R.id.editTextMobile);
                String email = txtEmail.getText().toString().trim();
                EditText txtUserName = findViewById(R.id.editTextEmail);
                String userName = txtUserName.getText().toString().trim();
                VerifyCodeSettings settings = new VerifyCodeSettings.Builder()
                        .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                        .sendInterval(30)
                        .locale(Locale.CHINA)
                        .build();
                com.huawei.hmf.tasks.Task<VerifyCodeResult> task= AGConnectAuth.getInstance().requestVerifyCode(email, settings);
                task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                    @Override
                    public void onSuccess(VerifyCodeResult verifyCodeResult) {
                        // The verification code request is successful.

                    }
                }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                    }
                });
            }
        });
    }
    private void registerWithEmail()
    {
        EditText txtEmail = findViewById(R.id.editTextMobile);
        String email = txtEmail.getText().toString().trim();
        EditText txtUserName = findViewById(R.id.editTextEmail);
        String userName = txtUserName.getText().toString().trim();
        EditText txtPassword = findViewById(R.id.editTextPassword);
        String password = txtPassword.getText().toString().trim();
        EditText editTextcode = findViewById(R.id.editTextcode);
        String code = editTextcode.getText().toString().trim();
        EmailUser emailUser = new EmailUser.Builder()
                .setEmail(email)
                .setVerifyCode(code)
                .setPassword(password) //optional
                .build();
        AGConnectAuth.getInstance().createUser(emailUser)
                .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                    @Override
                    public void onSuccess(SignInResult signInResult) {
                        AGConnectUser curentU= signInResult.getUser();
                        User user = new User(email, userName);
                        FirebaseDatabase.getInstance().getReference("Users").child(curentU.getUid())
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
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
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