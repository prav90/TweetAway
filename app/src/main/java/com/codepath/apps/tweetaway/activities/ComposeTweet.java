package com.codepath.apps.tweetaway.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.application.TwitterApplication;
import com.codepath.apps.tweetaway.models.Tweet;
import com.codepath.apps.tweetaway.models.User;
import com.codepath.apps.tweetaway.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by rpraveen on 10/29/16.
 */

public class ComposeTweet extends AppCompatActivity {

  private static final int TWEET_MAX_CHARS = 140;

  @BindView(R.id.ivCancelCompose) ImageView ivCancelCompose;
  @BindView(R.id.tvTweetCharsLeft) TextView tvTweetCharsLeft;
  @BindView(R.id.btNewTweet) Button btNewTweet;
  @BindView(R.id.etNewStatus) EditText etNewStatus;
  @BindView(R.id.tvProfileShortName) TextView tvProfileName;
  @BindView(R.id.tvFullName) TextView tvFullName;
  @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
  @BindView(R.id.replyToBlock) TextView tvReplyToBlock;

  private String mReplyTo = "";
  private Tweet mReplyToTweet;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compose_tweet);
    ButterKnife.bind(this);
    User currentUser = Parcels.unwrap(getIntent().getParcelableExtra("current_user"));
    mReplyToTweet = Parcels.unwrap(getIntent().getParcelableExtra("reply_to_tweet"));

    if (mReplyToTweet != null) {
      mReplyTo = mReplyToTweet.getUser().getScreenName();
      etNewStatus.setText(mReplyTo);
      btNewTweet.setText(R.string.compose_reply);
      tvReplyToBlock.setText("Reply to " + mReplyToTweet.getUser().getScreenName());
      tvReplyToBlock.setVisibility(View.VISIBLE);
    }

    // intialize views with values
    tvTweetCharsLeft.setText(""+ (TWEET_MAX_CHARS - mReplyTo.length()));
    if (currentUser != null) {
      tvFullName.setText(currentUser.getName());
      tvProfileName.setText(currentUser.getScreenName());
      Picasso
        .with(this)
        .load(currentUser.getProfileImageURL())
        .into(ivProfileImage);
    }

    // set up view listeners
    etNewStatus.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence status, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable status) {
        int charsLeft = TWEET_MAX_CHARS - status.length();
        if (charsLeft < 0) {
          btNewTweet.setEnabled(false);
        } else if (!btNewTweet.isEnabled()){
          btNewTweet.setEnabled(true);
        }
        if (!status.toString().contains(mReplyTo)) {
          btNewTweet.setText(R.string.compose_Tweet);
          tvReplyToBlock.setVisibility(View.GONE);
        }
        tvTweetCharsLeft.setText(""+charsLeft);
      }
    });

    btNewTweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // if it's a reply post the reply in this activity
        String replyString = getString(R.string.compose_reply);
        if (btNewTweet.getText().equals(replyString)) {
          TwitterClient client = TwitterApplication.getRestClient();
          client.replyToTweet(
            etNewStatus.getText().toString(),
            mReplyToTweet.getUid(),
            new JsonHttpResponseHandler(){
              @Override
              public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ComposeTweet.this, "Sending reply failed", Toast.LENGTH_SHORT);
              }

              @Override
              public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(ComposeTweet.this, "Reply sent", Toast.LENGTH_SHORT);
                ComposeTweet.this.onBackPressed();
              }
            }
          );
        }
        returnToTimelineActivity(etNewStatus.getText().toString());
      }
    });

    ivCancelCompose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        returnToTimelineActivity("");
      }
    });
  }

  private void returnToTimelineActivity(String status) {
    Intent timeLineIntent = new Intent();
    timeLineIntent.putExtra("statusUpdate", status);
    setResult(RESULT_OK, timeLineIntent);
    finish();
  }
}
