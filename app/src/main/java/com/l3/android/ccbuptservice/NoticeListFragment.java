package com.l3.android.ccbuptservice;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ihsan on 15/1/23.
 */
public class NoticeListFragment extends ListFragment {
    private static final String TAG = "NoticeFragment";

    ArrayList<Notice> mNotices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchNoticeTask().execute();


    }

    public void setupAdapter() {
        ArrayAdapter<Notice> adapter = new ArrayAdapter<Notice>(getActivity(),
                android.R.layout.simple_list_item_1, mNotices);
        setListAdapter(adapter);
    }

    private class FetchNoticeTask extends AsyncTask<Void, Void, ArrayList<Notice>> {
        @Override
        protected ArrayList<Notice> doInBackground(Void... params) {
            return new NoticeFetcher().fetchNotice();
        }

        @Override
        protected void onPostExecute(ArrayList<Notice> notices) {
            mNotices = notices;
            setupAdapter();
        }
    }
}
