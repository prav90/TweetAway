package com.codepath.apps.tweetaway.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.tweetaway.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.TwitterApplication;
import com.codepath.apps.tweetaway.TwitterClient;
import com.codepath.apps.tweetaway.adapters.TweetsRecyclerAdapter;
import com.codepath.apps.tweetaway.models.Tweet;
import com.codepath.apps.tweetaway.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

  public static final int COMPOSE_REQUEST_CODE = 100;

  private TwitterClient mClient;
  private TweetsRecyclerAdapter mAdapter;
  private ArrayList<Tweet> mTweets;
  private User mCurrentUser;

  @BindView(R.id.rvTweets) RecyclerView mRvTweets;
  @BindView(R.id.fabNewTweet) FloatingActionButton mFabNewTweet;
  @BindView(R.id.swipeContainer) SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.toolBar) Toolbar mToolBar;

  private EndlessRecyclerViewScrollListener mRvScrollListener;

  private long mTweetMaxID = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);

    // Bind Views
    ButterKnife.bind(this);

    setSupportActionBar(mToolBar);

    // initialize list and adapters
    mTweets = new ArrayList<>();
    mAdapter = new TweetsRecyclerAdapter(this, mTweets);

    // set recycler view adapter and layout
    mRvTweets.setAdapter(mAdapter);
    LinearLayoutManager rvLayoutManager = new LinearLayoutManager(this);
    mRvTweets.setLayoutManager(rvLayoutManager);

    // set up listeners
    mRvScrollListener = new EndlessRecyclerViewScrollListener(rvLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        //populateTimeline();
      }
    };
    mRvTweets.addOnScrollListener(mRvScrollListener);

    mFabNewTweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(TimelineActivity.this, "New tweet", Toast.LENGTH_SHORT).show();
        Intent showComposer = new Intent(TimelineActivity.this, ComposeTweet.class);
        showComposer.putExtra("current_user", Parcels.wrap(mCurrentUser));
        startActivityForResult(showComposer, COMPOSE_REQUEST_CODE);
      }
    });

    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        resetTimeLine();
      }
    });

    // populate the timeline
    mClient = TwitterApplication.getRestClient();
    getCurrentUser();
    populateTimeline();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
      String statusUpdate = data.getExtras().getString("statusUpdate");
      if (statusUpdate == null || statusUpdate.length() == 0) {
        return;
      }
      Toast.makeText(TimelineActivity.this, statusUpdate, Toast.LENGTH_LONG).show();
      // call the api and refresh the timeline
      mClient.postStatusUpdate(statusUpdate, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          resetTimeLine();
          Toast.makeText(TimelineActivity.this, "Status update suscess", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          Toast.makeText(TimelineActivity.this, "Status update failed", Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  private void resetTimeLine() {
    mTweetMaxID = -1;
    mTweets.clear();
    mAdapter.notifyDataSetChanged();
    populateTimeline();
  }

  private void getCurrentUser() {
    mClient.getCurrentUserDetails(new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          mCurrentUser = User.fromJSON(response);
        } catch (Exception e) {
          Log.d("User fetch failed", e.toString());
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
      }
    });
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
        // max ID is inclusive - decrement to make sure the last story isn't fetched again
        // stories less than or equal to this ID will be fetched in the next request
        mTweetMaxID--;
        mTweets.addAll(recentTweets);
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
      }
    });
  }
}
