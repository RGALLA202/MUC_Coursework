package com.rgalla202.weatherdb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by rgall on 19/11/2016.
 * The following class methods are available for the whole app,
 * they are called from the actionbar which remains consistent throughout the app
 */

public class ActionBarUtils {
    /**
     * Creates dialog box and displays on the activity currently used
     * @param context
     */
    public static void alertDialogShow(Context context)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
         alertDialog.setTitle("About");

        alertDialog.setMessage("This app gets data from the BBC weather RSS feeds. By Ryan Gallacher S1313462");
         alertDialog.setButton("OK", new DialogInterface.OnClickListener()
         {
         public void onClick(DialogInterface dialog, int which)
         {
         alertDialog.dismiss();
         }
         });
         alertDialog.show();
    }

    /**
     * Creates an intent and sends user to mainActivity from current activity
     * @param context
     */
    public static void goHome(Context context)
    {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * Creates an intent and sends user to SettingsActivity from current activity
     * @param context
     */
    public static void goToSettings(Context context)
    {
        Intent intent = new Intent(context,SettingsActivity.class);
        context.startActivity(intent);
    }
}
