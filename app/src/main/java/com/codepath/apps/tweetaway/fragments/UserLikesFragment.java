package com.codepath.apps.tweetaway.fragments;

import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rpraveen on 11/5/16.
 */

public class UserLikesFragment extends UserTimelineFragment {
  static final String SCREEN_NAME_ARGS = "screen_name";

  public static UserLikesFragment newInstance(String userScreenName) {
    UserLikesFragment fragment = new UserLikesFragment();
    Bundle args = new Bundle();
    args.putString(SCREEN_NAME_ARGS, userScreenName);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected void populateTimeline() {
    String userScreenName = getArguments().getString(SCREEN_NAME_ARGS);
    mClient.getUserLikesTimeline(userScreenName, mTweetMaxID, new JsonHttpResponseHandler(){
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
