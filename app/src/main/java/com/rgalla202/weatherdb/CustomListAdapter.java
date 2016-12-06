package com.rgalla202.weatherdb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rgalla202.weatherdb.R;import java.util.ArrayList;

/**
 * Created by rgall on 09/11/2016.
 * This is the adapter which sets up the data to be displayed in the CardView/RecyclerView
 */

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.CustomViewHolder>{
    ArrayList<RSSItem> rssItems;
    Context context;
    public CustomListAdapter(Context context, ArrayList<RSSItem>rssItems)
    {
        this.rssItems=rssItems;
        this.context=context;
    }

    /**
     * Creates a custom ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_row, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    /**
     * describes an item view and metadata about its place within the RecyclerView.
     * displays a summery of the data associated to the chosen location
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        final RSSItem current=rssItems.get(position);
        whatImage(holder, position);
        holder.Day.setText(current.getDay());
        holder.Weather.setText(current.getWeather());
        holder.minTemp.setText("Min Temp: " + current.getMinTemp());
        holder.maxTemp.setText("Max Temp: " + current.getMaxTemp());
        //make each row clickabe to take user to the MoreInfoActivity.
        holder.cV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MoreInfoActivity.class);
                intent.putExtra("day", current.getDay());
                intent.putExtra("maxTemp", current.getMaxTemp());
                intent.putExtra("minTemp", current.getMinTemp());
                intent.putExtra("desc", current.getDescription());
                intent.putExtra("weather", current.getWeather());
                intent.putExtra("GeoRSS", current.getGeoRSS());

                context.startActivity(intent);
            }
        });
    }

    /**
     * gets the total number of rows
     * @return
     */
    @Override
    public int getItemCount() {
        return rssItems.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder
    {
        TextView Title,Day, Weather, minTemp, maxTemp, Description,Date;
        ImageView Thumbnail;
        CardView cV;

        /**
         * sets up the customViewHolde with the appropriate dataTypes
         * @param itemView
         */
        public CustomViewHolder(View itemView) {
            super(itemView);
            Date = (TextView) itemView.findViewById(R.id.date_text);
            Thumbnail=(ImageView)itemView.findViewById(R.id.thumb_img);

            //below are the substring title textviews
            Day= (TextView) itemView.findViewById(R.id.day_text);
            Weather=(TextView)itemView.findViewById(R.id.weather_text);
            minTemp=(TextView)itemView.findViewById(R.id.min_temp_text);
            maxTemp=(TextView)itemView.findViewById(R.id.max_temp_text);
            cV= (CardView) itemView.findViewById(R.id.cardview);
        }
    }

    /**
     * gets the corresponding image based on the weathertype variable
     * @param holder
     * @param position
     */
    private void whatImage(CustomViewHolder holder, int position) {
        final RSSItem current=rssItems.get(position);
        holder.Date.setText("Updated on: " + current.getPubDate());
        String weatherType = current.getWeather();
        switch(weatherType)
        {
            case "Clear Sky":
                holder.Thumbnail.setImageResource(R.drawable.clear_sky);
                break;
            case "Fog":
                holder.Thumbnail.setImageResource(R.drawable.fog);
                break;
            case "Heavy Rain":
                holder.Thumbnail.setImageResource(R.drawable.heavy_rain);
                break;
            case "Heavy Rain Shower":
                holder.Thumbnail.setImageResource(R.drawable.heavy_rain_shower);
                break;
            case "Heavy Snow":
                holder.Thumbnail.setImageResource(R.drawable.heavy_snow);
                break;
            case "Light Cloud":
                holder.Thumbnail.setImageResource(R.drawable.light_cloud);
                break;
            case "Light Rain":
                holder.Thumbnail.setImageResource(R.drawable.light_rain);
                break;
            case "Light Rain Shower":
                holder.Thumbnail.setImageResource(R.drawable.light_rain_shower_day);
                break;
            case "Light Snow":
                holder.Thumbnail.setImageResource(R.drawable.light_snow);
                break;
            case "Mist":
                holder.Thumbnail.setImageResource(R.drawable.mist);
                break;
            case "Partly Cloudy":
                holder.Thumbnail.setImageResource(R.drawable.partly_cloudy);
                break;
            case "Sleet":
                holder.Thumbnail.setImageResource(R.drawable.sleet);
                break;
            case "Sunny":
                holder.Thumbnail.setImageResource(R.drawable.sunny);
                break;
            case "Sunny Intervals":
                holder.Thumbnail.setImageResource(R.drawable.sunny_intervals);
                break;
            case "Thick Cloud":
                holder.Thumbnail.setImageResource(R.drawable.thick_cloud);
                break;
            case "Thunderstorm":
                holder.Thumbnail.setImageResource(R.drawable.thunderstorm);
                break;
            case "Thundery Shower":
                holder.Thumbnail.setImageResource(R.drawable.thundery_shower);
                break;
            default:
                holder.Thumbnail.setImageResource(R.drawable.placeholder);
                break;
        }
    }
}
