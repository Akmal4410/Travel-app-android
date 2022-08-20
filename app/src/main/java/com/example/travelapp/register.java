package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;




public class register extends AppCompatActivity {

EditText firstname,lastname,dob,email,phone,post,place,pin,un,pwd,photo;
RadioButton male,female;
Button register,browse;
String fname,lname,gen,dob1,email1,phone1,post1,place1,pin1,username,password;
    String url="";
    String ip="";
    SharedPreferences sh;
    private static final int FILE_SELECT_CODE = 0;
    String reslt;
    String fileName="",path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstname=findViewById(R.id.fname);
        lastname=findViewById(R.id.lname);
        male=findViewById(R.id.radioButton1);
        female=findViewById(R.id.radioButton2);
        dob=findViewById(R.id.dob);
        photo=findViewById(R.id.photo);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        post=findViewById(R.id.post);
        place=findViewById(R.id.place);
        pin=findViewById(R.id.pin);
        browse=findViewById(R.id.button8);
        un=findViewById(R.id.uname);
        pwd=findViewById(R.id.pwd);
        register=findViewById(R.id.register);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("ip","192.168.43.65");
        dob.setInputType(InputType.TYPE_NULL);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog datepicker = new DatePickerDialog(register.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
//                datepicker.getDatePicker().setMinDate(System.currentTimeMillis());

                datepicker.show();
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //getting all types of files
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, ""), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        url="http://"+ip+":5000/userregistration";
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname=firstname.getText().toString();
                lname=lastname.getText().toString();
                if(male.isChecked())
                {
                    gen=male.getText().toString();
                }
                else
                {
                    gen=female.getText().toString();
                }

                dob1=dob.getText().toString();
                email1=email.getText().toString();
                phone1=phone.getText().toString();
                post1=post.getText().toString();
                place1=place.getText().toString();
                pin1=pin.getText().toString();
                username=un.getText().toString();
                password=pwd.getText().toString();
                if (fname.equalsIgnoreCase(""))
                {
                    firstname.setError("Enter your First Name");
                }
                else if (lname.equalsIgnoreCase(""))
                {
                    lastname.setError("Enter your Last name");
                }
                else if (dob1.equalsIgnoreCase(""))
                {
                    dob.setError("Enter your Date of Birth");
                }
                else if (email1.equalsIgnoreCase(""))
                {
                    email.setError("Enter your Email");
                }
                else if (phone1.equalsIgnoreCase(""))
                {
                    phone.setError("Enter your Phone NO.");
                }
                else if (post1.equalsIgnoreCase(""))
                {
                    post.setError("Enter your Post");
                }
                else if (place1.equalsIgnoreCase(""))
                {
                    place.setError("Enter your Place");
                }
                else if (pin1.equalsIgnoreCase(""))
                {
                    pin.setError("Enter your Pin no.");
                }
                else if (username.equalsIgnoreCase(""))
                {
                    un.setError("Enter your User Name");
                }
                else if (password.equalsIgnoreCase(""))
                {
                    pwd.setError("Enter your Password");
                }
                else if(path.equals(""))
                {
                    photo.setError("select file");
                }
                else {
                    reslt = uploadFile(path);



                    try {
                        JSONObject jo = new JSONObject(reslt);
                        String status = jo.getString("task");
                        if(status.equals("success"))
                        {
                            Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                            Intent in = new Intent(getApplicationContext(), userhome.class);
                                    startActivity(in);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                             ;

//                    }
//                    RequestQueue queue = Volley.newRequestQueue(register.this);
//
//                    // Request a string response from the provided URL.
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
////                        Display the response string.
//
//                            try {
//                                JSONObject jo = new JSONObject(response);
//                                String status = jo.getString("task");
//                                Toast.makeText(register.this, status, Toast.LENGTH_SHORT).show();
//
//                                if (status.equalsIgnoreCase("success")) {
//
//                                    Toast.makeText(register.this, status, Toast.LENGTH_SHORT).show();
//
//                                    Intent in = new Intent(getApplicationContext(), login.class);
//                                    startActivity(in);
////
//
//
//                                } else {
//                                    Toast.makeText(register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (Exception e) {
//
//                                Toast.makeText(register.this, "" + e, Toast.LENGTH_SHORT).show();
//
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(register.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<>();
//                            params.put("username", username);
//                            params.put("password", password);
//                            params.put("fname", fname);
//                            params.put("lname", lname);
//                            params.put("gender", gen);
//                            params.put("dob", dob1);
//                            params.put("email", email1);
//                            params.put("phoneno", phone1);
//                            params.put("post", post1);
//                            params.put("place", place1);
//                            params.put("pin", pin1);
//
//
//                            return params;
//                        }
//                    };
//                    // Add the request to the RequestQueue.
//                    queue.add(stringRequest);

                }

            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("File Uri", "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        path = FileUtils.getPath(this, uri);
                        //e4.setText(path1);
                        Log.d("path", path);
                        File fil = new File(path);
                        int fln = (int) fil.length();
                        photo.setText(path);

                        File file = new File(path);

                        byte[] byteArray = null;
                        try {
                            InputStream inputStream = new FileInputStream(fil);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            byte[] b = new byte[fln];
                            int bytesRead = 0;

                            while ((bytesRead = inputStream.read(b)) != -1) {
                                bos.write(b, 0, bytesRead);
                            }

                            byteArray = bos.toByteArray();
                            inputStream.close();
                            Bitmap bmp= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//                            if(bmp!=null){
//                                photo.setVisibility(View.VISIBLE);
//                                photo.setImageBitmap(bmp);
//                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    } catch (URISyntaxException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Please select suitable file", Toast.LENGTH_LONG).show();
                }
                break;


        }


    }

    public String uploadFile(String sourceFileUri) {

        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            fileName = sourceFileUri;
//            String upLoadServerUri = "http://" + sh.getString("ip", "") + ":5000/postphoto";
//            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
            FileUpload fp = new FileUpload(fileName);
            Map params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);
            params.put("fname", fname);
            params.put("lname", lname);
            params.put("gender", gen);
            params.put("dob", dob1);
            params.put("email", email1);
            params.put("phoneno", phone1);
            params.put("post", post1);
            params.put("place", place1);
            params.put("pin", pin1);
           String res= fp.multipartRequest(url, params, fileName, "files", "application/octet-stream");
            return res;

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"error"+e,Toast.LENGTH_LONG).show();
            return "0";
        }
    }
}