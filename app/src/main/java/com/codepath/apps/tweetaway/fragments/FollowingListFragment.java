package com.codepath.apps.tweetaway.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rpraveen on 11/6/16.
 */

public class FollowingListFragment extends FollowersListFragment {
  public static FollowingListFragment newInstance(String screenName) {
    FollowingListFragment fragment = new FollowingListFragment();
    Bundle args = new Bundle();
    args.putString(SCREEN_NAME_ARGS, screenName);
    fragment.setArguments(args);
    return fragment;
  }

  protected void populateFollowersList() {
    String screenName = getArguments().getString(SCREEN_NAME_ARGS);
    mClient.getFollowing(screenName, mCursor, new JsonHttpResponseHandler(){

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        addAll(response);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(getActivity(), "follower fetch failed", Toast.LENGTH_SHORT);
      }
    });
  }

}
