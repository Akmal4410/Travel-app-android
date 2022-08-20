package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.ImageView;
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

public class book_room extends AppCompatActivity {

EditText date;
ImageView img;
TextView roomdetails,price,ac,bedtype;
String roomdetails1,price1,ac1,bedtype1,room_id,date1;
Button book,cancel;
    String url="";
    String ip="";
    SharedPreferences sh;
    DatePickerDialog datepicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);

        roomdetails=findViewById(R.id.roomdetails);
        img=findViewById(R.id.imageView3);
        price=findViewById(R.id.price);
        ac=findViewById(R.id.ac);
        bedtype=findViewById(R.id.bed);
        book=findViewById(R.id.book);
        cancel=findViewById(R.id.cancel);
        date=findViewById(R.id.date);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        date.setInputType(InputType.TYPE_NULL);

        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        java.net.URL thumb_u;
        try {

            //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");

            thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000/static/image/"+getIntent().getStringExtra("phto"));
            Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
            img.setImageDrawable(thumb_d);

        }
        catch (Exception e)
        {
            Log.d("errsssssssssssss",""+e);
        }
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                datepicker = new DatePickerDialog(book_room.this,
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

        roomdetails.setText(getIntent().getStringExtra("room_details"));
        price.setText(getIntent().getStringExtra("price"));
        ac.setText(getIntent().getStringExtra("ac_type"));
        bedtype.setText(getIntent().getStringExtra("bed_type"));
        date.setText(getIntent().getStringExtra("date"));
        room_id=getIntent().getStringExtra("roomid");


        ip=sh.getString("ip","192.168.43.65");
        url="http://"+sh.getString("ip","")+":5000/roombooking";

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomdetails1=roomdetails.getText().toString();
                price1=price.getText().toString();
                ac1=ac.getText().toString();
                bedtype1=bedtype.getText().toString();
                date1=date.getText().toString();

                if(date1.equalsIgnoreCase(""))
                {
                    date.setError("****");
                }
                else {

                    RequestQueue queue = Volley.newRequestQueue(book_room.this);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.

                            try {
                                JSONObject jo = new JSONObject(response);
                                String status = jo.getString("task");
                                Toast.makeText(book_room.this, status, Toast.LENGTH_SHORT).show();

                                if (status.equalsIgnoreCase("success")) {


                                    Toast.makeText(book_room.this, status, Toast.LENGTH_SHORT).show();

                                    Intent in = new Intent(getApplicationContext(), userhome.class);
                                    startActivity(in);
//


                                } else {
                                    Toast.makeText(book_room.this, "Booking not successful", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                                Toast.makeText(book_room.this, "" + e, Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(book_room.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            params.put("roomid", room_id);
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
                startActivity(new Intent(getApplicationContext(),view_room_details.class));
            }
        });
    }
}