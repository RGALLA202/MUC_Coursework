package com.rgalla202.weatherdb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;import com.rgalla202.weatherdb.R;

public class ThreeDayForecastActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    private String sourceListingURL;
    private String chosenLocation;

    static String moreInfoLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threedayforecast);

        // get location and url intent
        TextView lblTitle = (TextView) findViewById(R.id.lbltitle);
        sourceListingURL = getIntent().getStringExtra("URL");
        chosenLocation = getIntent().getStringExtra("chosenLocation");
        moreInfoLocation = chosenLocation;
        lblTitle.setText("Three Day Forecast: " + chosenLocation);
        Log.d("URL",sourceListingURL);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        //set up AsyncTask
        AsyncReadRss asyncReadRss= new AsyncReadRss(this, recyclerView);
        asyncReadRss.chosenLocation(sourceListingURL);
        //starts Async
        asyncReadRss.execute();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_child_activities, menu);
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
                //startActivity(new Intent(this, mainActivity.class));
                ActionBarUtils.goHome(ThreeDayForecastActivity.this);

                return true;
            case R.id.action_about:

                ActionBarUtils.alertDialogShow(ThreeDayForecastActivity.this);
                return true;

            case R.id.action_settings:
                Toast toast = Toast.makeText(this, "you clicked settings", Toast.LENGTH_LONG);
                toast.show();
                ActionBarUtils.goToSettings(ThreeDayForecastActivity.this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
