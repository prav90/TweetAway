package com.codepath.apps.tweetaway.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rpraveen on 11/6/16.
 */

public class SearchResultsFragment extends BaseTimeLineFragment {

  public static SearchResultsFragment newInstance(String searchTerm) {
    SearchResultsFragment fragment = new SearchResultsFragment();
    Bundle args = new Bundle();
    args.putString("search_query", searchTerm);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected void populateTimeline() {
    String searchTerm = getArguments().getString("search_query");
    mClient.getSearchResults(searchTerm, mTweetMaxID, new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          JSONObject results = response.getJSONObject("search_metadata");
          if (results.getInt("count") > 0) {
            addAll(response.getJSONArray("statuses"));
          }
        } catch (Exception e) {
          Toast.makeText(getActivity(), "No Results Found", Toast.LENGTH_LONG);
        }

      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(getActivity(), "Failed to fetch results", Toast.LENGTH_LONG);
      }
    });
  }
}
