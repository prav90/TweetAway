package com.codepath.apps.tweetaway.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by rpraveen on 10/29/16.
 */

public class DateUtil {
  public static String getRelativeTimeForTweet(String tweetCreatedDate) {
    String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
    sf.setLenient(true);
    String relativeDate = "";
    try {
      long dateMillis = sf.parse(tweetCreatedDate).getTime();
      relativeDate = DateUtils.getRelativeTimeSpanString(
        dateMillis,
        System.currentTimeMillis(),
        DateUtils.SECOND_IN_MILLIS
      ).toString();
      String [] relativeSplit = relativeDate.split(" ");
      // splits into number, secs/mins/days and ago
      if (relativeSplit.length == 3) {
        if (relativeSplit[0].equalsIgnoreCase("in")) {
          relativeDate = relativeSplit[0] + " " + relativeSplit[1] + relativeSplit[2].charAt(0);
        } else {
          relativeDate = relativeSplit[0] + relativeSplit[1].charAt(0);
        }
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return relativeDate;
  }
}
