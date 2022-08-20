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

public class view_room_details extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView viewroom;
    SharedPreferences sp;
    String url="",ip="";
    ArrayList<String> room_id,room_details,price,ac_type,bed_type,photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room_details);

        viewroom=findViewById(R.id.viewroom);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url ="http://"+sp.getString("ip", "") + ":5000/viewroomdetails";

        RequestQueue queue = Volley.newRequestQueue(view_room_details.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    room_id= new ArrayList<>();
                    room_details= new ArrayList<>();
                    price= new ArrayList<>();
                    ac_type= new ArrayList<>();
                    bed_type= new ArrayList<>();
                    photo= new ArrayList<>();


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);

                        room_id.add(jo.getString("room_id"));
                        room_details.add(jo.getString("hotel_name")+" : "+jo.getString("room_details"));
                        price.add(jo.getString("price"));
                        ac_type.add(jo.getString("a/c_type"));
                        bed_type.add(jo.getString("bed_type"));
                        photo.add(jo.getString("photo"));




                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    viewroom.setAdapter(new custom2(view_room_details.this,room_details,price));
                    viewroom.setOnItemClickListener(view_room_details.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                    Toast.makeText(view_room_details.this, "err"+e, Toast.LENGTH_SHORT).show();

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(view_room_details.this, "err"+error, Toast.LENGTH_SHORT).show();
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
        Intent i=new Intent(getApplicationContext(),book_room.class);
        i.putExtra("room_details",room_details.get(position));
        i.putExtra("price",price.get(position));
        i.putExtra("ac_type",ac_type.get(position));
        i.putExtra("bed_type",bed_type.get(position));
        i.putExtra("roomid",room_id.get(position));
        i.putExtra("phto",photo.get(position));
        startActivity(i);

    }
}