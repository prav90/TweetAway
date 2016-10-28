package com.codepath.apps.tweetaway;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.codepath.apps.tweetaway.adapters.TweetsArrayAdapter;
import com.codepath.apps.tweetaway.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

  private TwitterClient mClient;
  private TweetsArrayAdapter mAdapter;
  private ArrayList<Tweet> mTweets;
  private ListView mLvTweets;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    mLvTweets = (ListView) findViewById(R.id.lvTweets);
    mTweets = new ArrayList<>();
    mAdapter = new TweetsArrayAdapter(this, mTweets);
    mLvTweets.setAdapter(mAdapter);
    mClient = TwitterApplication.getRestClient();
    populateTimeline();
  }

  private void populateTimeline() {
    mClient.getHomeTimeline(new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        mAdapter.addAll(Tweet.fromJSONArray(response));
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
      }
    });
  }
}
