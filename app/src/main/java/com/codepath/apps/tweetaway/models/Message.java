package com.codepath.apps.tweetaway.models;

import com.codepath.apps.tweetaway.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpraveen on 11/6/16.
 */

public class Message {
  private String senderScreenName;
  private String text;
  private String senderProfileImageUrl;
  private String createdAt;

  public String getSenderScreenName() {
    return senderScreenName;
  }

  public String getText() {
    return text;
  }

  public String getSenderProfileImageUrl() {
    return senderProfileImageUrl;
  }

  public String getCreatedAt() {
    return DateUtil.getRelativeTimeForTweet(createdAt);
  }

  public static Message fromJSONObject(JSONObject jsonObject) throws JSONException {
    Message message = new Message();
    message.createdAt = jsonObject.getString("created_at");
    message.senderScreenName = jsonObject.getString("sender_screen_name");
    message.text = jsonObject.getString("text");
    JSONObject sender = jsonObject.getJSONObject("sender");
    message.senderProfileImageUrl = sender.getString("profile_image_url_https");
    return message;
  }

  public static List<Message> fromJSONArray(JSONArray jsonArray) throws JSONException {
    ArrayList<Message> messages = new ArrayList<>();
    for (int i = 0; i < jsonArray.length(); i++) {
      messages.add(fromJSONObject(jsonArray.getJSONObject(i)));
    }
    return  messages;
  }
}
