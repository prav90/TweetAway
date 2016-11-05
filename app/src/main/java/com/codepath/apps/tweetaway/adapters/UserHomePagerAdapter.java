package com.codepath.apps.tweetaway.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.tweetaway.fragments.TimelineFragment;

/**
 * Created by rpraveen on 11/5/16.
 */

public class UserHomePagerAdapter extends SmartFragmentStatePagerAdapter {

  final int PAGE_COUNT = 3;
  private String tabTitles[] = new String[] {"Home", "Mentions", "Message"};
  private Context mContext;

  public UserHomePagerAdapter(FragmentManager fm, Context context) {
    super(fm);
    mContext = context;
  }

  @Override
  public int getCount() {
    return PAGE_COUNT;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return TimelineFragment.newInstance("home");
      case 1:
        return TimelineFragment.newInstance("mentions");
      default:
        return TimelineFragment.newInstance("home");
    }
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return tabTitles[position];
  }
}
