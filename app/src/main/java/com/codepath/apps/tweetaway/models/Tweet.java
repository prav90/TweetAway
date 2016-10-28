package com.codepath.apps.tweetaway.models;

/**
 * Created by rpraveen on 10/27/16.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * {
 "coordinates": null,
 "truncated": false,
 "created_at": "Tue Aug 28 21:16:23 +0000 2012",
 "favorited": false,
 "id_str": "240558470661799936",
 "in_reply_to_user_id_str": null,
 "entities": {
 "urls": [

 ],
 "hashtags": [

 ],
 "user_mentions": [

 ]
 },
 "text": "just another test",
 "contributors": null,
 "id": 240558470661799936,
 "retweet_count": 0,
 "in_reply_to_status_id_str": null,
 "geo": null,
 "retweeted": false,
 "in_reply_to_user_id": null,
 "place": null,
 "source": "OAuth Dancer Reborn",
 "in_reply_to_screen_name": null,
 "in_reply_to_status_id": null
 },
 */
public class Tweet {
  private String body;
  private long uid;
  private User user;
  private String createdAt;

  public String getBody() {
    return body;
  }

  public long getUid() {
    return uid;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public User getUser() {
    return user;
  }

  public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
    Tweet tweet = new Tweet();
    tweet.body = jsonObject.getString("text");
    tweet.uid = jsonObject.getLong("id");
    tweet.createdAt = jsonObject.getString("created_at");
    tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
    return tweet;
  }

  public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
    ArrayList<Tweet> tweets = new ArrayList<>();
    for (int i = 0; i < jsonArray.length(); i++) {
      try {
          tweets.add(Tweet.fromJSON(jsonArray.getJSONObject(i)));
      } catch (JSONException e){
        Log.d("Tweet parse exception", e.toString());
        continue;
      }
    }
    return tweets;
  }
}
