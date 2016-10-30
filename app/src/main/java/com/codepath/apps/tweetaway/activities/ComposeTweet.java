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

import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

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

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compose_tweet);
    ButterKnife.bind(this);
    User currentUser = Parcels.unwrap(getIntent().getParcelableExtra("current_user"));

    // intialize views with values
    tvTweetCharsLeft.setText(""+TWEET_MAX_CHARS);
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
        tvTweetCharsLeft.setText(""+charsLeft);
      }
    });

    btNewTweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
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
    timeLineIntent.putExtra("statusUpdate", etNewStatus.getText().toString());
    setResult(RESULT_OK, timeLineIntent);
    finish();
  }
}
