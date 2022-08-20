package com.example.travelapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class customview extends BaseAdapter{

SharedPreferences sh;

    private Context context;
    String vdoid;

    ArrayList<String> a,b,c,d;





    public customview(Context applicationContext, ArrayList<String> a, ArrayList<String> b, ArrayList<String> c,ArrayList<String> d) {
        // TODO Auto-generated constructor stub
        this.context=applicationContext;
        this.a=a;
        this.b=b;
        this.c=c;
        this.d=d;


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return a.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getItemViewType(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(convertview==null)
        {
            gridView=new View(context);
            gridView=inflator.inflate(R.layout.activity_customview, null);

        }
        else
        {
            gridView=(View)convertview;

        }
        TextView download=(TextView)gridView.findViewById(R.id.textView57);
        TextView tv1=(TextView)gridView.findViewById(R.id.textView54);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView55);
        TextView tv3=(TextView)gridView.findViewById(R.id.textView56);
        ImageView iv1=(ImageView)gridView.findViewById(R.id.imageView2);
        sh= PreferenceManager.getDefaultSharedPreferences(context);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent i=new Intent(context,Download.class);
                i.putExtra("file",d.get(position));
                context.startActivity(i);








            }
        });



        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        java.net.URL thumb_u;
        try {

            //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");

            thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000/static/image/"+d.get(position));
            Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
            iv1.setImageDrawable(thumb_d);

        }
        catch (Exception e)
        {
            Log.d("errsssssssssssss",""+e);
        }




        tv1.setText(a.get(position));
        tv2.setText(b.get(position));
        tv3.setText(c.get(position));




        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);





        return gridView;


    }




}
