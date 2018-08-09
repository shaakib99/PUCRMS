package com.example.wahid.pucrms;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.provider.MediaStore.Files.FileColumns.PARENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class secondactivity extends AppCompatActivity {
    ImageView threedot;
    Dialog delete;
    FloatingActionButton fab;
    Dialog progress;
    String[] start_time;
    String[] end_time ;
    String[] classes;
    String[] room;
    String[] weekdays = {"Saturday","Sunday","Monday","Tuesday","Wednesday"};
    String section_data;
    String selected_day;
    String ongoing_subject=" ";
    String ongoing_time=" ";
    String currentRoom = " ";
    String next_sub= " ";
    String next_time=" ";
    String next_room=" ";
    Float system_time;
    String message="";
    String today;
    int count=0;
    int count2 =0;
    public static  int backbutton;

    ArrayList<String> Time;
    ArrayList<String> subjectAndRoom;

    TextView day, section, current_subject, next_subject, current_time, nextTime,current_room,nextRoom;
    Spinner day_spinner;
    RecyclerView information;
    RecyclerView.LayoutManager mlayoutmanager;
    RecyclerView.Adapter madapter;

    Dialog pop;

    DatabaseHelper db;
    int response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondactivity);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }



        day = findViewById(R.id.day);
        section = findViewById(R.id.section);
        current_subject = findViewById(R.id.current_subject);
        next_subject = findViewById(R.id.next_subject);
        current_time = findViewById(R.id.time);
        current_room = findViewById(R.id.room);
        nextTime = findViewById(R.id.nexttime);
        nextRoom = findViewById(R.id.nextRoom);
        day_spinner = findViewById(R.id.dayselection);
        information = findViewById(R.id.recylerview);
        backbutton =0;

        Time = new ArrayList<>();
        subjectAndRoom = new ArrayList<>();




        String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

        system_time = NeccessaryMethods.TimeToDecimalConverter(timeStamp);



        section_data = getIntent().getStringExtra("section");
        today = NeccessaryMethods.GetDay();
        day.setText(today); //Day textbox e ajker din set kore dilam
        section.setText("Section: "+section_data); // section data set  korlam

        ArrayAdapter adapter = new ArrayAdapter(secondactivity.this,android.R.layout.simple_list_item_1,weekdays);
        day_spinner.setAdapter(adapter);

        int i=0;
        int weekday_value_holder=0;
        while (i<5)
        {
           if (weekdays[i].equals(today))
           {
               weekday_value_holder = i;
           }
            i++;
        }

        day_spinner.setSelection(weekday_value_holder);

        day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Time.clear();
                subjectAndRoom.clear();
                selected_day = weekdays[i];
                connection cn = new connection();
                cn.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        threedot = findViewById(R.id.threedot);
        fab = findViewById(R.id.fab);
        pop = new Dialog(this);
        pop.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pop.requestWindowFeature(MATCH_PARENT);
        delete = new Dialog(this);
        delete.requestWindowFeature(Window.FEATURE_NO_TITLE);

        progress = new Dialog(this);
        progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        progress.getWindow().setLayout(w, (int) (h * .22));
        progress.setContentView(R.layout.progress);

        //progress.show();


        threedot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup(view);

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdeletedatabase(view);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(false);
        if (backbutton<1)
        {
            Toast.makeText(secondactivity.this,"Press BACK again to exit",Toast.LENGTH_SHORT).show();
            backbutton++;
        }
        else
        {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(a);
            finish();
        }
    }



    public void showpopup(View v) {
        ImageView cancel;
        RelativeLayout feedback;
        RelativeLayout alarm;
        RelativeLayout oveerlapp;
        pop.setContentView(R.layout.popuplayout);
        cancel = pop.findViewById(R.id.cancel_icon);
        feedback = pop.findViewById(R.id.feedbackclick);
        alarm = pop.findViewById(R.id.alarm_click);
        oveerlapp = pop.findViewById(R.id.Overlap_click);


        pop.getWindow().setGravity(Gravity.TOP);
        pop.setCanceledOnTouchOutside(true);
        pop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        pop.getWindow().getAttributes().windowAnimations = R.style.slidedown;
        pop.show();
        Window popwindow = pop.getWindow();
        popwindow.setLayout(MATCH_PARENT, WRAP_CONTENT);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), feedbackactivity.class);
                startActivity(it);
                pop.dismiss();
            }
        });
        oveerlapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), thirdactivity.class).putExtra("section",section_data);
                startActivity(it);
                pop.dismiss();
            }
        });
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Intent it = new Intent(secondactivity.this,AlarmActivity.class).putExtra("section",section_data);
               // startActivity(it);
                pop.dismiss();
            }
        });
    }

    public void showdeletedatabase(View v) {
        Button yes;
        Button no;
        delete.setContentView(R.layout.deletedatabase);
        yes = delete.findViewById(R.id.yes);
        no = delete.findViewById(R.id.no);
        delete.getWindow().setGravity(Gravity.CENTER);

        delete.show();
        Window deletewindow = delete.getWindow();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        deletewindow.setLayout((int) (width), (int) (height * .223));
        delete.setCanceledOnTouchOutside(false);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DatabaseHelper(secondactivity.this);
                db.clearData();
                delete.dismiss();
                Toast.makeText(secondactivity.this, "Database Cleared", Toast.LENGTH_LONG).show();
                Intent it = new Intent(secondactivity.this, firstactivity.class);
                startActivity(it);

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete.dismiss();
            }
        });

    }


    class connection extends AsyncTask<String, String, String> {

        float float_value_of_start_time=0;
        float float_value_of_end_time=0;

        @Override
        protected String doInBackground(String... strings) {
            String host = "https://pucrms.000webhostapp.com/routine/fetch_data.php?db="+section_data+"&day="+selected_day;

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
                 // Toast.makeText(secondactivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                response=0;

            } catch (ClientProtocolException e) {
                e.printStackTrace();
                //  Toast.makeText(firstactivity.this,"Server is not resoponding",Toast.LENGTH_LONG).show();
                response=1;
            } catch (IOException e) {
                e.printStackTrace();
               //  Toast.makeText(secondactivity.this,"Could not connect to server",Toast.LENGTH_LONG).show();
                 response=2;
            }


            try {
                JSONObject jsonresult = new JSONObject(result);
                JSONArray info = jsonresult.getJSONArray("data");
                start_time = new String[info.length()];
                end_time = new String[info.length()];
                classes = new String[info.length()];
                room = new String[info.length()];
                for (int i =0;i<info.length();i++)
                {
                    JSONObject jb = info.getJSONObject(i);
                    start_time[i] = jb.getString("start_time");
                    end_time[i] = jb.getString("end_time");
                    classes[i] = jb.getString("classes");
                    room[i] = jb.getString("room_no");

                    String temp_class_time_and_end_time = NeccessaryMethods.fixtwetyfourhourtime(start_time[i].substring(0, 5)) + "-" + NeccessaryMethods.fixtwetyfourhourtime(end_time[i].substring(0, 5));
                    Time.add(temp_class_time_and_end_time); //array list e add kore nilam
                    String temp_Subject_and_Room = classes[i] + " at " +  room[i];
                    subjectAndRoom.add(temp_Subject_and_Room); //array list e add kore nilam

                     float_value_of_start_time = NeccessaryMethods.twentyfourhourTime(start_time[i].substring(0,5));
                     float_value_of_end_time = NeccessaryMethods.twentyfourhourTime(end_time[i].substring(0,5));


                        if (float_value_of_start_time <=system_time)
                        {
                            if (float_value_of_end_time >= system_time)
                            {
                                if (today.equals(selected_day))
                                {
                                ongoing_subject = classes[i];
                                ongoing_time = NeccessaryMethods.fixtwetyfourhourtime(start_time[i].substring(0,5)) + " - " + NeccessaryMethods.fixtwetyfourhourtime(end_time[i].substring(0,5));
                                currentRoom = room[i];


                                }
                                count++;
                            }

                        }
                    if (count2<1 && (float_value_of_start_time>system_time) && today.equals(selected_day))
                    {

                        try
                        {
                            JSONObject jc = info.getJSONObject(i);
                            next_sub = jc.getString("classes");
                            next_time = NeccessaryMethods.fixtwetyfourhourtime(jc.getString("start_time").substring(0,5)) + " - " +NeccessaryMethods.fixtwetyfourhourtime( jc.getString("end_time").substring(0,5));
                            next_room = jc.getString("room_no");

                        } catch (Exception e)
                        {
                            //array index out of bound
                        }
                        count2++;
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            progress.show();
        }

        @Override
        protected void onPostExecute(String s) {
            current_subject.setText(ongoing_subject);
           current_time.setText(ongoing_time);
           if (currentRoom.equals(" "))
           {
               current_room.setText(" ");
           }
           else
           current_room.setText("at "+currentRoom);


            next_subject.setText(next_sub);
            nextTime.setText(next_time);
            if (next_room.equals(" "))
            {
                nextRoom.setText(" ");
            }
            else
            nextRoom.setText("at " + next_room);
            if ( response ==1 || response ==2)
            {
                Toast.makeText(secondactivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }

            information.setHasFixedSize(true);
            mlayoutmanager = new LinearLayoutManager(secondactivity.this);
            madapter = new cardview1(Time,subjectAndRoom);
            information.setLayoutManager(mlayoutmanager);
            information.setAdapter(madapter);
            progress.dismiss();

            runAnimation(information);
         //   Toast.makeText(secondactivity.this,String.valueOf(float_value_of_end_time),Toast.LENGTH_LONG).show();

        }
    }

    public void runAnimation(RecyclerView recyclerView)
    {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_slide_up);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
