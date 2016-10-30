package com.codepath.apps.tweetaway.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.models.Tweet;
import com.codepath.apps.tweetaway.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rpraveen on 10/28/16.
 */
public class TweetsRecyclerAdapter extends RecyclerView.Adapter<TweetsRecyclerAdapter.TweetHolder> {

  private List<Tweet> mTweets;
  private Context mContext;

  public TweetsRecyclerAdapter(Context context, List<Tweet> tweets) {
    mTweets = tweets;
    mContext = context;
  }

  @Override
  public TweetsRecyclerAdapter.TweetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(mContext);
    View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
    return new TweetHolder(tweetView);
  }

  @Override
  public void onBindViewHolder(TweetHolder holder, int position) {
    Tweet currentTweet = mTweets.get(position);
    holder.tvProfileName.setText(currentTweet.getUser().getName());
    holder.tvTweetBody.setText(currentTweet.getBody());
    holder.ivProfileImage.setImageResource(android.R.color.transparent);
    holder.tvTimeSinceTweet.setText(
    DateUtil.getRelativeTimeForTweet(currentTweet.getCreatedAt())
    );
    Picasso
      .with(mContext)
      .load(currentTweet.getUser().getProfileImageURL())
      .into(holder.ivProfileImage);
  }

  @Override
  public int getItemCount() {
    return mTweets.size();
  }

  public static class TweetHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvProfileShortName) TextView tvProfileName;
    @BindView(R.id.tvTweetBody) TextView tvTweetBody;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvTimeSinceTweet) TextView tvTimeSinceTweet;

    public TweetHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}