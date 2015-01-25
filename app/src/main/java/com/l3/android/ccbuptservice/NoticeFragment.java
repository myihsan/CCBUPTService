package com.l3.android.ccbuptservice;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ihsan on 15/1/25.
 */
public class NoticeFragment extends Fragment {
    public static final String EXTRA_NOTICE_ID =
            "com.l3.android.ccbuptservice.notice_id";

    private Notice mNotice;
    private TextView mDateTimeTextView;
    private TextView mTitleTextView;
    private TextView mContentTextView;

    public static NoticeFragment newInstance(int noticeId) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_NOTICE_ID, noticeId);

        NoticeFragment fragment = new NoticeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int noticeId = getArguments().getInt(EXTRA_NOTICE_ID);
        if (noticeId != -1) {
            mNotice = NoticeArray.get(getActivity()).getNotice(noticeId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        mTitleTextView = (TextView) view.findViewById(R.id.notice_titleTextView);
        mContentTextView = (TextView) view.findViewById(R.id.notice_contentTextView);
        mDateTimeTextView = (TextView) view.findViewById(R.id.notice_dateTimeTextView);
        if (mNotice != null) {
            mDateTimeTextView.setText(mNotice.getDateTime());
            mTitleTextView.setText(mNotice.getTitle());
            mContentTextView.setText(mNotice.getContent());
        }
        return view;
    }
}
