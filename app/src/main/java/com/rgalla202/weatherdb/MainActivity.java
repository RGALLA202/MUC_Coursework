package com.rgalla202.weatherdb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rgalla202.weatherdb.R;
import com.rgalla202.weatherdb.weatherLocationDBMgr;
import java.io.IOException;
import java.util.List;

/**
 * Created by rgall on 10/11/2016.
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

        Spinner spin;
        Button threeDF;
        String listItem, result, savedPref, favResult ;
        String chosenLocation = null;
        weatherLocationDBMgr db = new weatherLocationDBMgr(this,"weatherlocations.s3db",null,1);
        TextView fav;
        Button btnFav;
        SharedPreferences sp;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            spin = (Spinner)findViewById(R.id.spinner);
            spin.setOnItemSelectedListener(this);
            loadSpinnerData();

            //3 day forecast Button
            threeDF = (Button)findViewById(R.id.btnthreedf);
            threeDF.setOnClickListener(this);
            //Favourite Location Button
            btnFav = (Button)findViewById(R.id.btnFavForecast);
            btnFav.setOnClickListener(this);

            fav=(TextView)findViewById(R.id.lblSavedFav);
            sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            savedPref =(sp.getString("fav", null));
            if(savedPref!=null)//for favourite location - search database if not null
            {
                favResult = db.findURL(savedPref);
                Log.d("resultFav", favResult);
                fav.setText("Your favourite Locaion is: " + savedPref);
            }
            else
            {
                fav.setText("Go to Settings to set a favourite Location");
            }
        }

    private void loadSpinnerData() {
        // database handler
        try {
            db.dbCreate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Spinner Drop down elements
        List<String> places = db.getAllPlaces();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, places);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spin.setAdapter(dataAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Action View
        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners
        //return super.onCreateOptionsMenu(menu);
        return true;

    }

    // Determines if Action bar item was selected. If true then do corresponding action.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_about:
                ActionBarUtils.alertDialogShow(MainActivity.this);
                return true;

            case R.id.action_settings:
                ActionBarUtils.goToSettings(MainActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // On selecting a spinner item
            listItem = parent.getItemAtPosition(position).toString();
            result = db.findURL(listItem);
            chosenLocation = listItem;

        }
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            System.out.println("In OnClick");
            //Check has the use made a choice of location
            if (listItem == "Please select a location...")
            {
                Toast.makeText(this, "Please select a location from the dropdown list", Toast.LENGTH_LONG).show();
            }
            else
            {
                //Switch statment for multiple buttons
                switch (v.getId())
                {
                    case R.id.btnthreedf:
                        try
                        {
                            if(checkInternet.isNetworkStatusAvialable (getApplicationContext())) {
                                Toast.makeText(getApplicationContext(), "internet avialable", Toast.LENGTH_SHORT).show();
                                //Intent code
                                Intent intent = new Intent(this, ThreeDayForecastActivity.class);
                                Intent intent2 = new Intent(this, MoreInfoActivity.class);

                                System.out.println("In btnThreeDF");
                                Log.e("mytag", "In btn3");

                                intent.putExtra("URL", result);
                                intent.putExtra("chosenLocation", chosenLocation);
                                intent2.putExtra("chosenLocation2", chosenLocation);
                                startActivity(intent);


                            } else {
                                //Toast.makeText(getApplicationContext(), "Internet is not avialable", Toast.LENGTH_SHORT).show();
                                checkInternet.noInternet(MainActivity.this);
                            }
                        }
                        catch(Exception e)
                        {
                            Log.e("error", "no internet");
                        }
                    break;
                    case R.id.btnFavForecast:
                        try
                        {
                            if(checkInternet.isNetworkStatusAvialable (getApplicationContext())) {
                                if(savedPref != null)
                                {
                                    Toast.makeText(getApplicationContext(), "internet avialable", Toast.LENGTH_SHORT).show();
                                    //Intent code
                                    Intent intent = new Intent(this, ThreeDayForecastActivity.class);
                                    Intent intent2 = new Intent(this, MoreInfoActivity.class);

                                    System.out.println("In btnThreeDF");
                                    Log.e("mytag", "In btn3");

                                    intent.putExtra("URL", favResult);
                                    intent.putExtra("chosenLocation", savedPref);
                                    intent2.putExtra("chosenLocation2", savedPref);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "No Favourite Location set", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                //Toast.makeText(getApplicationContext(), "Internet is not avialable", Toast.LENGTH_SHORT).show();
                                checkInternet.noInternet(MainActivity.this);
                            }
                        }
                        catch(Exception e)
                        {
                            Log.e("error", "no internet");
                        }
                    break;
                }
            }
        }

    }


