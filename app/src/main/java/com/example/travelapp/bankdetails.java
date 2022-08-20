package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class bankdetails extends AppCompatActivity {
    EditText bankname,ifsc,key,acno;
    TextView amount;
    Button pay;
    String bankname1,ifsc1,key1,acno1;
    String url="";
    String ip="";
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankdetails);

        bankname = findViewById(R.id.bname);
        ifsc = findViewById(R.id.ifsc);
        key = findViewById(R.id.key);
        acno = findViewById(R.id.acno);
        amount = findViewById(R.id.amount);
        pay = findViewById(R.id.button10);


        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        amount.setText(sh.getString("price",""));
        ip=sh.getString("ip","192.168.43.65");


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankname1=bankname.getText().toString();
                ifsc1=ifsc.getText().toString();
                key1=key.getText().toString();
                acno1=acno.getText().toString();
                if(bankname1.equalsIgnoreCase(""))
                {
                    bankname.setError("Enter Your Bank Name");
                }
                else if (ifsc1.equalsIgnoreCase(""))
                {
                    ifsc.setError("Enter your IFSC code");
                }
                else if (key1.equalsIgnoreCase(""))
                {
                    key.setError("Enter Your Key");
                }
                else if (acno1.equalsIgnoreCase(""))
                {
                    acno.setError("Enter Your Account Number");
                }
                else {


                    if (sh.getString("type", "").equalsIgnoreCase("room")) {


                        url = "http://" + sh.getString("ip", "") + ":5000/bankdetails1";

                        RequestQueue queue = Volley.newRequestQueue(bankdetails.this);

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.

                                try {
                                    JSONObject jo = new JSONObject(response);
                                    String status = jo.getString("task");
                                    Toast.makeText(bankdetails.this, status, Toast.LENGTH_SHORT).show();

                                    if (status.equalsIgnoreCase("invalid")) {

                                        Toast.makeText(bankdetails.this, "invalid Account", Toast.LENGTH_SHORT).show();

                                        Intent
                                                in = new Intent(getApplicationContext(), bankdetails.class);
                                        startActivity(in);
                                    } else if (status.equalsIgnoreCase("insufficent")) {

                                        Toast.makeText(bankdetails.this, "Insufficent Amount", Toast.LENGTH_SHORT).show();

                                        Intent in = new Intent(getApplicationContext(), bankdetails.class);
                                        startActivity(in);

                                    } else if (status.equalsIgnoreCase("success")) {

                                        Toast.makeText(bankdetails.this, "Payment Success", Toast.LENGTH_SHORT).show();

                                        Intent in = new Intent(getApplicationContext(), userhome.class);
                                        startActivity(in);

                                    } else {
                                        Toast.makeText(bankdetails.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Log.d("=========", e.toString());
                                    Toast.makeText(bankdetails.this, "" + e, Toast.LENGTH_SHORT).show();

                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(bankdetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("lid", sh.getString("lid", ""));
                                params.put("bankname", bankname1);
                                params.put("ifsc", ifsc1);
                                params.put("key", key1);
                                params.put("acno", acno1);
                                params.put("rid", sh.getString("rbid", ""));


                                return params;
                            }
                        };
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);

                    } else if (sh.getString("type", "").equalsIgnoreCase("package")) {
                        url = "http://" + sh.getString("ip", "") + ":5000/bankdetails";
                        RequestQueue queue = Volley.newRequestQueue(bankdetails.this);

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.

                                try {
                                    JSONObject jo = new JSONObject(response);
                                    String status = jo.getString("task");
                                    Toast.makeText(bankdetails.this, status, Toast.LENGTH_SHORT).show();

                                    if (status.equalsIgnoreCase("invalid")) {

                                        Toast.makeText(bankdetails.this, "invalid Account", Toast.LENGTH_SHORT).show();

                                        Intent
                                                in = new Intent(getApplicationContext(), bankdetails.class);
                                        startActivity(in);
                                    } else if (status.equalsIgnoreCase("insufficent")) {

                                        Toast.makeText(bankdetails.this, "Insufficent Amount", Toast.LENGTH_SHORT).show();

                                        Intent in = new Intent(getApplicationContext(), bankdetails.class);
                                        startActivity(in);

                                    } else if (status.equalsIgnoreCase("success")) {

                                        Toast.makeText(bankdetails.this, "Payment Success", Toast.LENGTH_SHORT).show();

                                        Intent in = new Intent(getApplicationContext(), userhome.class);
                                        startActivity(in);

                                    } else {
                                        Toast.makeText(bankdetails.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Log.d("=========", e.toString());
                                    Toast.makeText(bankdetails.this, "" + e, Toast.LENGTH_SHORT).show();

                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(bankdetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("lid", sh.getString("lid", ""));
                                params.put("bankname", bankname1);
                                params.put("ifsc", ifsc1);
                                params.put("key", key1);
                                params.put("acno", acno1);
                                params.put("pkid", sh.getString("pkid", ""));


                                return params;
                            }
                        };
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);


                    }

                }

            }
        });



    }

    private void pay(String url) {

    }
}