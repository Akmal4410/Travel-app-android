package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
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

public class placerating extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

Spinner place;
RatingBar rating;
Button rate;

ArrayList<String> places,place_id;
String url, ip,ratingval,pid="";
SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placerating);

        place=findViewById(R.id.spinner);
        rating=findViewById(R.id.ratingBar);
        rate=findViewById(R.id.rate);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("ip","192.168.43.65");
        url="http://"+sh.getString("ip","")+":5000/viewplace";



        RequestQueue queue = Volley.newRequestQueue(placerating.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    places= new ArrayList<>();
                    place_id= new ArrayList<>();



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        places.add(jo.getString("place"));
                        place_id.add(jo.getString("place_id"));




                    }

                     ArrayAdapter<String> ad=new ArrayAdapter<String>(placerating.this,android.R.layout.simple_list_item_1,places);
                    place.setAdapter(ad);
                    place.setOnItemSelectedListener(placerating.this);

//                    feedback.setAdapter(new Custom3(placerating.this,user_name,feedbacks,date));
//                    comments.setOnItemClickListener(View_Comment.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(placerating.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();



                return params;
            }
        };
        queue.add(stringRequest);





        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ratingval=rating.getRating()+"";

                sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                ip=sh.getString("ip","192.168.43.65");
                url="http://"+sh.getString("ip","")+":5000/addratingtoplace";

                RequestQueue queue = Volley.newRequestQueue(placerating.this);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.

                        try {
                            JSONObject jo = new JSONObject(response);
                            String status = jo.getString("task");
                            Toast.makeText(placerating.this, status, Toast.LENGTH_SHORT).show();

                            if (status.equalsIgnoreCase("success")) {


                                Toast.makeText(placerating.this, status, Toast.LENGTH_SHORT).show();

                                Intent in = new Intent(getApplicationContext(), userhome.class);
                                startActivity(in);
//



                            } else  {
                                Toast.makeText(placerating.this, "Rating is not successful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                            Toast.makeText(placerating.this, ""+e, Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(placerating.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();

                        params.put("place_id",pid);
                        params.put("rating",ratingval);
                        params.put("user_id", sh.getString("lid",""));

                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pid=place_id.get(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}