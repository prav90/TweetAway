package com.codepath.apps.tweetaway.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.tweetaway.fragments.UserLikesFragment;
import com.codepath.apps.tweetaway.fragments.UserTimelineFragment;

/**
 * Created by rpraveen on 11/5/16.
 */

public class UserProfilePagerAdapter extends SmartFragmentStatePagerAdapter {

  private String tabTitles[] = new String[] {"Tweets", "Likes"};
  private String mUserScreenName;

  public UserProfilePagerAdapter(FragmentManager fm, Context context, String userScreenName) {
    super(fm);
    mUserScreenName = userScreenName;
  }

  @Override
  public int getCount() {
    return tabTitles.length;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return UserTimelineFragment.newInstance(mUserScreenName);
      case 1:
        return UserLikesFragment.newInstance(mUserScreenName);
      default:
        return null;
    }
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return tabTitles[position];
  }{
  }
}