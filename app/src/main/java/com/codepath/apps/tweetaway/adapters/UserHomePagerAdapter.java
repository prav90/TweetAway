package com.codepath.apps.tweetaway.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.tweetaway.fragments.HomeTimelineFragment;
import com.codepath.apps.tweetaway.fragments.MentionsTimelineFragment;

/**
 * Created by rpraveen on 11/5/16.
 */

public class UserHomePagerAdapter extends SmartFragmentStatePagerAdapter {

  private String tabTitles[] = new String[] {"Home", "Mentions"};

  public UserHomePagerAdapter(FragmentManager fm, Context context) {
    super(fm);
  }

  @Override
  public int getCount() {
    return tabTitles.length;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return HomeTimelineFragment.newInstance("home");
      case 1:
        return MentionsTimelineFragment.newInstance("mentions");
      default:
        return null;
    }
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return tabTitles[position];
  }
}
