package com.codepath.apps.tweetaway.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.activities.FollowActivity;
import com.codepath.apps.tweetaway.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by rpraveen on 11/5/16.
 */

public class ProfileHeaderFragment extends FragmentBase {

  @BindView(R.id.ivProfileBanner) ImageView mIvProfileBanner;
  @BindView(R.id.ivProfileImage) ImageView mIvProfileImage;
  @BindView(R.id.tvTwitterName) TextView mTvTwitterName;
  @BindView(R.id.tvProfileName) TextView mTvProfileName;
  @BindView(R.id.tvProfileDescription) TextView mTvProfileDescription;
  @BindView(R.id.layoutLocationAndContact) LinearLayout mLocationAndContactHolder;
  @BindView(R.id.ivLocationIcon) ImageView mIvLocationIcon;
  @BindView(R.id.tvLocation) TextView mTvLocation;
  @BindView(R.id.ivWebsite) ImageView mIvWebsite;
  @BindView(R.id.tvWebsite) TextView mTvWebsite;
  @BindView(R.id.layoutFollowContent) LinearLayout mFollowContentHolder;
  @BindView(R.id.tvFollowers) TextView mTvFollowers;
  @BindView(R.id.tvFollowing) TextView mTvFollowing;
  @BindView(R.id.followingHolder) LinearLayout mFollowingHolder;
  @BindView(R.id.followersHolder) LinearLayout mFollowersHolder;

  public static ProfileHeaderFragment newInstance(String userName) {
    ProfileHeaderFragment fragment = new ProfileHeaderFragment();
    Bundle args = new Bundle();
    args.putString("screen_name", userName);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
    View view = inflater.inflate(R.layout.fragment_user_profile_header, container, false);
    ButterKnife.bind(this, view);
    mClient.getUserProfile(getArguments().getString("screen_name"), new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          final User user = User.fromJSON(response);
          if (user.getProfileBanner() != null) {
            Glide
            .with(getActivity())
            .load(user.getProfileBanner())
            .into(mIvProfileBanner);
          }
          if (user.getProfileImageURL() != null) {
            Glide
            .with(getActivity())
            .load(user.getProfileImageURL())
            .into(mIvProfileImage);
          }
          mTvProfileName.setText(user.getName());
          mTvTwitterName.setText(user.getScreenName());
          mTvProfileDescription.setText(user.getProfileDescription());
          mTvFollowers.setText("" + user.getFollowersCount());
          mTvFollowing.setText("" + user.getFollowingCount());
          mFollowingHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i = new Intent(getActivity(), FollowActivity.class);
              i.putExtra(FollowActivity.IS_FOLLOWING_ARG, true);
              i.putExtra(FollowActivity.SCREEN_NAME_ARG, user.getScreenName());
              startActivity(i);
            }
          });
          mFollowersHolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
              Intent i = new Intent(getActivity(), FollowActivity.class);
              i.putExtra(FollowActivity.IS_FOLLOWING_ARG, false);
              i.putExtra(FollowActivity.SCREEN_NAME_ARG, user.getScreenName());
              startActivity(i);
            }
          });
          mTvLocation.setVisibility(View.GONE);
          mIvLocationIcon.setVisibility(View.GONE);
          mIvWebsite.setVisibility(View.GONE);
          mTvWebsite.setVisibility(View.GONE);
          if (user.getLocation() != null && !user.getLocation().trim().equalsIgnoreCase("")) {
            mTvLocation.setText(user.getLocation());
            mIvLocationIcon.setVisibility(View.VISIBLE);
            mTvLocation.setVisibility(View.VISIBLE);
          }
          if (user.getUrl() != null) {
            mTvWebsite.setText(user.getUrl());
            mIvWebsite.setVisibility(View.VISIBLE);
            mTvWebsite.setVisibility(View.VISIBLE);
          }
        } catch (Exception e) {
          showToast("user parse failed");
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        showToast("user fetch failed");
      }
    });
    return view;
  }

  private void showToast(String toast) {
    Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT);
  }
}
