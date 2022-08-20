package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class allpost extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView posts;
    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> user_name,date,post,photo,pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allpost);

        posts = findViewById(R.id.allpost);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url ="http://"+sp.getString("ip", "") + ":5000/viewpost";


        RequestQueue queue = Volley.newRequestQueue(allpost.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    user_name= new ArrayList<>();
                    date=new ArrayList<>();
                    post=new ArrayList<>();
                    photo= new ArrayList<>();
                    pid= new ArrayList<>();



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        user_name.add(jo.getString("first_name")+" "+jo.getString("last_name"));
                        date.add(jo.getString("date"));
                        post.add(jo.getString("post"));
                        photo.add(jo.getString("photo_video"));
                        pid.add(jo.getString("post_id"));



                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    posts.setAdapter(new customview(allpost.this,user_name,date,post,photo));
                    posts.setOnItemClickListener(allpost.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(allpost.this, "err"+error, Toast.LENGTH_SHORT).show();
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

        SharedPreferences.Editor ed=sp.edit();
        ed.putString("pid",pid.get(position));
        ed.commit();
        Intent i =new Intent(getApplicationContext(),other_post_comment.class);
//        i.putExtra("pid",pid.get(position));
        startActivity(i);

    }
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),userhome.class));

//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
    }
}