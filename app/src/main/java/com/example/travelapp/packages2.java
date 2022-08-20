package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class packages2 extends AppCompatActivity {

EditText date;
TextView packageDetails,food,rate;
Button book,cancel;
String packages_id,ip,url,date1;
SharedPreferences sh;
    DatePickerDialog datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages2);

        packageDetails=findViewById(R.id.details);
        food=findViewById(R.id.food);
        rate=findViewById(R.id.rate);
        book=findViewById(R.id.book);
        cancel=findViewById(R.id.cancel);
        date=findViewById(R.id.date);

        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                datepicker = new DatePickerDialog(packages2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                datepicker.getDatePicker().setMinDate(System.currentTimeMillis());

                datepicker.show();
            }
        });

        packageDetails.setText(getIntent().getStringExtra("Package_details"));
        food.setText(getIntent().getStringExtra("food_availability"));
        rate.setText(getIntent().getStringExtra("rate"));
        date.setText(getIntent().getStringExtra("date"));
        packages_id=getIntent().getStringExtra("packages_id");

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("ip","192.168.43.65");
        url="http://"+sh.getString("ip","")+":5000/packagebooking";




        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date1=date.getText().toString();
                if(date1.equalsIgnoreCase(""))
                {
                    date.setError("**");
                }
                else {
                    RequestQueue queue = Volley.newRequestQueue(packages2.this);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.

                            try {
                                JSONObject jo = new JSONObject(response);
                                String status = jo.getString("task");
                                Toast.makeText(packages2.this, status, Toast.LENGTH_SHORT).show();

                                if (status.equalsIgnoreCase("success")) {


                                    Toast.makeText(packages2.this, status, Toast.LENGTH_SHORT).show();

                                    Intent in = new Intent(getApplicationContext(), userhome.class);
                                    startActivity(in);
//


                                } else {
                                    Toast.makeText(packages2.this, "Booking not successful", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                                Toast.makeText(packages2.this, "" + e, Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(packages2.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            params.put("packageid", packages_id);
                            params.put("date", date1);
                            params.put("userid", sh.getString("lid", ""));

                            return params;
                        }
                    };
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),packages1.class));
            }
        });

    }
}