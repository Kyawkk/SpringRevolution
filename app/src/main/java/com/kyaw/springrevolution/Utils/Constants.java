package com.kyaw.springrevolution.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.kyaw.springrevolution.Model.Duration;
import com.kyaw.springrevolution.Model.Post;
import com.kyaw.springrevolution.R;

import java.util.ArrayList;

public class Constants {
    public static final String MAIN_URL = "https://cms.myanmarwitness.info/wp-json/v1/post?posts_per_page=300&is_cover=true&date[0]=2020-06&date[1]=2022-04";
    public static int getMonth(String date) {
        getDay(date);
        String month = date.substring(5, 7);
        return Integer.parseInt(month);
    }

    public static int getDay(String date){
        String day = date.substring(8,10);
        System.out.println("day: "+day);
        return Integer.parseInt(day);
    }

    public static ArrayList<String> getMonthList(ArrayList<Post> posts) {
        ArrayList<String> months = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            String date = posts.get(i).getHuman_date();
            if (!months.contains(getMonthInName(getMonth(date)))) {
                months.add(getMonthInName(getMonth(date)));
            }
        }
        return months;
    }

    public static ArrayList<ArrayList<Post>> getPostsListInMonth(ArrayList<Post> posts){
        ArrayList<ArrayList<Post>> postListInMonth = new ArrayList<>();
        ArrayList<Integer> months = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            String date = posts.get(i).getHuman_date();
            if (!months.contains(getMonth(date))) {
                months.add(getMonth(date));
            }
        }

        for (int i=0; i<months.size(); i++){
            ArrayList<Post> postList = new ArrayList<>();
            for (int j=0; j<posts.size(); j++){
                if (months.get(i) == getMonth(posts.get(j).getHuman_date())){
                    postList.add(posts.get(j));
                }
            }
            postListInMonth.add(postList);
        }

        return postListInMonth;
    }

    private static String getMonthInName(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return null;
        }
    }

    public static boolean isMyanmarFont(String text){
        String english = "a b c d e f g h i j k l m n o p q r s t u v w x y z";
        boolean isEnglish = false;
        for (int i = 0; i<english.length(); i++){
            if (text.contains(String.valueOf(english.charAt(i)))){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public static void setMyanmarFont(Context context, TextView textView){
        Typeface face = ResourcesCompat.getFont(context, R.font.mm);
        textView.setTypeface(face);
    }

    public static void setEnglishFont(Context context, TextView textView){
        Typeface face = ResourcesCompat.getFont(context, R.font.en_regular);
        textView.setTypeface(face);
    }

    public static String getDuration(long millsecond){
        Duration duration = getTime(millsecond);
        String result = duration.getMinute() + ":" + duration.getSecond();
        return result;
    }

    public static Duration getTime(long millsecond){
        int second = (int) millsecond / 1000;
        int minute = second / 60;
        int remainingSecond = second % 60;
        return new Duration(minute,second);
    }
}
