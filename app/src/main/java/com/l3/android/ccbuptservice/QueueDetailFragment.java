package com.l3.android.ccbuptservice;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Ihsan on 15/2/5.
 */
public class QueueDetailFragment extends Fragment {
    public static final String TAG="QueueDetailFragment";
    public static final String EXTRA_QUEUE_ID =
            "com.l3.android.ccbuptservice.queue_id";

    private TextView mNowTextView, mTotalTextView;

    private Queue mQueue;

    public static QueueDetailFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_QUEUE_ID, id);

        QueueDetailFragment fragment = new QueueDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int queueId=-1;
        if (getActivity()!=null){
            queueId=getArguments().getInt(EXTRA_QUEUE_ID);
        }
        if (queueId!=-1){
            mQueue=QueueArray.get(getActivity()).getQueue(queueId);
            ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(mQueue.getTitle());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queue_detail, container, false);

        mNowTextView = (TextView) view.findViewById(R.id.queue_detail_now_textView);
        mTotalTextView = (TextView) view.findViewById(R.id.queue_detail_total_textView);

        new GetDetailTask().execute();

        return view;
    }

    private class GetDetailTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {

            String fetchUrl = "http://10.168.1.124/CCBUPTService/getqueuedetail.php";
            String url = Uri.parse(fetchUrl).buildUpon()
                    .appendQueryParameter("queueId", String.valueOf(mQueue.getId()))
                    .build().toString();
            try {
                String result = new DataFetcher().getUrl(url);
                JSONObject jsonObject=new JSONObject(result);
                mQueue.setNextNumber(jsonObject.getInt("nextNumber"));
                mQueue.setTotal(jsonObject.getInt("total"));
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: ", ioe);
            } catch (JSONException jsone) {
                Log.e(TAG, "Failed to parse detail", jsone);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mNowTextView.setText(String.valueOf(mQueue.getNextNumber()));
            mTotalTextView.setText(String.valueOf(mQueue.getTotal()));
        }
    }
}
