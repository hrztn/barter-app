package com.example.barterapp.modules;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.barterapp.R;

public class LoadSpinner {
    private Activity activity;
    private AlertDialog alertDialog;

    public LoadSpinner(Activity myActivity){
        activity = myActivity;
    }

    public void startLoadingAnimation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.spinner_loading, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void stopLoadingAnimation(){
        alertDialog.dismiss();
    }
}
