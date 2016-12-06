package com.rgalla202.weatherdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.R.attr.version;

/**
 * Created by rgall on 27/11/2016.
 */

public class weatherLocationDBMgr extends SQLiteOpenHelper
{

    private static final int DB_VER = 1;
    private static final String DB_PATH =  "data/data/com.rgalla202.weatherdb/databases/";
    private static final String DB_NAME = "weatherlocations.s3db";
    private static final String TBL_locations = "locations";

    public static final String COL_ID = "id";
    public static final String COL_place = "place";
    public static final String COL_URL = "url";

    private final Context appContext;

    public weatherLocationDBMgr(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        //assigns app context to the variable appContext
        this.appContext = context;
    }

    //=================================================
    //METHOD ONLY CALLED THE FIRST TIME THE APP IS RUN.
    //=================================================
    @Override
    public void onCreate(SQLiteDatabase db) {
    String CREATE_LOCATIONS_Table = "CREATE TABLE IF NOT EXISTS " +
            TBL_locations + "("
            + COL_ID + "INTEGER PRIMARY KEY,"
            + COL_place + "TEXT,"
            + COL_URL + "TEXT" + ")";
        db.execSQL(CREATE_LOCATIONS_Table);
    }

    //================================================
    //SHOULD ONLY BE CALLED WHEN DB NEEDS TO BE UPGRADED,
    // ACHIVED BY CHANGING DB VERSION WHEN MAKING THE INITIAL CALL (SEE LATER)
    //================================================
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(newVersion > oldVersion){
            try {
            this.dbCreate();
            }catch (Exception e){
                e.printStackTrace();
            }
            }
        }


    //BOTH THE ABOVE FUNCTIONS ARE BASED ON THE SQL

    //================================================
    //Creates an empty database on the system and rewrites it with your own database.
    //instigate the copy of the database from the Assets folder to the
    //correct location on the device
    //================================================
    public void dbCreate() throws IOException
    {
        boolean dbExist = dbCheck();

        if(!dbExist)
        {
            //By calling this method an empty database will be created into the default system patt
            //of the application so we can overwrite that db with our db.
            this.getReadableDatabase(); // create a new empty database in the correct path
            try
            {
                //copy the data from the database in the Assets folder to the new empty database
                copyDBFromAssets();
            }
            catch(IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if database exists to avoid recopying the file each time you open app
     * @return true if exists, false if it doesnt
     */
    private boolean dbCheck() {
        SQLiteDatabase db = null;
        try
        {
            String dbPath = DB_PATH + DB_NAME;
            Log.e("database","path created ");
            Log.d("dbPath", dbPath);
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            Log.e("database","open database ");
            db.setLocale(Locale.getDefault());
            Log.e("database","set locale ");
            db.setVersion(1);
            Log.e("database","version set");
        }catch (SQLiteException e)
        {
            e.printStackTrace();
            Log.d("SQLHelper", "couldnt find database");

        }
        if (db !=null)
        {
            db.close();
        }
        return db != null ? true : false;
    }

    /**
     * Copies db from local assets folder to the just created empty database in the system folder,
     * from where it can be accessed an handled.
     * This is done b transfering bytstream
     * @throws IOException
     */
    private void copyDBFromAssets() throws IOException
    {
        InputStream dbInput = null;
        Log.e("Openassets","arghh");
        OutputStream dbOutput = null;
        Log.e("Openassets","arghh");
        String dbFileName = DB_PATH +DB_NAME;
        try
        {
            Log.e("Openassets","arghh");
            dbInput = appContext.getAssets().open(DB_NAME);
            dbOutput = new FileOutputStream(dbFileName);
            //transfer bytes from db Input to the db Output
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dbInput.read(buffer))>0)
            {
                dbOutput.write(buffer, 0, length);
            }
            //close the stream
            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        }catch(IOException e)
        {
            throw new Error("Problems copying DB");
        }
    }

     /**
     * Adds new record to db
     * @param alocationInfo
     */
    public void addLocationInfo(locationInfo alocationInfo)
    {
        ContentValues values= new ContentValues();
        values.put(COL_place, alocationInfo.getPlace());
        values.put(COL_URL, alocationInfo.getUrl());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TBL_locations, null, values);
        db.close();
    }

    /**
     *This method will be used in main activity to find corresponding url for chosen location.
     *constructs SQL query string, opens db, uses db RAWQuery method to execute query
     *cursor is a means for accessing the correct record in db.
     * @param aplace
     * @return
     */
    public String findURL(String aplace)
    {
        String URL = "";
        // Select url Query
        String selectQuery = "SELECT url  FROM " + TBL_locations + " where place = " + "'" +aplace+"'" + ";";
        Log.d("1","testurl");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("2","test");
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            Log.d("3","test");

            do {
                URL = cursor.getString(0);
                Log.d("4","test");

            } while (cursor.moveToNext());
            Log.d("5","test");
        }
        else
        {
            Log.d("something happened?","test");
        }
        // closing connection
        cursor.close();
        Log.d("6","test");
        db.close();
        Log.d("7","test");
        Log.d("testreturn", URL.toString());
        return URL;
    }

    /**
     * This method will be used in main activity to list all places to be displayed.
     * constructs SQL query string, opens db, uses db RAWQuery method to execute query
     * cursor is a means for accessing the correct record in db.
     * @return
     */
    public List<String> getAllPlaces(){
        List<String> places = new ArrayList<String>();

        // Select place Query
        String selectQuery = "SELECT place  FROM " + TBL_locations + ";";
        Log.d("1","test");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("2","test");
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            Log.d("3","test");
            do {
                places.add(cursor.getString(0));
                Log.d("4","test");

            } while (cursor.moveToNext());
            Log.d("5","test");
        }
        else
        {
            Log.d("something happened?","test");
        }
        // closing connection
        cursor.close();
        Log.d("6","test");
        db.close();
        Log.d("7","test");
        Log.d("testreturn", places.toString());
        // returning Places
        return places;
    }

    /**
     * When called this method delets existing db
     * Currently only used for testing to ensure a fresh start when running the app
     */
    public void deletedb()
    {
        this.appContext.deleteDatabase(DB_NAME);
        Log.d("testdeletedb","done");
    }
}



