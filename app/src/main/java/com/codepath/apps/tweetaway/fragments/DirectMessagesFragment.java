package com.codepath.apps.tweetaway.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.tweetaway.R;
import com.codepath.apps.tweetaway.adapters.DividerItemDecoration;
import com.codepath.apps.tweetaway.adapters.MessagesAdapter;
import com.codepath.apps.tweetaway.models.Message;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by rpraveen on 11/6/16.
 */

public class DirectMessagesFragment extends FragmentBase {

  @BindView(R.id.rvDirectMessages) RecyclerView mRvDirectMessages;

  private List<Message> mDirectMessages;
  private MessagesAdapter mMessagesAdapter;

  public static DirectMessagesFragment newInstance() {
    return new DirectMessagesFragment();
  }

  @Override
  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    mDirectMessages = new ArrayList<>();
    mMessagesAdapter = new MessagesAdapter(getActivity(), mDirectMessages);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
    View view = inflater.inflate(R.layout.fragment_direct_messages, container, false);
    ButterKnife.bind(this, view);
    RecyclerView.ItemDecoration itemDecoration =
      new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
    mRvDirectMessages.addItemDecoration(itemDecoration);
    LinearLayoutManager rvLayoutManager = new LinearLayoutManager(getActivity());
    mRvDirectMessages.setLayoutManager(rvLayoutManager);
    mRvDirectMessages.setAdapter(mMessagesAdapter);
    getDirectMessages();
    return view;
  }

  private void getDirectMessages() {
    mClient.getDirectMessages(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        try {
          mDirectMessages.addAll(Message.fromJSONArray(response));
          mMessagesAdapter.notifyDataSetChanged();
        } catch (Exception e) {
          Toast.makeText(getActivity(), "Message parse failed", Toast.LENGTH_SHORT);
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(getActivity(), "Message fetch failed", Toast.LENGTH_SHORT);
      }
    });
  }
}
