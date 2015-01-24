package com.l3.android.ccbuptservice;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        NoticeAdapter adapter = new NoticeAdapter(mNotices);
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

    public class NoticeAdapter extends ArrayAdapter<Notice> {

        public NoticeAdapter(ArrayList<Notice> notices) {
            super(getActivity(), 0, notices);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView==null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_notice,null);
            }

            // Configure the view for this notice
            Notice notice = getItem(position);

            TextView titleTextView =
                    (TextView)convertView.findViewById(R.id.notice_list_item_titleTextView);
            titleTextView.setText(notice.getTitle());

            TextView dateTimeTextView =
                    (TextView)convertView.findViewById(R.id.notice_list_item_dateTimeTextView);
            dateTimeTextView.setText(notice.getDateTime());

            return convertView;
        }
    }
}
