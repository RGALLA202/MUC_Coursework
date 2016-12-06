package com.rgalla202.weatherdb;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rgall on 30/11/2016.
 * this class is to modify output
 * methods are accessable throughout the project
 */

public class ModifyOutput {

    /**
     * Round number to 2 dp
     * @param value
     * @param places
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
