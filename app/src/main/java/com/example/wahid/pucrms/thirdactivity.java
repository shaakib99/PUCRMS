package com.example.wahid.pucrms;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class thirdactivity extends AppCompatActivity {
    Button go;
    ImageView back;
    Spinner sectionSelect,SubjectSelect;
    Dialog wait;
    String[] section_Global;
    String section_name;
    String[] subject;
    String selected_Subject;
    String section_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdactivity);

        back = findViewById(R.id.back);
        go = findViewById(R.id.overlapgo);
        sectionSelect = findViewById(R.id.selectSection);
        SubjectSelect = findViewById(R.id.sSubject);

        section_data = getIntent().getStringExtra("section");
        wait = new Dialog(this);
        wait.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wait.setContentView(R.layout.progress);
        wait.setCanceledOnTouchOutside(false);

        wait.getWindow().setGravity(Gravity.CENTER);

        connection cn = new connection();
        cn.execute();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }




    class connection extends AsyncTask<String,String,String> {



        @Override
        protected String doInBackground(String... strings) {
            String host = "https://pucrms.000webhostapp.com/routine/fetch-section.php";

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            String result="";

            try {
                request.setURI(new URI(host));
                HttpResponse response = client.execute(request);
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer stringBuffer = new StringBuffer("");

                String line ="";

                while ((line=br.readLine())!= null)
                {
                    stringBuffer.append(line);
                }
                br.close();
                result = stringBuffer.toString();

            } catch (URISyntaxException e) {
                e.printStackTrace();
                //  Toast.makeText(thirdactivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                //  Toast.makeText(firstactivity.this,"Server is not resoponding",Toast.LENGTH_LONG).show();
               // response=1;
            } catch (IOException e) {
                e.printStackTrace();
                // Toast.makeText(thirdactivity.this,"Could not connect to server",Toast.LENGTH_LONG).show();
               // response=2;
            }

            try {
                JSONObject jsonresult = new JSONObject(result);
                JSONArray sectionArray = jsonresult.getJSONArray("section_data");
                String[] section = new String[sectionArray.length()];

                for (int i =0;i<sectionArray.length();i++)
                {
                    section[i] = sectionArray.getString(i);
                }
                section_Global= section.clone();
            } catch (JSONException e) {
                e.printStackTrace();
               // response=3;
                //  Toast.makeText(firstactivity.this,"Problem on Parsing Data",Toast.LENGTH_LONG).show();
            }
            return  null;
        }

        @Override
        protected void onPreExecute() {
            wait.show();
        }

        @Override
        protected void onPostExecute(String s) {


          try {

              ArrayAdapter<String> adapter = new ArrayAdapter<>(thirdactivity.this, android.R.layout.simple_list_item_1, section_Global);
              sectionSelect.setAdapter(adapter);
          }catch (Exception e) {
              e.printStackTrace();
              //something went wrong
          }

          sectionSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  section_name = section_Global[i];
                  connection2 cn2 = new connection2();
                  cn2.execute();
              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {

              }
          });

            wait.dismiss();
            }
    }



    class connection2 extends AsyncTask<String,String,String> {



        @Override
        protected String doInBackground(String... strings) {
            String host = "https://pucrms.000webhostapp.com/routine/subject.php?section="+section_name;

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            String result="";

            try {
                request.setURI(new URI(host));
                HttpResponse response = client.execute(request);
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer stringBuffer = new StringBuffer("");

                String line ="";

                while ((line=br.readLine())!= null)
                {
                    stringBuffer.append(line);
                }
                br.close();
                result = stringBuffer.toString();

            } catch (URISyntaxException e) {
                e.printStackTrace();
                //  Toast.makeText(firstactivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                //  Toast.makeText(firstactivity.this,"Server is not resoponding",Toast.LENGTH_LONG).show();
                // response=1;
            } catch (IOException e) {
                e.printStackTrace();
                // Toast.makeText(firstactivity.this,"Could not connect to server",Toast.LENGTH_LONG).show();
                // response=2;
            }

                try {
                    JSONObject jsonresult = new JSONObject(result);


                    JSONArray classes = jsonresult.getJSONArray("data");
                    subject = new String[classes.length()];
                    // Toast.makeText(overlap_activity.this,"hello",Toast.LENGTH_LONG).show();



                    for (int i = 0; i < classes.length(); i++) {
                        JSONObject jb = classes.getJSONObject(i);
                        subject[i] = jb.getString("classes");

                    }
            } catch (JSONException e) {
                e.printStackTrace();
                // response=3;
                //  Toast.makeText(firstactivity.this,"Problem on Parsing Data",Toast.LENGTH_LONG).show();
            }
            return  null;
        }

        @Override
        protected void onPreExecute() {
            wait.show();
        }

        @Override
        protected void onPostExecute(String s) {


            try {

                ArrayAdapter<String> ad = new ArrayAdapter<>(thirdactivity.this, android.R.layout.simple_list_item_1, subject);
                SubjectSelect.setAdapter(ad);
            }catch (Exception e) {
                e.printStackTrace();
                //something went wrong
            }

            SubjectSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   selected_Subject = subject[i];
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            wait.dismiss();
            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent it = new Intent(thirdactivity.this,fourthactivity.class).putExtra("selected_subject",selected_Subject);
                    it.putExtra("section_data",section_data);
                    it.putExtra("other_section",section_name);
                    startActivity(it);

                }
            });
        }

    }








}
