package com.codepath.apps.tweetaway.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.tweetaway.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.TwitterApplication;
import com.codepath.apps.tweetaway.TwitterClient;
import com.codepath.apps.tweetaway.adapters.DividerItemDecoration;
import com.codepath.apps.tweetaway.adapters.TweetsRecyclerAdapter;
import com.codepath.apps.tweetaway.decorators.ItemClickSupport;
import com.codepath.apps.tweetaway.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by rpraveen on 11/4/16.
 */

public class TimelineFragment extends Fragment {

  private String mFragmentType;

  @BindView(R.id.rvTweets) RecyclerView mRvTweets;
  @BindView(R.id.swipeContainer) SwipeRefreshLayout mSwipeRefreshLayout;

  private TweetsRecyclerAdapter mAdapter;
  private ArrayList<Tweet> mTweets;
  private TwitterClient mClient;

  private EndlessRecyclerViewScrollListener mRvScrollListener;

  private long mTweetMaxID = -1;

  public static TimelineFragment newInstance(String fragmentType) {
    TimelineFragment fragment = new TimelineFragment();
    Bundle args = new Bundle();
    args.putString("fragment_type", fragmentType);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
    View view = inflater.inflate(R.layout.fragment_timeline, container, false);
    ButterKnife.bind(this, view);
    mFragmentType = getArguments().getString("fragment_type");
    // initialize list and adapters
    mClient = TwitterApplication.getRestClient();
    mTweets = new ArrayList<>();
    mAdapter = new TweetsRecyclerAdapter(getActivity(), mTweets);
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

    ItemClickSupport.addTo(mRvTweets).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
      @Override
      public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Toast.makeText(getActivity(), "Show Tweet detail view", Toast.LENGTH_SHORT).show();
      }
    });
    populateTimeline();
    return view;
  }

  private void resetTimeLine() {
    mTweetMaxID = -1;
    mTweets.clear();
    mAdapter.notifyDataSetChanged();
    populateTimeline();
  }

  private void populateTimeline() {
    mClient.getHomeTimeline(mTweetMaxID, mFragmentType, new JsonHttpResponseHandler(){
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
