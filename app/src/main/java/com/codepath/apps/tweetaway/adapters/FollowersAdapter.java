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
import com.codepath.apps.tweetaway.models.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by rpraveen on 11/6/16.
 */

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.FollowItemHolder> {

  private List<User> mUsers;
  private Context mContext;
  private OnFollowItemClickListener mListener;

  public FollowersAdapter(Context context, OnFollowItemClickListener listener, List<User> users) {
    mUsers = users;
    mContext = context;
    mListener = listener;
  }

  @Override
  public FollowItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(mContext);
    View followItemView = inflater.inflate(R.layout.item_follow, parent, false);
    return new FollowItemHolder(followItemView);
  }

  @Override
  public void onBindViewHolder(FollowItemHolder holder, int position) {
    User user = mUsers.get(position);
    holder.tvProfileName.setText(user.getName());
    holder.tvTwitterName.setText(user.getScreenName());
    holder.tvProfileDescription.setText(user.getProfileDescription());
    Glide
      .with(mContext)
      .load(user.getProfileImageURL())
      .bitmapTransform(new RoundedCornersTransformation(mContext, 1, 1))
      .into(holder.ivProfileImage);

  }

  @Override
  public int getItemCount() {
    return mUsers.size();
  }

  public class FollowItemHolder
    extends RecyclerView.ViewHolder
    implements View.OnClickListener {
    @BindView(R.id.tvProfileName) TextView tvProfileName;
    @BindView(R.id.tvTwitterName) TextView tvTwitterName;
    @BindView(R.id.tvProfileDescription) TextView tvProfileDescription;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;

    public FollowItemHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      int position = getAdapterPosition();
      User user = mUsers.get(position);
      mListener.onFollowItemClickListener(user.getScreenName());
    }
  }

  public interface OnFollowItemClickListener {
    public void onFollowItemClickListener(String screenName);
  }
}
