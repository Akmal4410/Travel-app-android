package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Add_Comment extends AppCompatActivity {

EditText comment;
Button send;
String comment1;
    String url="";
    String ip="";
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__comment);

        comment=findViewById(R.id.comment);
        send=findViewById(R.id.button14);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("ip","192.168.43.65");
        url="http://"+sh.getString("ip","")+":5000/addcomment";

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comment1=comment.getText().toString();
                if(comment1.equalsIgnoreCase("")){
                    comment.setError("Enter Your Comment");
                }
                else {

                    RequestQueue queue = Volley.newRequestQueue(Add_Comment.this);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.

                            try {
                                JSONObject jo = new JSONObject(response);
                                String status = jo.getString("task");
                                Toast.makeText(Add_Comment.this, status, Toast.LENGTH_SHORT).show();

                                if (status.equalsIgnoreCase("success")) {


                                    Toast.makeText(Add_Comment.this, status, Toast.LENGTH_SHORT).show();

                                    Intent in = new Intent(getApplicationContext(), other_post_comment.class);
                                    startActivity(in);
//


                                } else {
                                    Toast.makeText(Add_Comment.this, "Comment not send", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                                Toast.makeText(Add_Comment.this, "" + e, Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Add_Comment.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("postid", sh.getString("pid", ""));
                            params.put("userid", sh.getString("lid", ""));
                            params.put("comment", comment1);


                            return params;
                        }
                    };
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }
        });
    }
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),userhome.class));

//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
    }
}