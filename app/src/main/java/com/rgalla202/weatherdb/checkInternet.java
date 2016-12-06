package com.rgalla202.weatherdb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rgall on 04/12/2016.
 */

public class checkInternet {

    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }
    public static void noInternet(Context context)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Notification");

        alertDialog.setMessage("No Internet Available. An internet connection is required to fetch weather data:\n\b\n" +
                "\b"+
                "Check you have mobile data turned on. \n\b" +
                "or Check you are in a wifi zone. \n\bSorry for any inconvenience ");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public static void noInternetMap(Context context)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Notification");

        alertDialog.setMessage("No Internet Available. An internet connection is required Display the Map:\n\b\n" +
                "\b"+
                "Check you have mobile data turned on. \n\b" +
                "or Check you are in a wifi zone. Then Select Refresh. \n\bSorry for any inconvenience ");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
