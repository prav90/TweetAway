package com.codepath.apps.tweetaway.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.fragments.SearchResultsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rpraveen on 11/6/16.
 */

public class SearchActivity extends AppCompatActivity {
  @BindView(R.id.toolBar) Toolbar mToolBar;
  @BindView(R.id.flSearchResultsContainer) FrameLayout mFlSearchResultsContainer;

  @Override
  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    setContentView(R.layout.activity_search_results);
    ButterKnife.bind(this);

    String searchQuery = getIntent().getStringExtra("search_query");
    SearchResultsFragment fragment = SearchResultsFragment.newInstance(searchQuery);
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.flSearchResultsContainer, fragment).commit();
    setSupportActionBar(mToolBar);
    getSupportActionBar().setTitle("Search Results for " + searchQuery);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
