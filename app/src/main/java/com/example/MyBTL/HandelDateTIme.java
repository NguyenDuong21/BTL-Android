package com.example.MyBTL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HandelDateTIme extends AppCompatActivity {
    TextView showDateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handel_date_time);
        showDateTime = findViewById(R.id.textView13);
        // using date class
        Date curentdate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); // format date.
        String dateformated = simpleDateFormat.format(curentdate);
        /*showDateTime.setText(dateformated);*/
        // end using date class

        Calendar now = Calendar.getInstance();
        //
        System.out.println("Current Year is : " + now.get(Calendar.YEAR));
        // month start from 0 to 11
        System.out.println("Current Month is : " + (now.get(Calendar.MONTH) + 1));
        System.out.println("Current Date is : " + now.get(Calendar.DATE));

        /*indexOf() and lastIndexOf()*/


        // using Calendar class
        Calendar calendar = Calendar.getInstance();
        Date curentDateCalendar = calendar.getTime();
        String curentdateformat = simpleDateFormat.format(curentDateCalendar);

        // add day month year
        calendar.add(Calendar.DATE, 2);
        calendar.add(Calendar.MONTH, 2);
        calendar.add(Calendar.YEAR, 2);
        Date futureDateCalendar = calendar.getTime();

        /*showDateTime.setText(simpleDateFormat.format(futureDateCalendar));*/
        // end using Calendar class

        // convert string to date time
        String myDateString = "25/05/2011";
        try {
            Date date_converted = simpleDateFormat.parse(myDateString);
            showDateTime.setText(simpleDateFormat.format(date_converted));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // end convert
    }
}