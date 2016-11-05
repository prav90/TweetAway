package com.codepath.apps.tweetaway.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.TwitterApplication;
import com.codepath.apps.tweetaway.adapters.UserHomePagerAdapter;
import com.codepath.apps.tweetaway.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

  public static final int COMPOSE_REQUEST_CODE = 100;

  private User mCurrentUser;

  FloatingActionButton mFabNewTweet;
  Toolbar mToolBar;
  ViewPager mPager;
  TabLayout mTabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);

    // Bind Views
    //ButterKnife.bind(this);
    mToolBar = (Toolbar) findViewById(R.id.toolBar);
    mFabNewTweet = (FloatingActionButton) findViewById(R.id.fabNewTweet);
    mTabLayout = (TabLayout) findViewById(R.id.tabsUserHome);
    mPager = (ViewPager) findViewById(R.id.pagerUserHome);

    setSupportActionBar(mToolBar);

    mPager.setAdapter(new UserHomePagerAdapter(getSupportFragmentManager(), this));
    mTabLayout.setupWithViewPager(mPager);

    mFabNewTweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //Toast.makeText(TimelineActivity.this, "New tweet", Toast.LENGTH_SHORT).show();
        Intent showComposer = new Intent(TimelineActivity.this, ComposeTweet.class);
        showComposer.putExtra("current_user", Parcels.wrap(mCurrentUser));
        startActivityForResult(showComposer, COMPOSE_REQUEST_CODE);
      }
    });

    // populate the timeline
    getCurrentUser();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
      String statusUpdate = data.getExtras().getString("statusUpdate");
      if (statusUpdate == null || statusUpdate.length() == 0) {
        return;
      }
      //Toast.makeText(TimelineActivity.this, statusUpdate, Toast.LENGTH_LONG).show();
      // call the api and refresh the timeline
//      mClient.postStatusUpdate(statusUpdate, new JsonHttpResponseHandler() {
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//          try {
//            Tweet recentComposeTweet = Tweet.fromJSON(response);
//            mTweets.add(0, recentComposeTweet);
//            mAdapter.notifyDataSetChanged();
//            mRvTweets.getLayoutManager().scrollToPosition(0);
//            Toast.makeText(TimelineActivity.this, "Status update success", Toast.LENGTH_SHORT).show();
//          } catch (Exception e) {
//            Log.d("compose failure", e.toString());
//          }
//        }
//
//        @Override
//        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//          Toast.makeText(TimelineActivity.this, "Status update failed", Toast.LENGTH_SHORT).show();
//        }
//      });
    }
  }

  private void getCurrentUser() {
    TwitterApplication.getRestClient().getCurrentUserDetails(new JsonHttpResponseHandler(){
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
}
