package id.my.match.parkir.utility.datetime;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by admin on 30/03/2018.
 */

public class DateTime_Format {
    public static String Date_Format(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) {
            return "";
        }

        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat convetTimeFormat = new SimpleDateFormat("hh:mm a");

        String hasil="";
        try {
            String dateString = convetDateFormat.format(date);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date2 = sdf.parse(dateString);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yy");
            String formattedDate = outputFormat.format(date2);

            hasil = formattedDate;

            Log.d(TAG, "Got the date: " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return hasil;
    }

    public static String DateTime_Format(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (date == null) {
            return "";
        }

        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat convetTimeFormat = new SimpleDateFormat("hh:mm a");

        String hasil="";
        try {
            String dateString = convetDateFormat.format(date);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date2 = sdf.parse(dateString);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yy");
            String formattedDate = outputFormat.format(date2);

            hasil = formattedDate + ", "+convetTimeFormat.format(date);

            Log.d(TAG, "Got the date: " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return hasil;
    }

    public static String DateTimeDay_Format(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (date == null) {
            return "";
        }

        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String hasil="";
        try {
            String dateString = convetDateFormat.format(date);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date2 = sdf.parse(dateString);

            Locale myLocale = new Locale(Locale.getDefault().getLanguage());

            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", myLocale);
            String formattedDate = outputFormat.format(date2);

            hasil = formattedDate;

            Log.d(TAG, "Got the date: " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return hasil;
    }

    public static String Age_Format(String inputDate) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return "";

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return String.valueOf(age);
    }
}
