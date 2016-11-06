package com.codepath.apps.tweetaway.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "36Un0tsKlH7u0UGtGnlcdvkDv";       // Change this
	public static final String REST_CONSUMER_SECRET = "eh0U8qNAyJFcxMbAq34HcQLKv7AsjEgF7QM1ZlKqfCQax0UKfx"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cptweetaway"; // Change this (here and in manifest)

  final int TWEET_COUNT = 10;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(long maxID, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/home_timeline.json");
    // Can specify query string params directly or through RequestParams.
    RequestParams params = new RequestParams();
    params.put("count", TWEET_COUNT);
    params.put("since_id", 1);
		if (maxID != -1) {
			params.put("max_id", maxID);
		}
    client.get(apiUrl, params, handler);
  }

  public void getMentionsTimeline(long maxID, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    RequestParams params = new RequestParams();
    params.put("count", TWEET_COUNT);
    params.put("since_id", 1);
    if (maxID != -1) {
      params.put("max_id", maxID);
    }
    client.get(apiUrl, params, handler);
  }

  public void getUserTimeline(String screenName, long maxID, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/user_timeline.json");
    RequestParams params = new RequestParams();
    params.put("screen_name", screenName);
    params.put("count", TWEET_COUNT);
    params.put("since_id", 1);
    if (maxID != -1) {
      params.put("max_id", maxID);
    }
    client.get(apiUrl, params, handler);
  }

  public void getUserLikesTimeline(String screenName, long maxID, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("favorites/list.json");
    RequestParams params = new RequestParams();
    params.put("screen_name", screenName);
    params.put("count", TWEET_COUNT);
    params.put("since_id", 1);
    if (maxID != -1) {
      params.put("max_id", maxID);
    }
    client.get(apiUrl, params, handler);
  }

  public void getFollowers(String screenName, long cursor, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("followers/list.json");
    RequestParams params = new RequestParams();
    params.put("screen_name", screenName);
    params.put("cursor", cursor);
    client.get(apiUrl, params, handler);
  }

  public void getFollowing(String screenName, long cursor, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("friends/list.json");
    RequestParams params = new RequestParams();
    params.put("screen_name", screenName);
    params.put("cursor", cursor);
    client.get(apiUrl, params, handler);
  }

  public void getDirectMessages(AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("direct_messages.json");
    RequestParams params = new RequestParams();
    client.get(apiUrl, params, handler);
  }

	// Invalid tweets are not sent to the api
	public boolean postStatusUpdate(String status, AsyncHttpResponseHandler handler) {
		if (status == null || status.length() == 0) {
			return false;
		}
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", status);
		client.post(apiUrl, params, handler);
		return true;
	}

	public void getCurrentUserDetails(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		RequestParams params = new RequestParams();
		client.get(apiUrl, params, handler);
	}

  public void getUserProfile(String screenName, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("users/show.json");
    RequestParams params = new RequestParams();
    params.put("screen_name", screenName);
    client.get(apiUrl, params, handler);
  }
}