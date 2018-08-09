package com.example.wahid.pucrms;

import java.util.Date;

public class NeccessaryMethods {


    private static String settingUpDay(String text)
    {
        if (text.equals("Sat"))
            text="Saturday";
        else if (text.equals("Sun"))
            text="Sunday";
        else if (text.equals("Mon"))
            text="Monday";
        else if (text.equals("Tue"))
            text="Tuesday";
        else if (text.equals("Wed"))
            text= "Wednesday";
        else if (text.equals("Thu"))
            text="Thursday";
        else if (text.equals("Fri"))
            text="Friday";


        return text;
    }

    private static String settingUpMonth(String text)
    {
        if (text.equals("Jan"))
            text="January";
        else if (text.equals("Feb"))
            text="February";
        else if (text.equals("Mar"))
            text="March";
        else if (text.equals("Apr"))
            text="April";
        else if (text.equals("May"))
            text= "May";
        else if (text.equals("Jun"))
            text="June";
        else if (text.equals("Jul"))
            text="July";
        else if (text.equals("Aug"))
            text="August";
        else if (text.equals("Sep"))
            text="September";
        else if (text.equals("Oct"))
            text="October";
        else if (text.equals("Nov"))
            text= "November";
        else if (text.equals("Dec"))
            text="December";


        return text;
    }

    /* SYSTEM DATE METHOD */
    public static String getdate() {

        Date date = new Date();

        return date.toString();
    }

    /* SYSTEM DATE METHOD FINISHED */

    /* SYSTEM DAY IN STRING */
    public static String GetDay() {
        String date = getdate();
        String day = date.substring(0, 3);

        return settingUpDay(day);
    }

    /* SYSTEM DAY IN STRING METHOD FINISHED */

    /* SYSTEM TIME IN STRING METHOD */
    public static String getTime() {
        String date = getdate().substring(11, 20);


        return date;
    }
    /* SYSTEM TIME IN STRING METHOD FINISHED */

    public static String getMonth() {
        String date = getdate();
        String month = date.substring(4,7);

        return settingUpMonth(month);
    }
    public static String getYear() {
        String date = getdate();
        String year = date.substring(24,28);

        return year;
    }



    public  static float TimeToDecimalConverter(String time)
    {

        String t[] = ZeroRemover(time).split(":");
        String  a = "." + t[1];




        float n = Float.parseFloat(t[0]) + Float.parseFloat(a);

        return n;
    }

    public static String ZeroRemover(String time)
    {
        String[] t = time.split(":");
        if ((t[0].charAt(0)) == '0')
        {
            t[0] = String.valueOf(t[0].charAt(1));
            return  t[0] + ":" + t[1];
        }

        else
            return time;

    }


    public static float twentyfourhourTime(String time) {


        time = ZeroRemover(time);

        String t[] = time.split(":");

        time = t[0] + "." +t[1];

        return Float.parseFloat(time);
    }


    public static String fixtwetyfourhourtime(String time)
    {

        String[] t = time.split(":");
        if (t[0].equals("13"))
        {
            t[0] = "01";
        }
        else if (t[0].equals("14"))
        {
            t[0] = "02";
        }
        else if (t[0].equals("15"))
        {
            t[0] = "03";
        }
        else if (t[0].equals("16"))
        {
            t[0] = "04";
        }
        else if (t[0].equals("17"))
        {
            t[0] = "05";
        }
        return t[0] + ":" +t[1];
    }
}

