package org.l3.android.ccbuptservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by Ihsan on 15/1/23.
 */
public class NoticeListFragment extends Fragment {
    private static final String TAG = "NoticeListFragment";

    private ListView mListView;
    private PtrFrameLayout mFrame;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);
        mListView = (ListView) view.findViewById(R.id.notice_list_listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                Notice notice = ((NoticeAdapter) mListView.getAdapter()).getItem(position);
                intent.putExtra(NoticeFragment.EXTRA_NOTICE_ID, notice.getId());
                startActivity(intent);
            }
        });
        NoticeAdapter adapter = new NoticeAdapter(NoticeArray.get(getActivity()).getNotices());
        mListView.setAdapter(adapter);

        mFrame = (PtrFrameLayout) view.findViewById(R.id.notice_list_material_style_ptr_frame);

        // header
        final MaterialHeader header = new MaterialHeader(getActivity());
        int[] colors = getResources().getIntArray(R.array.header_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DisplayUtil.dp2px(getActivity(), 10), 0, DisplayUtil.dp2px(getActivity(), 10));
        header.setPtrFrameLayout(mFrame);

        mFrame.setLoadingMinTime(1000);
        mFrame.setDurationToCloseHeader(1000);
        mFrame.setHeaderView(header);
        mFrame.addPtrUIHandler(header);

        mFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                new FetchNoticeTask().execute();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListView != null) {
            mListView.setStackFromBottom(false);
            mFrame.autoRefresh(true);
        }
    }

    public void updateAdapter() {
        ((NoticeAdapter) mListView.getAdapter()).notifyDataSetChanged();
    }

    private class FetchNoticeTask extends AsyncTask<Void, Void, ArrayList<Notice>> {
        @Override
        protected ArrayList<Notice> doInBackground(Void... params) {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            String specialty;
            String grade;
            if (preferences.contains("specialty")
                    && (specialty = preferences.getString("specialty", null).toString()) != null
                    && preferences.contains("grade")
                    && (grade = preferences.getString("grade", null).toString()) != null) {
                return new DataFetcher(getActivity()).fetchNoticeBySpecialty(grade+specialty);
            } else {
                return new DataFetcher(getActivity()).fetchNotice();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Notice> notices) {
            mFrame.refreshComplete();

            NoticeArray.get(getActivity()).refreshNotices(0, notices);
            updateAdapter();
        }
    }

    public class NoticeAdapter extends ArrayAdapter<Notice> {

        public NoticeAdapter(ArrayList<Notice> notices) {
            super(getActivity(), 0, notices);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_notice, null);
            }

            // Configure the view for this notice
            Notice notice = getItem(position);

            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.notice_list_item_titleTextView);
            titleTextView.setText(notice.getTitle());

            TextView dateTimeTextView =
                    (TextView) convertView.findViewById(R.id.notice_list_item_dateTimeTextView);
            dateTimeTextView.setText(notice.getDateTime());

            return convertView;
        }
    }
}
