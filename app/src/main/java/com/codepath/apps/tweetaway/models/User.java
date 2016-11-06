package com.codepath.apps.tweetaway.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpraveen on 10/27/16.
 */
/*
 "user": {
 "name": "OAuth Dancer",
 "profile_sidebar_fill_color": "DDEEF6",
 "profile_background_tile": true,
 "profile_sidebar_border_color": "C0DEED",
 "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
 "created_at": "Wed Mar 03 19:37:35 +0000 2010",
 "location": "San Francisco, CA",
 "follow_request_sent": false,
 "id_str": "119476949",
 "is_translator": false,
 "profile_link_color": "0084B4",
 "entities": {
 "url": {
 "urls": [
 {
 "expanded_url": null,
 "url": "http://bit.ly/oauth-dancer",
 "indices": [
 0,
 26
 ],
 "display_url": null
 }
 ]
 },
 "description": null
 },
 "default_profile": false,
 "url": "http://bit.ly/oauth-dancer",
 "contributors_enabled": false,
 "favourites_count": 7,
 "utc_offset": null,
 "profile_image_url_https": "https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
 "id": 119476949,
 "listed_count": 1,
 "profile_use_background_image": true,
 "profile_text_color": "333333",
 "followers_count": 28,
 "lang": "en",
 "protected": false,
 "geo_enabled": true,
 "notifications": false,
 "description": "",
 "profile_background_color": "C0DEED",
 "verified": false,
 "time_zone": null,
 "profile_background_image_url_https": "https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
 "statuses_count": 166,
 "profile_background_image_url": "http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",
 "default_profile_image": false,
 "friends_count": 14,
 "following": false,
 "show_all_inline_media": false,
 "screen_name": "oauth_dancer"
 },
 */
@Parcel
public class User {
  private String name;
  long uid;
  String screenName;
  String profileImageURL;
  String profileBanner;
  String profileDescription;
  long followersCount;
  long followingCount;
  boolean isVerifiedProfile;
  String location;
  String url;

  public String getName() {
    return name;
  }

  public long getUid() {
    return uid;
  }

  public String getScreenName() {
    return screenName;
  }

  public String getProfileImageURL() {
    return profileImageURL.replace("_normal", "");
  }

  public String getProfileBanner() {
    return profileBanner;
  }

  public String getProfileDescription() {
    return profileDescription;
  }

  public long getFollowersCount() {
    return followersCount;
  }

  public long getFollowingCount() {
    return followingCount;
  }

  public boolean isVerifiedProfile() {
    return isVerifiedProfile;
  }

  public String getLocation() {
    return location;
  }

  public String getUrl() {
    return url;
  }


  public static User fromJSON(JSONObject jsonObject) throws JSONException {
    User user = new User();
    user.name = jsonObject.getString("name");
    user.uid = jsonObject.getLong("id");
    user.screenName = jsonObject.getString("screen_name");
    if (user.screenName != null) {
      user.screenName = "@" + user.getScreenName().trim();
    }
    user.profileImageURL = jsonObject.getString("profile_image_url");
    if (jsonObject.has("profile_banner_url")) {
      user.profileBanner = jsonObject.getString("profile_banner_url");
    }
    user.profileDescription = jsonObject.getString("description");
    user.followingCount = jsonObject.getLong("friends_count");
    user.followersCount = jsonObject.getLong("followers_count");
    user.isVerifiedProfile = jsonObject.getBoolean("verified");
    user.location = jsonObject.getString("location");
    user.url = jsonObject.getString("url");
    return user;
  }

  public static List<User> fromJSONArray(JSONArray jsonArray) throws JSONException {
    ArrayList<User> users = new ArrayList<>();
    for(int i = 0; i < jsonArray.length(); i++) {
      users.add(fromJSON(jsonArray.getJSONObject(i)));
    }
    return users;
  }
}
