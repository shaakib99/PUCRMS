package com.example.wahid.pucrms;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class fourthactivity extends AppCompatActivity {
    TextView mysec, othersec,teacher_id;
    RecyclerView rv;
    RecyclerView.LayoutManager rv_layout_manager;
    RecyclerView.Adapter rv_adapter;
    ImageView back2;
    String my_section,selected_subject,other_section,teacher;
    String[] day,start_time,end_time,classes;
    String[] din,shuru_time,sesh_time,cl;
    ArrayList<String> overlap_data = new ArrayList<>();
    ArrayList<String> section_data = new ArrayList<>();
    ArrayList<String> d = new ArrayList<>();
    Dialog load,ok;
    Button okkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourthactivity);
        teacher_id = findViewById(R.id.teacher_id);
        mysec = findViewById(R.id.mysection);
        othersec = findViewById(R.id.othersection);
        rv = findViewById(R.id.recylerview2);
        load = new Dialog(this);
        ok  = new Dialog(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;



        ok.setCanceledOnTouchOutside(false);
        ok.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ok.getWindow().setGravity(Gravity.CENTER);
        ok.getWindow().setLayout((int) (width*.9),(int) (height*.4));
        ok.setContentView(R.layout.oklayout);

        okkey = ok.findViewById(R.id.okbutton);
        okkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             finish();
                ok.dismiss();


            }
        });

        load.setCanceledOnTouchOutside(false);
        load.requestWindowFeature(Window.FEATURE_NO_TITLE);
        load.getWindow().setGravity(Gravity.CENTER);
        load.setContentView(R.layout.progress);

        my_section = getIntent().getStringExtra("section_data");
        selected_subject = getIntent().getStringExtra("selected_subject").replaceAll(" ","%20");
        other_section = getIntent().getStringExtra("other_section");

        mysec.setText(my_section);
        othersec.setText(other_section);
        connection cn = new connection();
        cn.execute();

        back2=findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }


    class connection extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            String host = "https://pucrms.000webhostapp.com/routine/overlap.php?section="+other_section + "&previous_section="+my_section+"&subject="+selected_subject;

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            String result = "";

            try {
                request.setURI(new URI(host));
                HttpResponse response = client.execute(request);
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer stringBuffer = new StringBuffer("");

                String line = "";

                while ((line = br.readLine()) != null) {
                    stringBuffer.append(line);
                }
                br.close();
                result = stringBuffer.toString();

            } catch (URISyntaxException e) {
                e.printStackTrace();
                  Toast.makeText(fourthactivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                //  Toast.makeText(firstactivity.this,"Server is not resoponding",Toast.LENGTH_LONG).show();
                // response=1;
            } catch (IOException e) {
                e.printStackTrace();
                 Toast.makeText(fourthactivity.this,"Could not connect to server",Toast.LENGTH_LONG).show();
                // response=2;
            }

            try {

                JSONObject jsonresult = new JSONObject(result);
                JSONArray jb = jsonresult.getJSONArray("overlap_data");
                JSONArray jx = jsonresult.getJSONArray("section_data");
                teacher  = jsonresult.getString("teacher");
                day = new String[jb.length()];
                start_time = new String[jb.length()];
                end_time = new String[jb.length()];
                classes = new String[jb.length()];

                for (int i=0;i<jb.length();i++)
                {
                    JSONObject jc = jb.getJSONObject(i);
                    day[i] = jc.getString("day");
                    start_time[i] = jc.getString("start_time");
                    end_time[i] = jc.getString("end_time");
                    classes[i] = jc.getString("classes");
                }
                din = new String[jx.length()];
                shuru_time = new String[jx.length()];
                sesh_time = new String[jx.length()];
                cl = new String[jx.length()];

                for (int i = 0;i<jx.length();i++)
                {
                    JSONObject jy = jx.getJSONObject(i);
                    din[i] =  jy.getString("day");
                    shuru_time[i] = jy.getString("start_time");
                    sesh_time[i] = jy.getString("end_time");
                    cl[i] =jy.getString("classes");
                }
                for (int i=0;i<jx.length();i++)
                {
                    for (int j=0;j<jb.length();j++) {
                        if (din[i].equals(day[j])) {
                            d.add(din[i]);
                            overlap_data.add(NeccessaryMethods.fixtwetyfourhourtime(start_time[j].substring(0, 5)) + "-" + NeccessaryMethods.fixtwetyfourhourtime(end_time[j].substring(0, 5)) + "\n" + classes[j]);
                            section_data.add(NeccessaryMethods.fixtwetyfourhourtime(shuru_time[i].substring(0, 5)) + "-" + NeccessaryMethods.fixtwetyfourhourtime(sesh_time[i].substring(0, 5)) + "\n" + cl[i]);
                        }
                    }
                }
                if (jx.length() > jb.length())
                {
                    for (int i= jb.length();i<jx.length();i++)
                    {

                        d.add(din[i]);
                        overlap_data.add("");
                        section_data.add(NeccessaryMethods.fixtwetyfourhourtime(shuru_time[i].substring(0, 5)) + "-" + NeccessaryMethods.fixtwetyfourhourtime(sesh_time[i].substring(0, 5)) + "\n" + cl[i]);

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            load.show();
        }

        @Override
        protected void onPostExecute(String s) {
            load.dismiss();
            if (overlap_data.size() == 0) {
                //Toast.makeText(fourthactivity.this,"Congrats! No Overalpped Subject Found",Toast.LENGTH_LONG).show();
                ok.show();
            } else {
                teacher_id.setText(selected_subject.replaceAll("%20"," ")+" is taken by "+ teacher);
                rv.setHasFixedSize(true);
                rv_layout_manager = new LinearLayoutManager(fourthactivity.this);
                rv_adapter = new cardview2(d, overlap_data, section_data);
                rv.setLayoutManager(rv_layout_manager);
                rv.setAdapter(rv_adapter);
                runAnimation(rv);
            }
        }
    }

    public void runAnimation(RecyclerView recyclerView)
    {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_slide_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter() .notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}
