package com.codepath.apps.tweetaway.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.tweetaway.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.activities.UserProfileActivity;
import com.codepath.apps.tweetaway.adapters.DividerItemDecoration;
import com.codepath.apps.tweetaway.adapters.FollowersAdapter;
import com.codepath.apps.tweetaway.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


/**
 * Created by rpraveen on 11/6/16.
 */

public class FollowersListFragment
  extends FragmentBase
  implements FollowersAdapter.OnFollowItemClickListener {

  static final String SCREEN_NAME_ARGS = "screen_name";

  @BindView(R.id.rvFollowersList) RecyclerView mRvFollowersList;

  private FollowersAdapter mFollowersAdapter;
  private ArrayList<User> mUsers;

  private EndlessRecyclerViewScrollListener mRvScrollListener;

  protected long mCursor = -1;

  public static FollowersListFragment newInstance(String screenName) {
    FollowersListFragment fragment = new FollowersListFragment();
    Bundle args = new Bundle();
    args.putString(SCREEN_NAME_ARGS, screenName);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    mUsers = new ArrayList<>();
    mFollowersAdapter = new FollowersAdapter(getActivity(), this, mUsers);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
    View view = inflater.inflate(R.layout.fragment_follow_list, container, false);
    ButterKnife.bind(this, view);
    RecyclerView.ItemDecoration itemDecoration =
      new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
    mRvFollowersList.addItemDecoration(itemDecoration);
    LinearLayoutManager rvLayoutManager = new LinearLayoutManager(getActivity());
    mRvFollowersList.setLayoutManager(rvLayoutManager);
    mRvScrollListener = new EndlessRecyclerViewScrollListener(rvLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        if (mCursor != 0) {
          populateFollowersList();
        }
      }
    };
    mRvFollowersList.addOnScrollListener(mRvScrollListener);
    mRvFollowersList.setAdapter(mFollowersAdapter);
    populateFollowersList();
    return view;
  }

  protected void populateFollowersList() {
    String screenName = getArguments().getString(SCREEN_NAME_ARGS);
    mClient.getFollowers(screenName, mCursor, new JsonHttpResponseHandler(){

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

  protected void addAll(JSONObject response) {
    try {
      mCursor = response.getLong("next_cursor");
      List<User> users = User.fromJSONArray(response.getJSONArray("users"));
      mUsers.addAll(users);
      mFollowersAdapter.notifyDataSetChanged();
    } catch (Exception e) {
      Toast.makeText(getActivity(), "parsing followers failed", Toast.LENGTH_SHORT);
    }
  }

  @Override
  public void onFollowItemClickListener(String screenName) {
    Intent i = new Intent(getActivity(), UserProfileActivity.class);
    i.putExtra("screen_name", screenName);
    startActivity(i);
  }
}
