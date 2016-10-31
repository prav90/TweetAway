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
  private String createdAt;
  private long retweetCount;
  private long favoriteCount;

  private User user;
  private Media media;

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

  public long getRetweetCount() {
    return retweetCount;
  }

  public long getFavoriteCount() {
    return favoriteCount;
  }

  public Media getMedia() {
    return media;
  }

  public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
    Tweet tweet = new Tweet();
    tweet.body = jsonObject.getString("text");
    tweet.uid = jsonObject.getLong("id");
    tweet.createdAt = jsonObject.getString("created_at");
    tweet.retweetCount = jsonObject.getLong("retweet_count");
    tweet.favoriteCount = jsonObject.getLong("favorite_count");

    tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

    JSONObject entities = jsonObject.getJSONObject("entities");
    if (!entities.has("media")) {
      tweet.media = new Media();
    } else { // has photo, gif or video
      // load the media array from tweet object
      JSONArray media = entities.getJSONArray("media");
      // if extended entities is present override the media
      if (jsonObject.has("extended_entities")) {
        JSONObject extendedEntities = jsonObject.getJSONObject("extended_entities");
        if (extendedEntities.has("media")) {
          media = extendedEntities.getJSONArray("media");
        }
      }
      tweet.media = Media.fromJSONArray(media);
    }
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
