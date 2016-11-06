package com.codepath.apps.tweetaway.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.tweetaway.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.activities.UserProfileActivity;
import com.codepath.apps.tweetaway.adapters.DividerItemDecoration;
import com.codepath.apps.tweetaway.adapters.TweetsRecyclerAdapter;
import com.codepath.apps.tweetaway.models.Tweet;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rpraveen on 11/5/16.
 */

public abstract class BaseTimeLineFragment
  extends FragmentBase
  implements TweetsRecyclerAdapter.OnTweetClickListener {

  @BindView(R.id.rvTweets) RecyclerView mRvTweets;
  @BindView(R.id.swipeContainer) SwipeRefreshLayout mSwipeRefreshLayout;

  private TweetsRecyclerAdapter mAdapter;
  private ArrayList<Tweet> mTweets;

  private EndlessRecyclerViewScrollListener mRvScrollListener;

  protected long mTweetMaxID = -1;

  @Override
  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    // initialize list and adapters
    mTweets = new ArrayList<>();
    mAdapter = new TweetsRecyclerAdapter(getActivity(), this, mTweets);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
    View view = inflater.inflate(R.layout.fragment_timeline, container, false);
    ButterKnife.bind(this, view);

    RecyclerView.ItemDecoration itemDecoration =
      new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
    mRvTweets.addItemDecoration(itemDecoration);

    // set recycler view adapter and layout
    mRvTweets.setAdapter(mAdapter);
    LinearLayoutManager rvLayoutManager = new LinearLayoutManager(getActivity());
    mRvTweets.setLayoutManager(rvLayoutManager);

    // set up listeners
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        resetTimeLine();
      }
    });

    mRvScrollListener = new EndlessRecyclerViewScrollListener(rvLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        //populateTimeline();
      }
    };
    mRvTweets.addOnScrollListener(mRvScrollListener);

    populateTimeline();
    return view;
  }

  protected void addAll(JSONArray response) {
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

  public void onProfileImageClicked(String screen_name) {
    Intent i = new Intent(getActivity(), UserProfileActivity.class);
    i.putExtra("screen_name", screen_name);
    startActivity(i);
  }

  private void resetTimeLine() {
    mTweetMaxID = -1;
    mTweets.clear();
    mAdapter.notifyDataSetChanged();
    populateTimeline();
  }

  protected abstract void populateTimeline();

}
