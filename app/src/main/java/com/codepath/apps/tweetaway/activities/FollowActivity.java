package com.codepath.apps.tweetaway.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.fragments.FollowersListFragment;
import com.codepath.apps.tweetaway.fragments.FollowingListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rpraveen on 11/6/16.
 */

public class FollowActivity extends AppCompatActivity {

  public static final String IS_FOLLOWING_ARG = "is_following";
  public static final String SCREEN_NAME_ARG = "screen_name";

  @BindView(R.id.flFollowContainer) FrameLayout followContainer;
  @BindView(R.id.toolBar) Toolbar mToolBar;

  @Override
  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    setContentView(R.layout.activity_followers);
    boolean isFollowingIntent = getIntent().getBooleanExtra(IS_FOLLOWING_ARG, true);
    String userScreenName = getIntent().getStringExtra(SCREEN_NAME_ARG);
    ButterKnife.bind(this);
    FollowersListFragment followFrag = null;
    int toolBarTitle = R.string.toolbar_followers;
    if (isFollowingIntent) {
      followFrag = FollowingListFragment.newInstance(userScreenName);
      toolBarTitle = R.string.toolbar_friends;
    } else {
      followFrag = FollowersListFragment.newInstance(userScreenName);
    }
    setSupportActionBar(mToolBar);
    getSupportActionBar().setTitle(toolBarTitle);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.flFollowContainer,followFrag).commit();
  }

  /**
   * react to the user tapping the back/up icon in the action bar
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
        // if this doesn't work as desired, another possibility is to call `finish()` here.
        this.onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
