package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class view_package_status extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView packagestatus;
    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> date,packages,status,pkid,price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_package_status);

        packagestatus=findViewById(R.id.viewpackegestatus);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url ="http://"+sp.getString("ip", "") + ":5000/viewpackagestatus";

        RequestQueue queue = Volley.newRequestQueue(view_package_status.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    date=new ArrayList<>();
                    packages= new ArrayList<>();
                    status= new ArrayList<>();
                    pkid= new ArrayList<>();
                    price= new ArrayList<>();



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        date.add(jo.getString("date"));
                        packages.add(jo.getString("package_details"));
                        status.add(jo.getString("status"));
                        pkid.add(jo.getString("packages_booking_id"));
                        price.add(jo.getString("rate"));




                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    packagestatus.setAdapter(new Custom3(view_package_status.this,date,packages,status));
                    packagestatus.setOnItemClickListener(view_package_status.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(view_package_status.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",sp.getString("lid",""));



                return params;
            }
        };
        queue.add(stringRequest);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(status.get(position).equalsIgnoreCase("accept"))
        {
            SharedPreferences.Editor ed=sp.edit();
            ed.putString("pkid",pkid.get(position));
            ed.putString("price",price.get(position));
            ed.putString("type","package");

            ed.commit();
            startActivity(new Intent(getApplicationContext(),bankdetails.class));

        }
        else if(status.get(position).equalsIgnoreCase("paid"))
        {
            AlertDialog.Builder ald=new AlertDialog.Builder(view_package_status.this);
            ald.setTitle("booking status Paid......")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            try
                            {
                                startActivity(new Intent(getApplicationContext(),userhome.class));


                            }
                            catch(Exception e)
                            {
                                Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_LONG).show();
                            }

                        }
                    });



            AlertDialog al=ald.create();
            al.show();


        }
        else{

            AlertDialog.Builder ald=new AlertDialog.Builder(view_package_status.this);
            ald.setTitle("booking status pending......")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            try
                            {
                                startActivity(new Intent(getApplicationContext(),userhome.class));


                            }
                            catch(Exception e)
                            {
                                Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_LONG).show();
                            }

                        }
                    });



            AlertDialog al=ald.create();
            al.show();
        }

    }
}