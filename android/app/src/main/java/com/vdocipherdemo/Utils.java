package com.vdocipherdemo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;

public class Utils {

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("codingninjas.in");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

}
