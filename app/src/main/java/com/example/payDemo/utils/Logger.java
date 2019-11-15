package com.example.payDemo.utils;

import android.util.Log;

public class Logger {
    private static String TAG = "lhTest";

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }
}
