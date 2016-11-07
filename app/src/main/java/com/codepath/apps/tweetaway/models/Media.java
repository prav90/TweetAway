package com.codepath.apps.tweetaway.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by rpraveen on 10/30/16.
 */
@Parcel
public class Media {

  // by default everything is text only
  boolean isTextOnly = true;
  boolean isImage = false;
  boolean isVideo = false;

  String imageURL = null;
  String videoURL = null;

  public static Media fromJSONArray(JSONArray mediaArray) {
    // there might be more than one of photos, multi_photos, video, animated_gif
    // loop through and get the ones
    Media media = new Media();
    try {
      for (int i = 0; i < mediaArray.length(); i++) {
        JSONObject mediaObject = mediaArray.getJSONObject(i);
        String mediaType = mediaObject.getString("type");
        switch (mediaType) {
          case "photo":
            // there can be multiple images, we make it one for simplicity
            media.isImage = true;
            media.imageURL = mediaObject.getString("media_url_https");
            break;
          case "video":
            JSONObject videoInfo = mediaObject.getJSONObject("video_info");
            JSONArray videoVariants = videoInfo.getJSONArray("variants");
            for (int j = 0; j < videoVariants.length(); j++) {
              JSONObject videoDetails = videoVariants.getJSONObject(j);
              if (videoDetails.getString("content_type").equalsIgnoreCase("video/mp4")) {
                media.videoURL = videoDetails.getString("url");
                media.isVideo = true;
              }
            }
            break;
          default:
            // default to text only for other types
            // logic will decide how to display the tweet
            media.isTextOnly = true;
        }
      }
    } catch (JSONException e) {
      media.isTextOnly = true;
      Log.d("Media Parse Error", e.toString());
    }
    return media;
  }

  public String getImage(String size) {
    switch (size) {
      case "small":
      case "thumb":
      case "large":
        return imageURL + ":" + size;
    }
    return imageURL;
  }

  public String getVideoURL() {
    return videoURL;
  }

  public boolean isTextOnly() {
    return isTextOnly;
  }

  public boolean isImage() {
    return isImage;
  }

  public boolean isVideo() {
    return isVideo;
  }
}
