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
import com.codepath.apps.tweetaway.models.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by rpraveen on 11/6/16.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder> {

  private List<Message> mMessages;
  private Context mContext;

  public MessagesAdapter(Context context, List<Message> messages) {
    mMessages = messages;
    mContext = context;
  }

  @Override
  public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(mContext);
    View view = inflater.inflate(R.layout.item_message, parent, false);
    return new MessageHolder(view);
  }

  @Override
  public void onBindViewHolder(MessageHolder holder, int position) {
    Message message = mMessages.get(position);
    holder.mTvMessageSender.setText(message.getSenderScreenName());
    holder.mTvMessageText.setText(message.getText());
    holder.mTvTimeSent.setText(message.getCreatedAt());
    Glide
      .with(mContext)
      .load(message.getSenderProfileImageUrl())
      .bitmapTransform(new RoundedCornersTransformation(mContext, 1, 1))
      .into(holder.mIvMessageSender);
  }

  @Override
  public int getItemCount() {
    return mMessages.size();
  }

  class MessageHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivMessageSender) ImageView mIvMessageSender;
    @BindView(R.id.tvMessageSenderScreenName) TextView mTvMessageSender;
    @BindView(R.id.tvMessageText) TextView mTvMessageText;
    @BindView(R.id.tvTimeSent) TextView mTvTimeSent;

    public MessageHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
