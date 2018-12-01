package com.vdocipherdemo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;

public class Utils {

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("codingninjas.in");
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
