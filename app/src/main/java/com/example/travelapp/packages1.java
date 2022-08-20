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
import android.widget.ListView;

import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class packages1 extends AppCompatActivity implements AdapterView.OnItemClickListener {
ListView packages;
    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> package_details,food_availability,rate,packages_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages1);

        packages=findViewById(R.id.viewpackages);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url ="http://"+sp.getString("ip", "") + ":5000/viewpackage";

        RequestQueue queue = Volley.newRequestQueue(packages1.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    package_details= new ArrayList<>();
                    food_availability= new ArrayList<>();
                    rate=new ArrayList<>();
                    packages_id=new ArrayList<>();


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        package_details.add(jo.getString("agency_name")+" : "+jo.getString("package_details"));
                        food_availability.add(jo.getString("food_availability"));
                        rate.add(jo.getString("rate"));
                        packages_id.add(jo.getString("packages_id"));


                    }

                     ArrayAdapter<String> ad=new ArrayAdapter<String>(packages1.this,android.R.layout.simple_list_item_1,package_details);
                    packages.setAdapter(ad);
                    packages.setOnItemClickListener(packages1.this);

//                    packages.setAdapter(new Custom3(packages1.this,package_details,food_availability,rate));
//                    comments.setOnItemClickListener(View_Comment.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(packages1.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pid", "1");


                return params;
            }
        };
        queue.add(stringRequest);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent(getApplicationContext(),packages2.class);

        i.putExtra("Package_details",package_details.get(position));
        i.putExtra("food_availability",food_availability.get(position));
        i.putExtra("rate",rate.get(position));
        i.putExtra("packages_id",packages_id.get(position));

        startActivity(i);

    }
}