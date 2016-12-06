package com.rgalla202.weatherdb;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.rgalla202.weatherdb.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rgalla202.weatherdb.ThreeDayForecastActivity.moreInfoLocation;


public class MoreInfoActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String location = "location goes here";
    String day;
    String minTemp;
    String maxTemp;
    String description;
    String weather;
    String latlng;
    static double Lng;
    static double Lat;
    static double currentLNG;
    static double currentLAT;
    private GoogleMap map;
    GoogleApiClient mGoogleApiClient;

    TextView lblTitle;
    ImageView icon;
    Button btnChangeMap;
    AlertDialog mapChangerDialog;
    TextView lblDistance;


    //variables for substring description
    Pattern p;
    Matcher m;
    String start = ":";
    String end = ",";

    String wD;
    String wS;
    String vis;
    String pre = "";
    String hum = "";
    String uV = "";
    String pol = "";
    String sunR = "";
    String sunS = "";
    int count = 0;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        //set up XML elements
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        lblDistance = (TextView)findViewById(R.id.distance);
        icon = (ImageView) findViewById(R.id.img_icon);
        btnChangeMap = (Button) findViewById(R.id.btnMapType);
        btnChangeMap.setOnClickListener(this);
        //get intents from three day forecast
        day = getIntent().getStringExtra("day");
        maxTemp = getIntent().getStringExtra("maxTemp");
        minTemp = getIntent().getStringExtra("minTemp");
        description = getIntent().getStringExtra("desc");
        weather = getIntent().getStringExtra("weather");
        latlng = getIntent().getStringExtra("GeoRSS");
        //is there another way to pass location from main activity to threeDF to MoreInfoActivity?
        location = ThreeDayForecastActivity.moreInfoLocation;
        whatIcon();
        separateDesc();
        separateLatLng();
        displayInfo();
        setUpMap();

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        Log.d("testgeoRSS", latlng);
    }

    /**
     * separates the Lat Lng of the GeoRSS which was passed as an intent from the previous activity.
     */
    private void separateLatLng() {
        int space = latlng.indexOf(" ");
        String lt;
        String lg;
        lt = latlng.substring(0, space);
        Lat = Double.parseDouble(lt);
        lg = latlng.substring(space + 1);
        Lng = Double.parseDouble(lg);
        Log.d("lng,lat", Lat + "," + Lng);
    }

    /**
     * Sets the Icon To be displayed
     */
    private void whatIcon() {
        switch (weather) {
            case "Clear Sky":
                icon.setImageResource(R.drawable.clear_sky);
                break;
            case "Fog":
                icon.setImageResource(R.drawable.fog);
                break;
            case "Heavy Rain":
                icon.setImageResource(R.drawable.heavy_rain);
                break;
            case "Heavy Rain Shower":
                icon.setImageResource(R.drawable.heavy_rain_shower);
                break;
            case "Heavy Snow":
                icon.setImageResource(R.drawable.heavy_snow);
                break;
            case "Light Cloud":
                icon.setImageResource(R.drawable.light_cloud);
                break;
            case "Light Rain":
                icon.setImageResource(R.drawable.light_rain);
                break;
            case "Light Rain Shower":
                icon.setImageResource(R.drawable.light_rain_shower_day);
                break;
            case "Light Snow":
                icon.setImageResource(R.drawable.light_snow);
                break;
            case "Mist":
                icon.setImageResource(R.drawable.mist);
                break;
            case "Partly Cloudy":
                icon.setImageResource(R.drawable.partly_cloudy);
                break;
            case "Sleet":
                icon.setImageResource(R.drawable.sleet);
                break;
            case "Sunny":
                icon.setImageResource(R.drawable.sunny);
                break;
            case "Sunny Intervals":
                icon.setImageResource(R.drawable.sunny_intervals);
                break;
            case "Thick Cloud":
                icon.setImageResource(R.drawable.thick_cloud);
                break;
            case "Thunderstorm":
                icon.setImageResource(R.drawable.thunderstorm);
                break;
            case "Thundery Shower":
                icon.setImageResource(R.drawable.thundery_shower);
                break;
            default:
                icon.setImageResource(R.drawable.placeholder);
                break;
        }
    }

    /**
     * Splits full description into separate strings to be displayed later in a listview,
     * by doing this there is more control over how the data is displayed.
     */
    private void separateDesc() {
        p = Pattern.compile(Pattern.quote(start) + "(.*?)" + Pattern.quote(end));
        m = p.matcher(description);
        while (m.find()) {
            count = count + 1;
            String temp = m.group(1);
            switch (count) {
                case 1:
                    wD = "Wind Direction: " + temp;
                    Log.d("assigned_wd", wD);
                    break;
                case 2:
                    wS = "Wind Speed: " + temp;
                    Log.d("assigned_ws", wS);
                    break;
                case 3:
                    vis = "Visibility: " + temp;
                    Log.d("assigned_vis", vis);
                    break;
                case 4:
                    pre = "Pressure: " + temp;
                    Log.d("assigned_pre", pre);
                    break;
                case 5:
                    hum = "Humidity: " + temp;
                    Log.d("assigned_hum", hum);
                    break;
                case 6:
                    uV = "UV Risk: " + temp;
                    Log.d("assigned_uV", uV);
                    break;
                case 7:
                    pol = "Pollution: " + temp;
                    Log.d("assigned_pol", pol);
                    break;
                case 8:
                    sunR = "Sun Rise: " + temp;
                    Log.d("assigned_sunR", sunR);
                    break;
                case 9:
                    sunS = "Sun Set: " + temp;
                    Log.d("assigned_sunS", sunS);
                    break;
                default:
                    Log.d("default", "default");
                    break;
            }
        }
    }

    /**
     * Displays the text and images
     */
    private void displayInfo() {
        //displays "title" main info of page
        lblTitle.setText("More Details: \r\n" + "It is " + weather + " in \r\n" + location + " on \r\n" + day);
        //displays the new strings (details) which were split from description into a list view.
        listview = (ListView) findViewById(R.id.listview);
        String[] details = {wD, wS, vis, pre, hum, uV, pol, sunR, sunS};
        listview.setAdapter(new MoreInfoCustomRowAdapter(this, details));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more_info, menu);
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
                //Takes user to the main Activity
                ActionBarUtils.goHome(MoreInfoActivity.this);
                return true;
            case R.id.action_refresh:
                //Refreshes the activity, user is informed to do this if there is no internet connection
                //ActionBarUtils.goRefresh(MoreInfoActivity.this);
                finish();
                startActivity(getIntent());
                return true;
            case R.id.action_about:
                //Displays an about Dialog
                ActionBarUtils.alertDialogShow(MoreInfoActivity.this);
                return true;
            case R.id.action_settings:
                //Takes user to the Settings Activity
                ActionBarUtils.goToSettings(MoreInfoActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng MarkerPoint = new LatLng(Lat, Lng);
        map = googleMap;
        Toast.makeText(this, MarkerPoint.toString(), Toast.LENGTH_LONG).show();
        if (map != null) {
            MarkerOptions options = new MarkerOptions();
            options.title(location);
            options.snippet(weather);
            options.position(MarkerPoint);

            double radiusInMeters = 6437.38; // 4 Mile Radius to help represent the area of description
            //red outline
            int strokeColor = 0xffff0000;
            //opaque red fill
            int shadeColor = 0x44ff0000;
            CircleOptions circleOptions = new CircleOptions().center(MarkerPoint).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
            if(checkInternet.isNetworkStatusAvialable (getApplicationContext()))
            {
                //adds radius circle on top of chosen location marker
                Circle mCircle = map.addCircle(circleOptions);
                //adds chosenlocation marker to the map
                map.addMarker(options);
                //Sets map type initially to Normal - user can also choose what type they want
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //Sets the marker position to chosen location
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(MarkerPoint, 10));
                Toast.makeText(getApplicationContext(), "internet avialable", Toast.LENGTH_SHORT).show();

            }
            else
            {
                checkInternet.noInternetMap(MoreInfoActivity.this);
            }
        }
    }
    private void setUpMap()
    {
        MapFragment mapLocation = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapLocation.getMapAsync(this); //Create the map and apply to the  variable
    }


    @Override
    public void onClick(View v)
    {
       //Switch statment for multiple buttons (possible future implementations)
        switch (v.getId())
        {
            case R.id.btnMapType:
            mapChanger();
            break;
        }
    }

    /**
     * Displays a dialog box containing a list of radio buttons
     * User can make a choice and the map shall update accordingly.
     */
    private void mapChanger()
    {
        // Strings to Show In Dialog with Radio Buttons
        String[] types = {" Default - Normal "," None "," Hybrid "," Satellite", "Terrain"};
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select The Map Type");
        builder.setSingleChoiceItems(types, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item)
            {
                //switch for different map types
                //reload map when selected
                switch(item)
                {
                    case 0:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        map.setMapType(GoogleMap.MAP_TYPE_NONE);
                        break;
                    case 2:
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 3:
                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 4:
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    default:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                }
                mapChangerDialog.dismiss();
            }
        });
        mapChangerDialog = builder.create();
        mapChangerDialog.show();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mLastLocation;
        //adds current location layer button to map, when clicked moveCamera to user location
        //Check that current location permission has been accepted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if(checkInternet.isNetworkStatusAvialable (getApplicationContext()))
            {
                map.setMyLocationEnabled(true);
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null)
                {
                    //gets the lat and lng of user location
                    currentLAT = mLastLocation.getLatitude();
                    currentLNG = mLastLocation.getLongitude();

                    //adds a blue marker to users current approximate location
                    LatLng CurrentPoint = new LatLng(currentLAT, currentLNG);
                    MarkerOptions cOptions = new MarkerOptions();
                    cOptions.title("You are Here");
                    cOptions.snippet("This is a rough estimation");
                    cOptions.position(CurrentPoint);
                    cOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                        map.addMarker(cOptions);

                    //this is the same lat lng as the chosenlocation marker
                    LatLng destination = new LatLng(Lat,Lng);

                    //Calculating the distance in meters
                    Double distanceMeters = SphericalUtil.computeDistanceBetween(CurrentPoint, destination);
                    //convert to miles
                    Double distanceMiles = (distanceMeters / 1609.344);
                    //call method to round distance to 2 decimal places for nicer display
                    distanceMiles = ModifyOutput.round(distanceMiles,2);
                    Log.d("distance_Miles_rounded", ""+distanceMiles);
                    //Displaying the distance
                    lblDistance.setText("You are " + distanceMiles+" Miles away from " + location + " (As the crow flies)");
                    //draw a line between two points
                    PolylineOptions line= new PolylineOptions()
                                    .add(CurrentPoint,destination)
                                    .width(5)
                                    .color(Color.RED);
                    map.addPolyline(line);
                }
            }
        }
        else
        {
            //If curent Location Permissions are not enabled display the following
            Toast.makeText(this, "Current location not enabled", Toast.LENGTH_LONG).show();
            lblDistance.setText("Current Location could not be established - it may be disabled");

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("connectSuspended", "Suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("connectFailed", "failed");
    }
}
