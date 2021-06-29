package com.kyaw.springrevolution.CustomView;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.kyaw.springrevolution.BuildConfig;
import com.kyaw.springrevolution.Interface.UpdateListener;
import com.kyaw.springrevolution.Model.UpdaterModel;
import com.kyaw.springrevolution.Network.Updater;
import com.kyaw.springrevolution.R;
import com.kyaw.springrevolution.Utils.Constants;

import java.util.concurrent.ThreadPoolExecutor;

public class UpdateDialog extends Dialog implements UpdateListener {
    private UpdaterModel model;
    private Context context;
    private ImageView close;
    private TextView version,date,developer,description;
    private final Activity activity;
    private MaterialButton check_update;
    private CircularProgressIndicator loading;
    private Updater updater,updateTwo;
    private final String versionCode = BuildConfig.VERSION_NAME;
    private UpdateListener listener;

    public UpdateDialog(@NonNull Activity context) {
        super(context);
        this.activity = context;
    }

    public UpdateDialog(Activity activity,UpdaterModel model){
        super(activity);
        this.activity = activity;
        this.model = model;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.update_dialog);

        updater = new Updater(this,activity);
        listener = this;

        close = findViewById(R.id.close_dialog);
        version = findViewById(R.id.updater_version);
        date = findViewById(R.id.updater_date);
        developer = findViewById(R.id.updater_developer);
        description = findViewById(R.id.updater_desc);
        check_update = findViewById(R.id.check_update_btn);
        loading = findViewById(R.id.update_loading);

        //set transition when layout changes
        ((ViewGroup)findViewById(R.id.root)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        check_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdate();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void checkUpdate(){
        updater.execute();
        check_update.setText("");
        loading.setVisibility(View.VISIBLE);
    }

    private void setData(UpdaterModel model){
        if (model != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    version.setText(model.getVersion());
                    date.setText(model.getDate());
                    description.setText(model.getDescription());
                    developer.setText(model.getDeveloper());
                    loading.setVisibility(View.GONE);
                }
            });
        }
    }

    public static void showUpdateDialog(Context context){
        MaterialButton update;
        if (Looper.myLooper() == null){
            Looper.prepare();
        }
        Dialog dialog = new Dialog(context,R.style.FullWidth_Dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.update_available_dialog,null);
        update = view.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.openPlayStore(context);
            }
        });
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onSuccess(UpdaterModel model) {
        setData(model);
        if (!versionCode.equals(model.getVersion())){
            cancel();
        }
        else {
            System.out.println("update: You are up to date!");
        }
    }

    @Override
    public void onError() {
        check_update.setText("Try Again");
    }
}
