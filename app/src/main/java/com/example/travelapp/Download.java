package com.example.travelapp;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;


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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.io.File;
import android.os.Environment;
public class Download extends AppCompatActivity {
    ProgressDialog mProgressDialog;
    private PowerManager.WakeLock mWakeLock;
    static final int DIALOG_DOWNLOAD_PROGRESS = 2;
    SharedPreferences sh;
    String vdoid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);


        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        vdoid = getIntent().getStringExtra("file");


        SharedPreferences.Editor ed=sh.edit();
        ed.putString("orginal",vdoid);
        ed.commit();
        startDownload(vdoid);

    }

    private void startDownload(String vdoid) {


        String url = "http://"+sh.getString("ip", "")+":5000/static/image/"+vdoid;

        new DownloadFileAsync().execute(url);


    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                        getClass().getName());
                mWakeLock.acquire();
                showDialog(DIALOG_DOWNLOAD_PROGRESS);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(String... aurl) {
                int count;

                try {
                    Log.d("aurllll", aurl[0]);


                    Log.d("aurllll",aurl[0]);

                    URL url = new URL(aurl[0]);
                    URLConnection conexion = url.openConnection();
                    conexion.connect();

                    int lenghtOfFile = conexion.getContentLength();
                    Log.d("ANDRO_ASYNC", "Length of file: " + lenghtOfFile);

//	String filename = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date())+"ticket.html";
                    InputStream input = new BufferedInputStream(url.openStream());
//                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File myFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/"+sh.getString("orginal", ""));
//                File myFile = new File(storageDir, "icon.jpg");
//                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + sh.getString("orginal", ""));
                    OutputStream output =new FileOutputStream(myFile);
                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress(""+(int)((total*100)/lenghtOfFile));
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();


//                    URL url = new URL(aurl[0]);
//                    URLConnection conexion = url.openConnection();
//                    conexion.connect();
//
//                    int lenghtOfFile = conexion.getContentLength();
//                    Log.d("ANDRO_ASYNC", "Length of file: " + lenghtOfFile);
//
//
//
//
//
////	String filename = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date())+"ticket.html";
//                    InputStream input = new BufferedInputStream(url.openStream());
//                    OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + sh.getString("orginal", ""));
//
//                    byte data[] = new byte[1024];
//
//                    long total = 0;
//
//                    while ((count = input.read(data)) != -1) {
//                        total += count;
//                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));
//                        output.write(data, 0, count);
//                    }
//
//                    output.flush();
//                    output.close();
//                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onProgressUpdate(String... progress) {
                Log.d("ANDRO_ASYNC", progress[0]);
                mProgressDialog.setProgress(Integer.parseInt(progress[0]));
            }

            @Override
            protected void onPostExecute(String unused) {
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            }
        }


        @Override
        protected Dialog onCreateDialog ( int id){
            switch (id) {
                case DIALOG_DOWNLOAD_PROGRESS:
                    mProgressDialog = new ProgressDialog(this);
                    mProgressDialog.setMessage("Downloading File...");
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    return mProgressDialog;
            }
            return null;

        }



}
