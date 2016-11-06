package com.codepath.apps.tweetaway.fragments;

import android.support.v4.app.Fragment;

import com.codepath.apps.tweetaway.application.TwitterApplication;
import com.codepath.apps.tweetaway.network.TwitterClient;

/**
 * Created by rpraveen on 11/5/16.
 */

public class FragmentBase extends Fragment {

  protected TwitterClient mClient;

  public FragmentBase() {
    mClient = TwitterApplication.getRestClient();
  }
}
