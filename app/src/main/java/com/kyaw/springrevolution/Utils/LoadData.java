    package com.kyaw.springrevolution.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kyaw.springrevolution.Interface.DataLoadingListener;
import com.kyaw.springrevolution.Model.Post;
import com.kyaw.springrevolution.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LoadData extends AsyncTask<Integer,Integer,JSONObject>{
    private Context context;
    private static ArrayList<Post> posts = new ArrayList<>();
    private String webUrl;
    private HttpURLConnection connection = null;
    private BufferedReader reader = null;
    private ProgressDialog dialog;
    private dataCallBack callBack = null;
    private DataLoadingListener dataLoadingListener = null;
    private LinearLayout container;
    private static int size = 0;

    public LoadData(Context context, dataCallBack callBack, String webUrl, LinearLayout container, LinearProgressIndicator loading) {
        this.context = context;
        this.webUrl = webUrl;
        this.callBack = callBack;
        this.container = container;
        this.loading = loading;
    }

    private LinearProgressIndicator loading;

    public LoadData(Context context,dataCallBack callBack,String webUrl) {
        this.context = context;
        this.callBack = callBack;
        this.webUrl = webUrl;
    }

    public LoadData(DataLoadingListener dataLoadingListener){
        this.dataLoadingListener = dataLoadingListener;
    }

    public LoadData(Context context,String webUrl){
        this.context = context;
        this.webUrl = webUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /*dialog = new ProgressDialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;
        dialog.setMessage("Getting Data...");
        dialog.setCanceledOnTouchOutside(false);*/
        //dialog.show();
    }

    @Override
    protected JSONObject doInBackground(Integer... integers) {
        URL url = null;
        try {
            if (webUrl != null){
                url = new URL(webUrl);
            }
            else {
                url = new URL(Constants.MAIN_URL);
            }
            connection = (HttpURLConnection) url.openConnection();

            connection.connect();
            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                size = buffer.length();
                if (buffer.length() == 0) {
                    return null;
                }

                return new JSONObject(buffer.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        System.out.println("is null: "+(dataLoadingListener == null));
        if (dataLoadingListener != null){
            dataLoadingListener.onFinished();
        }
        posts.clear();/*
        if (dialog.isShowing()) {
            dialog.dismiss();
        }*/
        try {
            JSONArray array = jsonObject.getJSONArray("posts");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String timestamp = object.getString("taken_at_timestamp");
                String desc = String.valueOf(object.get("description"));
                String image = String.valueOf(object.get("display_image"));
                String video = String.valueOf(object.get("video_url"));
                String human_date = String.valueOf(object.get("human_date"));
                boolean is_cover = object.getBoolean("is_cover");
                int ig_user = object.getInt("ig_user");

                //add data to posts list
                posts.add(new Post(timestamp,desc,image,video,human_date,is_cover,ig_user));
            }
            if (callBack != null){
                callBack.onReceived(posts);
            }
        }
        catch (NullPointerException exception){
            if (dialog!= null){
                dialog.setMessage("Something went wrong!");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        //stop loading
        if (loading != null){
            loading.setIndicatorColor(context.getResources().getColor(R.color.purple_500));
            container.setVisibility(View.GONE);
        }
    }
}


