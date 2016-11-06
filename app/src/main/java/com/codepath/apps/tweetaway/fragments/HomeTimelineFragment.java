package com.codepath.apps.tweetaway.fragments;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rpraveen on 11/4/16.
 */

public class HomeTimelineFragment extends BaseTimeLineFragment {

  public static HomeTimelineFragment newInstance(String fragmentType) {
    return new HomeTimelineFragment();
  }

  @Override
  protected void populateTimeline() {
    mClient.getHomeTimeline(mTweetMaxID, new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        addAll(response);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
      }
    });
  }
}
