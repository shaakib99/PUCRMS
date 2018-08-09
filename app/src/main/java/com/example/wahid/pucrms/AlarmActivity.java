package com.example.wahid.pucrms;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
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
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    CheckBox[] chk_day = new CheckBox[5];
    Button set_alarm;
    String section_data;
    String[] json_start_time_data= new String[5];
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        chk_day[0] = findViewById(R.id.chk_sat);
        chk_day[1] = findViewById(R.id.chk_sun);
        chk_day[2] = findViewById(R.id.chk_mon);
        chk_day[3] = findViewById(R.id.chk_tue);
        chk_day[4] = findViewById(R.id.chk_wed);
        set_alarm = findViewById(R.id.set_alarm);

        section_data = getIntent().getStringExtra("section");

        connection cn = new connection();
        cn.execute();
    }

    @SuppressLint("StaticFieldLeak")
    class connection extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            String result = "";


            try {
                request.setURI(new URI("http://192.168.1.102/routine/class_start_time.php?section=" + section_data));
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
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonresult = new JSONObject(result);
                JSONArray jdata = jsonresult.getJSONArray("data");
                json_start_time_data= new String[jdata.length()];

                for (int i =0;i<jdata.length();i++)
                {
                    json_start_time_data[i] = jdata.getString(i).substring(0,5);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[] temp_;



            for (int i =0 ; i<chk_day.length;i++)
            {
                if (chk_day[i].isChecked())
                {

                    temp_ = json_start_time_data[i].split(":");
                    if (i==0) {
                        calendar.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
                        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(temp_[0]));
                        calendar.set(Calendar.MINUTE,Integer.parseInt(temp_[1]));
                        calendar.set(Calendar.SECOND,0);
                       // Toast.makeText(AlarmActivity.this,String.valueOf(i),Toast.LENGTH_LONG).show();
                    }
                    else if (i==1)
                    {
                        calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(temp_[0]));
                        calendar.set(Calendar.MINUTE,Integer.parseInt(temp_[1]));
                       // Toast.makeText(AlarmActivity.this,String.valueOf(i),Toast.LENGTH_LONG).show();
                    }
                    else if (i==2)
                    {
                        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(temp_[0]));
                        calendar.set(Calendar.MINUTE,Integer.parseInt(temp_[1]));
                      //  Toast.makeText(AlarmActivity.this,String.valueOf(i),Toast.LENGTH_LONG).show();
                    }
                    else if (i==3)
                    {
                        calendar.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
                        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(temp_[0]));
                        calendar.set(Calendar.MINUTE,Integer.parseInt(temp_[1]));
                       // Toast.makeText(AlarmActivity.this,String.valueOf(i),Toast.LENGTH_LONG).show();
                    }
                    else if (i==4)
                    {
                        calendar.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
                        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(temp_[0]));
                        calendar.set(Calendar.MINUTE,Integer.parseInt(temp_[1]));
                       // Toast.makeText(AlarmActivity.this,String.valueOf(i),Toast.LENGTH_LONG).show();
                    }

                }

            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            set_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent it = new Intent(AlarmActivity.this,myalarm.class);
                    PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,it,PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pi);
                  //  alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pi);
                    Toast.makeText(AlarmActivity.this,"Alarm is set",Toast.LENGTH_LONG).show();
                }
            });
           // Toast.makeText(AlarmActivity.this,String.valueOf(temp_[1]),Toast.LENGTH_LONG).show();

        }

        public int zeroRemve(String time)
        {
            char t=' ';
            if (time.length() == 2)
            {
                if (time.charAt(0)== '0')
                {
                    t = time.charAt(1);
                }
            }
            return  Integer.parseInt(String.valueOf(t));
        }

    }
}
