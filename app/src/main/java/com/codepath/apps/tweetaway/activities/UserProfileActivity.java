package com.codepath.apps.tweetaway.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.adapters.UserProfilePagerAdapter;
import com.codepath.apps.tweetaway.fragments.ProfileHeaderFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rpraveen on 11/5/16.
 */

public class UserProfileActivity extends AppCompatActivity {

  @BindView(R.id.flUserProfileContainer) FrameLayout mFlContainer;
  @BindView(R.id.tabsUserProfile) TabLayout mTabsUserProfile;
  @BindView(R.id.pagerUserProfile) ViewPager mUserProfileViewPager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_profile);
    ButterKnife.bind(this);
    String user_screen_name = getIntent().getStringExtra("screen_name");
    ProfileHeaderFragment profileHeader = ProfileHeaderFragment.newInstance(user_screen_name);
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.flUserProfileContainer, profileHeader).commit();
    mUserProfileViewPager.setAdapter(new UserProfilePagerAdapter(fragmentManager, this, user_screen_name));
    mTabsUserProfile.setupWithViewPager(mUserProfileViewPager);
  }
}
