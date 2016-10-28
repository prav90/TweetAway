package com.codepath.apps.tweetaway.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rpraveen on 10/27/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
  public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
    super(context, android.R.layout.simple_list_item_1, tweets);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Tweet tweet = getItem(position);
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
    }
    ImageView profileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
    TextView profileName = (TextView) convertView.findViewById(R.id.tvProfileShortName);
    TextView tweetText = (TextView) convertView.findViewById(R.id.tvTweetBody);
    profileName.setText(tweet.getUser().getName());
    tweetText.setText(tweet.getBody());
    profileImage.setImageResource(android.R.color.transparent);
    Picasso.with(getContext()).load(tweet.getUser().getProfileImageURL()).into(profileImage);
    return convertView;
  }
}
