package com.l3.android.ccbuptservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.tencent.android.tpush.XGPushConfig;

import java.io.IOException;

/**
 * Created by Ihsan on 15/2/5.
 */
public class QueueFragment extends Fragment {
    private static final String TAG = "QueueFragment";
    private static final int CHOOSE_QUEUE = 1;

    private LinearLayout mQueuedLinearLayout;
    private TextView mMyNumberTextView, mMyIdentificationTextView, mQueueNowTextView;
    private FloatingActionButton mFAButton;
    private Button mQuitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queue, container, false);

        mQueuedLinearLayout = (LinearLayout) view.findViewById(R.id.queue_queued_linearLayout);
        mMyNumberTextView = (TextView) view.findViewById(R.id.queue_my_number_textView);
        mMyIdentificationTextView = (TextView) view.findViewById(R.id.queue_my_identification);
        mQueueNowTextView = (TextView) view.findViewById(R.id.queue_now_textView);
        mFAButton = (FloatingActionButton) view.findViewById(R.id.notice_list_fab);
        mQuitButton = (Button) view.findViewById(R.id.queue_quit_queue_button);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QuitQueueTask().execute();
            }
        });

        mFAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QueueListActivity.class);
                startActivityForResult(intent, CHOOSE_QUEUE);
            }
        });

        mQueuedLinearLayout.setVisibility(View.INVISIBLE);
        checkQueuedQueue();
        return view;
    }

    private void checkQueuedQueue() {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // If already queued a queue, show the detail
        if (pre.contains(getString(R.string.queued_number))
                && pre.contains(getString(R.string.queued_queue))) {
            mFAButton.setVisibility(View.INVISIBLE);
            mQueuedLinearLayout.setVisibility(View.VISIBLE);
            mMyNumberTextView.setText(String.valueOf(
                    pre.getInt(getString(R.string.queued_number), -1)));
            mMyIdentificationTextView.setText(XGPushConfig.getToken(getActivity()));
            new getNowQueuerTask().execute();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkQueuedQueue();
    }

    private class getNowQueuerTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            int queueId = PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getInt(getString(R.string.queued_queue), -1);
            return new DataFetcher(getActivity()).fetchNowQueuer(queueId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer != -1) {
                mQueueNowTextView.setText(String.valueOf(integer));
            } else {
                Toast.makeText(getActivity(), "排队更新失败，请重试", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class QuitQueueTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean flag = false;
            int queueId = PreferenceManager
                    .getDefaultSharedPreferences(getActivity())
                    .getInt(getString(R.string.queued_queue), -1);
            int number = Integer.valueOf(mMyNumberTextView.getText().toString());
            String token = XGPushConfig.getToken(getActivity());
            String fetchUrl = getString(R.string.root_url) + "quitqueue.php";
            String url = Uri.parse(fetchUrl).buildUpon()
                    .appendQueryParameter("queueId", String.valueOf(queueId))
                    .appendQueryParameter("number", String.valueOf(number))
                    .appendQueryParameter("token", token)
                    .build().toString();
            Log.d(TAG, url);
            try {
                String result = new DataFetcher(getActivity()).getUrl(url);
                Log.d(TAG, result);
                if (result.equals("succeed")) {
                    flag = true;
                }
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: ", ioe);
            }
            return flag;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .remove(getString(R.string.queued_queue))
                        .remove(getString(R.string.queued_number))
                        .commit();
                mQueuedLinearLayout.setVisibility(View.INVISIBLE);
                mFAButton.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "处理失败，请重试", Toast.LENGTH_LONG).show();
            }
        }
    }
}
