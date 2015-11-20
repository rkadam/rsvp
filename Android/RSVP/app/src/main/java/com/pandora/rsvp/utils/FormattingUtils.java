package com.pandora.rsvp.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.Format;
import java.text.SimpleDateFormat;

public class FormattingUtils {
    private static final Format formatterJustDate = new SimpleDateFormat("yyyy/MM/dd");
    private static final Format formatterJustTime24NoSeconds = new SimpleDateFormat("HH:mm");
    
    public static String formatJustDate(long milliseconds) {
        return formatterJustDate.format(milliseconds);
    }

    public static String formatTimeNoSeconds(long milliseconds) {
        return formatterJustTime24NoSeconds.format(milliseconds);
    }
}
