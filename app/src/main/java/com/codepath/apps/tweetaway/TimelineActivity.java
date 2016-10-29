package com.codepath.apps.tweetaway;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.apps.tweetaway.adapters.TweetsRecyclerAdapter;
import com.codepath.apps.tweetaway.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

  private TwitterClient mClient;
  private TweetsRecyclerAdapter mAdapter;
  private ArrayList<Tweet> mTweets;
  private RecyclerView mRvTweets;

  private EndlessRecyclerViewScrollListener mRvScrollListener;

  private long mTweetMaxID = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);

    mTweets = new ArrayList<>();
    mAdapter = new TweetsRecyclerAdapter(this, mTweets);

    mRvTweets = (RecyclerView) findViewById(R.id.rvTweets);
    mRvTweets.setAdapter(mAdapter);
    LinearLayoutManager rvLayoutManager = new LinearLayoutManager(this);
    mRvTweets.setLayoutManager(rvLayoutManager);

    mRvScrollListener = new EndlessRecyclerViewScrollListener(rvLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        populateTimeline();
      }
    };
    mRvTweets.addOnScrollListener(mRvScrollListener);

    mClient = TwitterApplication.getRestClient();
    populateTimeline();
  }

  private void populateTimeline() {
    mClient.getHomeTimeline(mTweetMaxID, new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        List<Tweet> recentTweets = Tweet.fromJSONArray(response);
        for (Tweet t : recentTweets) {
          if (mTweetMaxID == -1) {
            mTweetMaxID = t.getUid();
          }
          if (mTweetMaxID > t.getUid()) {
            mTweetMaxID = t.getUid();
          }
        }
        mTweets.addAll(recentTweets);
        mAdapter.notifyDataSetChanged();
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
      }
    });
  }
}
