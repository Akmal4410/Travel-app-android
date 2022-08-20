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

public class feedback extends AppCompatActivity {

EditText feedback;
Button send;
String feedback1;
    String url="";
    String ip="";
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedback=findViewById(R.id.feedback);
        send=findViewById(R.id.send);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("ip","192.168.43.65");
        url="http://"+sh.getString("ip","")+":5000/addfeedback";

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                feedback1=feedback.getText().toString();

                if(feedback1.equalsIgnoreCase(""))
                {
                    feedback.setError("Enter Your Feedback");
                }
                else {

                    RequestQueue queue = Volley.newRequestQueue(feedback.this);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.

                            try {
                                JSONObject jo = new JSONObject(response);
                                String status = jo.getString("task");
                                Toast.makeText(feedback.this, status, Toast.LENGTH_SHORT).show();

                                if (status.equalsIgnoreCase("success")) {


                                    Toast.makeText(feedback.this, status, Toast.LENGTH_SHORT).show();

                                    Intent in = new Intent(getApplicationContext(), userhome.class);
                                    startActivity(in);
//


                                } else {
                                    Toast.makeText(feedback.this, "Feedback not send", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                                Toast.makeText(feedback.this, "" + e, Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(feedback.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            params.put("userid", sh.getString("lid", ""));
                            params.put("feedback", feedback1);

                            return params;
                        }
                    };
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }
        });
    }
}