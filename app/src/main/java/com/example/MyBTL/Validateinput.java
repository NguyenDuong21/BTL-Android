package com.example.MyBTL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validateinput extends AppCompatActivity {
    EditText editTextTextPersonName;
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validateinput);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*checkContain(editTextTextPersonName.getText().toString());*/

                /*String str = "abcdefghijklmnopqrtuvwxyz";
                String strOut = str.substring(0,7);*/

                if(checkEmailValid(editTextTextPersonName.getText().toString()))
                {
                    Toast.makeText(Validateinput.this, "Is email address", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Validateinput.this, "Isn't email address", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private Boolean checkContain(String txtCheck)
    {



        boolean isContainNumber = false;
        boolean isContainLowerCase = false;
        boolean isContainUpperCase = false;
        for(int i=0; i<txtCheck.length() ; i++)
        {
            char ch = txtCheck.charAt(i);
            if(Character.isDigit(ch))
            {
                Toast.makeText(Validateinput.this, "Contain digit", Toast.LENGTH_LONG).show();
            } else if (Character.isLowerCase(ch))
            {
                Toast.makeText(Validateinput.this, "Contain lowerCase", Toast.LENGTH_LONG).show();
            } else if(Character.isUpperCase(ch))
            {
                Toast.makeText(Validateinput.this, "Contain uppercase", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }
    private boolean checkEmailValid(String inputVal)
    {
        Pattern pattern = Pattern.compile("\\w+@\\w+(\\.\\w+)+");
        Matcher matcher = pattern.matcher(inputVal);
        return matcher.matches();
    }
}