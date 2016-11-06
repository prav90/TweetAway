package com.codepath.apps.tweetaway.fragments;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.tweetaway.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rpraveen on 11/4/16.
 */

public class HomeTimelineFragment extends BaseTimeLineFragment {

  public static HomeTimelineFragment newInstance() {
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    String statusUpdate = data.getExtras().getString("statusUpdate");
    mClient.postStatusUpdate(statusUpdate, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          Tweet recentComposeTweet = Tweet.fromJSON(response);
          addToTop(recentComposeTweet);
          Toast.makeText(getActivity(), "Status update success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
          Log.d("compose failure", e.toString());
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(getActivity(), "Status update failed", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
