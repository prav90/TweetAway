package com.codepath.apps.tweetaway.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

  @Override
  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    setContentView(R.layout.activity_followers);
    boolean isFollowingIntent = getIntent().getBooleanExtra(IS_FOLLOWING_ARG, true);
    String userScreenName = getIntent().getStringExtra(SCREEN_NAME_ARG);
    ButterKnife.bind(this);
    FollowersListFragment followFrag = null;
    if (isFollowingIntent) {
      followFrag = FollowingListFragment.newInstance(userScreenName);
    } else {
      followFrag = FollowersListFragment.newInstance(userScreenName);
    }
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.flFollowContainer,followFrag).commit();
  }
}
