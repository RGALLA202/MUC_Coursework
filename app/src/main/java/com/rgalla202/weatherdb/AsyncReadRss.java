package com.rgalla202.weatherdb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by rgall on 09/11/2016.
 */

public class AsyncReadRss extends AsyncTask<Void, Void, Void>
{
    Context context;
    ProgressDialog prog;
    String address="";
    URL url;
    ArrayList<RSSItem>rssItems;
    RecyclerView recyclerView;

    public String chosenLocation(String sourceListingURL) {

        address = sourceListingURL;
        return address;
    }

    public AsyncReadRss(Context context, RecyclerView recyclerView)
    {
        this.recyclerView = recyclerView;
        this.context=context;
        prog=new ProgressDialog(context);
        prog.setMessage("Please wait... Getting Data");
    }

    @Override
    protected void onPreExecute() {
        // update the UI immediately after the task is executed
        //display progress dialog to keep user informed until long duration task is complete
        // close dialog in postExecute.
        prog = new ProgressDialog(context);
        prog.setTitle("Fetching data");
        prog.setMessage("Please Wait ...");
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.show();
        //set adapter before

        super.onPreExecute();
    }

    /**
     * dismiss progress dialog and display data in the recyclerView
     * @param aVoid
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        prog.dismiss();
        CustomListAdapter adapter = new CustomListAdapter(context, rssItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new CustomListGap(40) );
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected Void doInBackground(Void... params) {
        //connect to url and parse xml
        ProcessXML(GetData());
        return null;
    }

    /**
     * parses xml
     * @param data
     */
    private void ProcessXML(Document data)
    {
        if (data != null) {
            rssItems = new ArrayList<>();
            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node currentChild = items.item(i);
                if (currentChild.getNodeName().equalsIgnoreCase("item")) {
                    RSSItem item = new RSSItem();
                    NodeList itemchilds = currentChild.getChildNodes();
                    for (int j = 0; j < itemchilds.getLength(); j++) {
                        Node current = itemchilds.item(j);
                        //Log.d("textcontent", current.getTextContent());
                        if (current.getNodeName().equalsIgnoreCase("title")) {
                            Log.d("title: ", current.getTextContent());
                            item.setTitle(current.getTextContent());
                            //======================================== split title string
                            String tempTitle = current.getTextContent();
                            int DayEnd = tempTitle.indexOf(":") ;
                            int WeatherEnd = tempTitle.indexOf(",");
                            //get the day
                            String tempDay = tempTitle.substring(0, DayEnd);
                            Log.d("testTitle: ", tempTitle);
                            Log.d("testDay: ", tempDay);
                            item.setDay(tempDay);
                            //get the weather type
                            String tempWeather = tempTitle.substring(DayEnd + 2, WeatherEnd);
                            Log.d("testweather: ", tempWeather);
                            item.setWeather(tempWeather);
                            //get temperature substing:
                            // get maxtemp in °C - can convert to °F later
                            String temptotalTemp = tempTitle.substring(WeatherEnd+1);
                            int startMax = temptotalTemp.indexOf(":");
                            int endMax = temptotalTemp.indexOf("°C");
                            try
                            {
                                //at some point in the day max temp is removed from the weather rss feed for the current day,
                                // therefore check it exsists when getting the temp
                                if(temptotalTemp.indexOf("Maximum Temperature:")!=-1)
                                {
                                    Log.d("check","max exists");

                                    String tempMax = temptotalTemp.substring(startMax + 1, endMax);
                                    Log.d("testMax: ", tempMax);
                                    //format to user preference
                                   tempMax = tempFormat(tempMax);
                                    item.setMaxTemp(tempMax);
                                    //get min temp
                                    //check for min temp in string;
                                    if(temptotalTemp.indexOf("Minimum Temperature:")!=-1)
                                    {
                                        Log.d("check","min exsists");
                                        String Min= temptotalTemp.substring(endMax+1);
                                        int startMin = Min.indexOf(":");
                                        int endMin = Min.indexOf("°C");
                                        String tempMin = Min.substring(startMin +1, endMin);
                                        Log.d("testMinTemp: ", tempMin);
                                        //format to user preference
                                        tempMin = tempFormat(tempMin);
                                        item.setMinTemp(tempMin);
                                    }
                                    else
                                    {
                                        Log.d("check","min does not exist");
                                        String noMin = "not available";
                                        item.setMinTemp(noMin);
                                    }
                                }
                                else {
                                    Log.d("check","max does not exist");
                                    String noMax = "not available";
                                    item.setMaxTemp(noMax);
                                    int startMin = temptotalTemp.indexOf(":");
                                    int endMin = temptotalTemp.indexOf("°C");
                                    String tempMin = temptotalTemp.substring(startMin +1, endMin);
                                    Log.d("testMinTemp: ", tempMin);
                                    //format to user preference
                                    tempMin = tempFormat(tempMin);
                                    item.setMinTemp(tempMin);
                                }

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        } else if (current.getNodeName().equalsIgnoreCase("link")) {
                            Log.d("link: ", current.getTextContent());
                            item.setLink(current.getTextContent());
                        } else if (current.getNodeName().equalsIgnoreCase("description")) {
                            Log.d("description: ", current.getTextContent());
                            try
                            {
                                String descTemp = current.getTextContent();
                                int subDesc = descTemp.indexOf("Wind");
                                descTemp = descTemp.substring(subDesc)+",";
                                Log.d("descTemp",descTemp);
                                item.setDescription(descTemp);
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        } else if (current.getNodeName().equalsIgnoreCase("pubDate")) {
                            Log.d("pubDate: ", current.getTextContent());
                            item.setPubDate(current.getTextContent());
                        } else if (current.getNodeName().equalsIgnoreCase("georss:point")) {
                            Log.d("georss: ", current.getTextContent());
                            item.setGeoRSS(current.getTextContent());
                        }
                    }
                    rssItems.add(item);
                }
            }

        }
    }

    /**
     *  Format temperature to °C
     *  future updates add settings and choose between °C or °F
     * @param temp
     * @return
     */
    private String tempFormat(String temp) {
        temp =  temp + "°C";
        Log.d("testFormat",temp);
        return temp;
    }

    /**
     * connects to web resource
     * @return
     */
    public Document GetData()
    {
        try
        {
            url = new URL(address);
            url = new URL(address);
            Log.d("test",address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
