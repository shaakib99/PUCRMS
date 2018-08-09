package com.example.wahid.pucrms;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class feedbackactivity extends AppCompatActivity {


        EditText feedb;
        ImageView send,bakikon;
        List<NameValuePair> message = new ArrayList<NameValuePair>(1);
        InputStream is = null;
        String text ="";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            setContentView(R.layout.activity_feedbackactivity);
            feedb = findViewById(R.id.feedb);
            send = findViewById(R.id.send);
            bakikon = findViewById(R.id.bakikon);
            bakikon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     text = feedb.getText().toString();

                    if (!text.equals("")) {

                        try {
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost("https://pucrms.000webhostapp.com/routine/message.php");

                            message.add(new BasicNameValuePair("message", text));
                            httpPost.setEntity(new UrlEncodedFormEntity(message));

                            HttpResponse httpResponse = httpClient.execute(httpPost);

                            HttpEntity httpEntity = httpResponse.getEntity();

                            is = httpEntity.getContent();

                            Toast.makeText(feedbackactivity.this, "Thanks for the suggestion", Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                            Toast.makeText(feedbackactivity.this, "Could not connect to internet", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(feedbackactivity.this, "Could not connect to internet", Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                        Toast.makeText(feedbackactivity.this,"Please type something",Toast.LENGTH_LONG).show();
                }
            });

        }
    }


