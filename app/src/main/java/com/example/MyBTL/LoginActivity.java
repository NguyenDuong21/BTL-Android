package com.example.MyBTL;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    FirebaseAuth mAuth;
//    ImageView loginFB, loginGG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AGConnectUser user = AGConnectAuth.getInstance().getCurrentUser();
//        loginFB = findViewById(R.id.loginFB);
//        loginGG = findViewById(R.id.loginGG);
        if(user != null)
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();
        TextView textView = findViewById(R.id.createAccount);
//        loginFB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginwithFB();
//            }
//        });
//        loginGG.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginwithGG();
//            }
//        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        ImageView btnLogin = findViewById(R.id.loginBtn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAuth();
            }
        });
    }
//    private void loginwithGG()
//    {
//        AGConnectAuth.getInstance().signIn(this,AGConnectAuthCredential.Google_Provider).addOnSuccessListener(new OnSuccessListener<SignInResult>() {
//            @Override
//            public void onSuccess(SignInResult signInResult) {
//                // onSuccess
//                AGConnectUser user =  signInResult.getUser();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(Exception e) {
//                        // onFail
//                        Toast.makeText(LoginActivity.this, e.getMessage(),
//                                Toast.LENGTH_SHORT).show();
//                        Log.e("Error", e.getMessage());
//                    }
//                });
//    }
//    private void loginwithFB()
//    {
//        AGConnectAuth.getInstance().signIn(this,AGConnectAuthCredential.Facebook_Provider).addOnSuccessListener(new OnSuccessListener<SignInResult>() {
//            @Override
//            public void onSuccess(SignInResult signInResult) {
//                // onSuccess
//                AGConnectUser user =  signInResult.getUser();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(Exception e) {
//                        // onFail
//                        Toast.makeText(LoginActivity.this, e.getMessage(),
//                                Toast.LENGTH_SHORT).show();
//                        Log.e("Error", e.getMessage());
//                    }
//                });
//    }
    private void loginUserAuth()
    {
        String txtEmail = email.getText().toString();
        String txtPassword = password.getText().toString();
        AGConnectAuthCredential credential = EmailAuthProvider.credentialWithPassword(txtEmail, txtPassword);
        AGConnectAuth.getInstance().signIn(credential)
                .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                    @Override
                    public void onSuccess(SignInResult signInResult) {
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Đăng nhập thành công")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.setTitle("Đăng nhập thành công");
                        dialog.show();*/

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        Log.e("Error", e.getMessage());
                    }
                });
    }
    private void loginUser() {
        String txtEmail = email.getText().toString();
        String txtPassword = password.getText().toString();
        if(!txtEmail.isEmpty() && !txtPassword.isEmpty())
        {
            mAuth.signInWithEmailAndPassword(txtEmail, txtPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Đăng nhập thành công")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setTitle("Đăng nhập thành công");
                                alertDialog.show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

}