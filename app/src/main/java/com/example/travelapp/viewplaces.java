package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

import android.widget.SearchView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class viewplaces extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView viewplace;

    SharedPreferences sp;
    String url="",ip="",name;
    ArrayList<String> places,latitude,longitude,description,image;
    SearchView s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewplaces);
        s1=(SearchView)findViewById(R.id.searchView1);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        viewplace=findViewById(R.id.viewplace);
         viewplace();


        try
        {
            if(android.os.Build.VERSION.SDK_INT>9)
            {
                StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        }
        catch(Exception e)
        {

        }
        s1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                return false;

            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                name=arg0;
                viewplc(name);
                return true;
            }
        });
    }

    private void viewplc(String name) {


        RequestQueue queue = Volley.newRequestQueue(viewplaces.this);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = "http://" + sp.getString("ip", "") + ":5000/viewplacebyrating";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);

                    places = new ArrayList<>();
                    latitude = new ArrayList<>();
                    longitude = new ArrayList<>();
                    description = new ArrayList<>();
                    image = new ArrayList<>();


                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);

                        places.add(jo.getString("place"));
                        latitude.add(jo.getString("latitude"));
                        longitude.add(jo.getString("longitude"));
                        description.add(jo.getString("description"));
                        image.add(jo.getString("image"));




                    }
//                     ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);
                    viewplace.setAdapter(new customview2(viewplaces.this,places,description,latitude,longitude,image));
//                    viewplace.setOnItemClickListener(viewplaces.this);


                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(viewplaces.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();




                params.put("name", name);


                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void viewplace() {



        url ="http://"+sp.getString("ip", "") + ":5000/viewplace";

        RequestQueue queue = Volley.newRequestQueue(viewplaces.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    places = new ArrayList<>();
                    latitude = new ArrayList<>();
                    longitude = new ArrayList<>();
                    description = new ArrayList<>();
                    image = new ArrayList<>();



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        places.add(jo.getString("place"));
                        latitude.add(jo.getString("latitude"));
                        longitude.add(jo.getString("longitude"));
                        description.add(jo.getString("description"));
                        image.add(jo.getString("image"));


                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    viewplace.setAdapter(new customview2(viewplaces.this,places,description,latitude,longitude,image));
                    viewplace.setOnItemClickListener(viewplaces.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(viewplaces.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();



                return params;
            }
        };
        queue.add(stringRequest);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}