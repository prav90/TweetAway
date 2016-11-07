package com.codepath.apps.tweetaway.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.adapters.UserHomePagerAdapter;
import com.codepath.apps.tweetaway.application.TwitterApplication;
import com.codepath.apps.tweetaway.fragments.HomeTimelineFragment;
import com.codepath.apps.tweetaway.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

  public static final int COMPOSE_REQUEST_CODE = 100;

  private User mCurrentUser;
  private ActionBarDrawerToggle mDrawerToggle;
  private UserHomePagerAdapter mPagerAdapter;

  @BindView(R.id.fabNewTweet) FloatingActionButton mFabNewTweet;
  @BindView(R.id.toolBar) Toolbar mToolBar;
  @BindView(R.id.pagerUserHome) ViewPager mPager;
  @BindView(R.id.tabsUserHome) TabLayout mTabLayout;
  @BindView(R.id.drawerLayout) DrawerLayout mDrawerLayout;
  @BindView(R.id.nvView) NavigationView mNvNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);

    // Bind Views
    ButterKnife.bind(this);
    setSupportActionBar(mToolBar);
    getSupportActionBar().setTitle(R.string.tab_home);

    mPagerAdapter = new UserHomePagerAdapter(getSupportFragmentManager(), this);
    mPager.setAdapter(mPagerAdapter);
    mTabLayout.setupWithViewPager(mPager);

    mFabNewTweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //Toast.makeText(TimelineActivity.this, "New tweet", Toast.LENGTH_SHORT).show();
        Intent showComposer = new Intent(TimelineActivity.this, ComposeTweet.class);
        showComposer.putExtra("current_user", Parcels.wrap(mCurrentUser));
        startActivityForResult(showComposer, COMPOSE_REQUEST_CODE);
      }
    });

    mDrawerToggle =
      new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.drawer_open, R.string.drawer_close);

    mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        int toolBarTitle = R.string.tab_home;
        switch (position) {
          case 1:
            toolBarTitle = R.string.tab_mentions;
            break;
          case 2:
            toolBarTitle = R.string.tab_message;
            break;
        }
        getSupportActionBar().setTitle(toolBarTitle);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    // populate the timeline
    getCurrentUser();
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_home_activity, menu);
    MenuItem searchItem = menu.findItem(R.id.action_search);
    final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        if (query.trim().length() == 0) {
          return true;
        }
        Intent i = new Intent(TimelineActivity.this, SearchActivity.class);
        i.putExtra("search_query", query);
        startActivity(i);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    return true;
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
      String statusUpdate = data.getExtras().getString("statusUpdate");
      if (statusUpdate == null || statusUpdate.length() == 0) {
        return;
      }
      //Toast.makeText(TimelineActivity.this, statusUpdate, Toast.LENGTH_LONG).show();
      // call the api and refresh the timeline
      mPager.setCurrentItem(0);
      HomeTimelineFragment homeTimeline =
        (HomeTimelineFragment)mPagerAdapter.getRegisteredFragment(0);
      homeTimeline.onActivityResult(requestCode, resultCode, data);
    }
  }

  public User getLoggedInUser() {
    return mCurrentUser;
  }

  private void getCurrentUser() {
    TwitterApplication.getRestClient().getCurrentUserDetails(new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          mCurrentUser = User.fromJSON(response);
          View navHeaderView = mNvNavigationView.getHeaderView(0);
          ImageView navHeaderImage = (ImageView) navHeaderView.findViewById(R.id.ivNavHeader);
          Glide
            .with(TimelineActivity.this)
            .load(mCurrentUser.getProfileImageURL())
            .into(navHeaderImage);
          TextView navProfileName = (TextView) navHeaderView.findViewById(R.id.navHeaderProfileName);
          TextView navScreenName = (TextView) navHeaderView.findViewById(R.id.navHeaderScreenName);
          navProfileName.setText(mCurrentUser.getName());
          navScreenName.setText(mCurrentUser.getScreenName());
          mDrawerLayout.addDrawerListener(mDrawerToggle);
          mNvNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              switch (item.getItemId()) {
                case R.id.profileMenuItem:
                  Intent i = new Intent(TimelineActivity.this, UserProfileActivity.class);
                  i.putExtra("screen_name", mCurrentUser.getScreenName());
                  startActivity(i);
                  break;
                case R.id.logoutMenuItem:
                  TwitterApplication.getRestClient().clearAccessToken();
                  Intent login = new Intent(TimelineActivity.this, LoginActivity.class);
                  startActivity(login);
                  break;
                default:
                  return true;
              }
              return true;
            }
          });
        } catch (Exception e) {
          Log.d("User fetch failed", e.toString());
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
      }
    });
  }
}
