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
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queue, container, false);

        mQueuedLinearLayout = (LinearLayout) view.findViewById(R.id.queue_queued_linearLayout);
        mMyNumberTextView = (TextView) view.findViewById(R.id.queue_my_number_textView);
        mMyIdentificationTextView = (TextView) view.findViewById(R.id.queue_my_identification);
        mQueueNowTextView = (TextView) view.findViewById(R.id.queue_now_textView);
        mFAButton = (FloatingActionButton) view.findViewById(R.id.notice_list_fab);

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
                    pre.getInt(getString(R.string.queued_queue), -1)));
            mMyIdentificationTextView.setText(XGPushConfig.getToken(getActivity()));
            new getNowQueuerTask().execute();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkQueuedQueue();
    }

    private int getNowQueuer() {
        int queueId = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getInt(getString(R.string.queued_queue), -1);
        if (queueId != -1) {
            String result = null;
            String fetchUrl = "http://10.168.1.124/CCBUPTService/getnowqueuer.php";
            String url = Uri.parse(fetchUrl).buildUpon()
                    .appendQueryParameter("queueId", String.valueOf(queueId))
                    .build().toString();
            Log.d(TAG, url);
            try {
                result = new DataFetcher().getUrl(url);
                return Integer.valueOf(result);
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: ", ioe);
                return -1;
            } catch (NumberFormatException nfe){
                return -1;
            }
        }
        return -1;
    }

    private class getNowQueuerTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            return getNowQueuer();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer != -1) {
                mQueueNowTextView.setText(String.valueOf(integer));
            }
        }
    }


}
