package com.codepath.apps.tweetaway.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.fragments.DirectMessagesFragment;
import com.codepath.apps.tweetaway.fragments.HomeTimelineFragment;
import com.codepath.apps.tweetaway.fragments.MentionsTimelineFragment;

/**
 * Created by rpraveen on 11/5/16.
 */

public class UserHomePagerAdapter extends SmartFragmentStatePagerAdapter {

  private String tabTitles[] = new String[] {"Home", "Mentions", "Messages"};
  private int imageResID[] =
    new int[] {R.drawable.ic_home_tab, R.drawable.ic_mentions_tab, R.drawable.ic_message_tab};
  private Context mContext;

  public UserHomePagerAdapter(FragmentManager fm, Context context) {
    super(fm);
    mContext = context;
  }

  @Override
  public int getCount() {
    return tabTitles.length;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return HomeTimelineFragment.newInstance();
      case 1:
        return MentionsTimelineFragment.newInstance();
      case 2:
        return DirectMessagesFragment.newInstance();
      default:
        return null;
    }
  }

  @Override
  public CharSequence getPageTitle(int position) {
    Drawable image = ContextCompat.getDrawable(mContext, imageResID[position]);
    image.setBounds(0, 0, 70, 70);
    SpannableString sb = new SpannableString(" ");
    ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
    sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return sb;
  }
}
