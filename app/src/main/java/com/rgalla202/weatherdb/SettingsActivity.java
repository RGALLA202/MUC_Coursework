package com.rgalla202.weatherdb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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

import java.util.List;

/**
 * Created by rgall on 04/12/2016.
 */

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    Spinner spinPref;
    Button btnSave;
    String favLocation;
    TextView saveFav;
    weatherLocationDBMgr db = new weatherLocationDBMgr(this,"weatherlocations.s3db",null,1);
    SharedPreferences sp;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.d("prefTest", "1");
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        saveFav = (TextView) findViewById(R.id.lblSavedFav);
        spinPref = (Spinner)findViewById(R.id.prefSpin);
        spinPref.setOnItemSelectedListener(this);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        // Spinner Drop down elements
        List<String> places = db.getAllPlaces();
        Log.d("prefTest", "getallPlaces");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, places);
        Log.d("prefTest", "adapter");


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d("prefTest", "setDropdown");

        // attaching data adapter to spinner
        spinPref.setAdapter(dataAdapter);
        loadPrefs();
        Log.d("prefTest", "setAdapter");

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        favLocation = parent.getItemAtPosition(position).toString();
        Log.d("prefTest", "onItemselected");
        //showing selected spinner item
       // Toast.makeText(parent.getContext(), "You selected: " + favLocation, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        savePrefs("fav", favLocation);
        Log.d("prefTest", "onClick");
        loadPrefs();
        ActionBarUtils.goHome(SettingsActivity.this);
    }

    /**
     * Loads the previously saved preference
     * this variable can now be displayed
     */
    private void loadPrefs()
    {
        String favPlace = sp.getString("fav", null);
        saveFav.setText("Your Favourite Location is: " + favPlace);
    }

    /**
     * Saves the chosen location as a shared preference
     * value is the string output from selecting a location from the spinner
     * @param key
     * @param value
     */
    private void savePrefs(String key, String value)
    {
        SharedPreferences.Editor edit = sp.edit();
        Log.d("prefTest", "save1");

        edit.putString(key, value);
        Log.d("prefTest", "save2");

        edit.commit();
        Log.d("prefTest", "commit");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        // Action View
        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners
        //return super.onCreateOptionsMenu(menu);
        return true;

    }

    /**
     * Determines if Action bar item was selected. If true then do corresponding action.
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_home:
                ActionBarUtils.goHome(SettingsActivity.this);
                return true;

            case R.id.action_about:
                ActionBarUtils.alertDialogShow(SettingsActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
