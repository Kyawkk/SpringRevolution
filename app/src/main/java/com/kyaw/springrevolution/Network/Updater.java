package com.kyaw.springrevolution.Network;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.kyaw.springrevolution.Interface.UpdateListener;
import com.kyaw.springrevolution.Model.UpdaterModel;
import com.kyaw.springrevolution.Utils.Constants;

public class Updater extends AsyncTask<Boolean, Void, Boolean> {

    private UpdateListener listener;
    private final boolean isAvailable = false;
    private Activity activity;

    public Updater(UpdateListener listener) {
        this.listener = listener;
    }

    public Updater() {
    }

    public Updater(UpdateListener listener, Activity activity) {
        this.listener = listener;
        this.activity = activity;
    }

    public Updater(Activity context) {
        this.activity = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Boolean... voids) {
        UpdaterModel model = null;
        try {
            String json = Constants.readUrl(Constants.UPDATE_URL);
            Gson gson = new Gson();
            model = gson.fromJson(json, UpdaterModel.class);
            System.out.println("is listener null ? " + (listener == null));
            if (listener != null) {
                listener.onSuccess(model);
            }
            if (activity != null) {
                if (!model.getVersion().equals(Constants.versionCode)) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Constants.showUpdateDialog(activity);
                        }
                    });
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onError();
            }
            e.printStackTrace();
        }
        System.out.println("do in background");
        if (model!= null)
            return !Constants.versionCode.equals(model.getVersion());
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isAvailable) {
        super.onPostExecute(isAvailable);
        isAvailable(isAvailable);
    }

    public boolean isAvailable(Boolean... booleans) {
        if (booleans.length != 0)
            return booleans[0];
        else doInBackground();
        return false;
    }
}
