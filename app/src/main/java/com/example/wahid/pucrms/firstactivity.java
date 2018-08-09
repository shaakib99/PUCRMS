package com.example.wahid.pucrms;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class firstactivity extends AppCompatActivity {
    Button Go;
    Spinner sectionSpinner;
    String[] defaultsection;
    String Global_section;
    Dialog wait;
    int response;
    DatabaseHelper db;
    String section =" ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstactivity);
        Go = findViewById(R.id.firstsectionbutton);
        sectionSpinner = findViewById(R.id.sectionSelection);
        db = new DatabaseHelper(firstactivity.this);

        Cursor c = db.getSection();

        if (c.moveToLast()) {
            section = c.getString(0);
            Toast.makeText(firstactivity.this, section, Toast.LENGTH_SHORT).show();
        }

        if (!section.equals(" ")) {
            Intent it = new Intent(firstactivity.this, secondactivity.class).putExtra("section", section);
            startActivity(it);
        } else {


            wait = new Dialog(firstactivity.this);

            wait.requestWindowFeature(Window.FEATURE_NO_TITLE);
            wait.setContentView(R.layout.progress);
            wait.getWindow().setGravity(Gravity.CENTER);
            wait.setCanceledOnTouchOutside(false);


            connection cn = new connection();
            cn.execute();


        }
    }
    @Override
    public void onBackPressed()
    {
        moveTaskToBack(false);

            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finish();

    }


    class connection extends AsyncTask<String,String,String>{



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

            } catch (URISyntaxException  e) {
                e.printStackTrace();
              //  Toast.makeText(firstactivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
           } catch (ClientProtocolException e) {
                e.printStackTrace();
              //  Toast.makeText(firstactivity.this,"Server is not resoponding",Toast.LENGTH_LONG).show();
                response=1;
            } catch (IOException e) {
                e.printStackTrace();
               // Toast.makeText(firstactivity.this,"Could not connect to server",Toast.LENGTH_LONG).show();
                response=2;
            }

            try {
                JSONObject jsonresult = new JSONObject(result);
                JSONArray sectionArray = jsonresult.getJSONArray("section_data");
                String[] section = new String[sectionArray.length()];

                for (int i =0;i<sectionArray.length();i++)
                {
                    section[i] = sectionArray.getString(i);
                }
                defaultsection = section.clone();
            } catch (JSONException e) {
                e.printStackTrace();
                response=3;
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

          try{  ArrayAdapter<String> adapter = new ArrayAdapter<String>(firstactivity.this,android.R.layout.simple_list_item_1,defaultsection);

                sectionSpinner.setAdapter(adapter);
            }catch (Exception e)
            {
                Toast.makeText(firstactivity.this,"Something Went Wrong ",Toast.LENGTH_LONG).show();
            }


            wait.dismiss();

            sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Global_section = defaultsection[i];
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            Go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if ((response == 1) || (response ==2) || (response == 3))
                    {
                        Toast.makeText(firstactivity.this,"Failed to connect",Toast.LENGTH_SHORT).show();
                        Toast.makeText(firstactivity.this,"Try checking your internet connection",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        db.addSection(Global_section);
                        Toast.makeText(firstactivity.this,"Section saved into database",Toast.LENGTH_LONG).show();
                        Intent it = new Intent(firstactivity.this, secondactivity.class).putExtra("section", Global_section);
                        startActivity(it);
                        finish();
                    }
                }
            });



        }
    }
}
