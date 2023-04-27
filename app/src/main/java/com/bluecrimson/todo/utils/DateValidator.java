package com.bluecrimson.todo.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateValidator {
    public static boolean isValidDate(String inDate) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static boolean isStartDateLessThanEndDate(String startDate, String endDate) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date sd = simpleDateFormat.parse(startDate);
            Date ed = simpleDateFormat.parse(endDate);

            assert sd != null;
            int result = sd.compareTo(ed);

            return result < 0;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertDateString(String dateString) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        DateFormat outputDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        try {
            Date date = inputDateFormat.parse(dateString);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
