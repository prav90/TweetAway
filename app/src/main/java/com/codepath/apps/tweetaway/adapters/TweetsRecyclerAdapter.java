package com.codepath.apps.tweetaway.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.models.Tweet;
import com.codepath.apps.tweetaway.utils.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
    holder.tvTwitterName.setText(currentTweet.getUser().getScreenName());
    holder.tvTweetBody.setText(currentTweet.getBody());
    holder.ivProfileImage.setImageResource(android.R.color.transparent);
    holder.ivTweetImage.setImageResource(android.R.color.transparent);
    holder.ivTweetImage.setVisibility(View.GONE);

    holder.tvTimeSinceTweet.setText(
      DateUtil.getRelativeTimeForTweet(currentTweet.getCreatedAt())
    );
    holder.tvRetweets.setText(currentTweet.getRetweetCount() + "");
    holder.tvFavorites.setText(currentTweet.getFavoriteCount() + "");
    Glide
      .with(mContext)
      .load(currentTweet.getUser().getProfileImageURL())
      .bitmapTransform(new RoundedCornersTransformation(mContext, 1, 1))
      .into(holder.ivProfileImage);
    if (currentTweet.getMedia().isImage()) {
      holder.ivTweetImage.setVisibility(View.VISIBLE);
      Glide
        .with(mContext)
        .load(currentTweet.getMedia().getImage("small"))
        .bitmapTransform(new RoundedCornersTransformation(mContext, 10, 10))
        .into(holder.ivTweetImage);
    }
  }

  @Override
  public int getItemCount() {
    return mTweets.size();
  }

  public static class TweetHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvProfileShortName) TextView tvProfileName;
    @BindView(R.id.tvTwitterName) TextView tvTwitterName;
    @BindView(R.id.tvTweetBody) TextView tvTweetBody;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvTimeSinceTweet) TextView tvTimeSinceTweet;
    @BindView(R.id.ivTweetImage) ImageView ivTweetImage;

    // footer
    @BindView(R.id.tvRetweets) TextView tvRetweets;
    @BindView(R.id.tvFavorites) TextView tvFavorites;

    public TweetHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
