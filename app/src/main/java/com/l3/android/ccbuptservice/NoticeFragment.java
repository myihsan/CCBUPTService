package com.l3.android.ccbuptservice;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ihsan on 15/1/25.
 */
public class NoticeFragment extends Fragment {
    private static final String TAG = "NoticeFragment";
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
        int noticeId = getArguments().getInt(EXTRA_NOTICE_ID,-1);
        if (noticeId != -1) {
            mNotice = NoticeArray.get(getActivity()).getNotice(noticeId);
        }
        setHasOptionsMenu(true);
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


    @Override
    public void onResume() {
        super.onResume();

        XGPushClickedResult clickedResult = XGPushManager.onActivityStarted(getActivity());
        if (clickedResult != null) {
            String customContent = clickedResult.getCustomContent();
            if (customContent != null & customContent.length() != 0) {
                try {
                    JSONObject jsonObject = new JSONObject(customContent);
                    String title = jsonObject.getString("title");
                    String content = jsonObject.getString("content");
                    String dateTime = jsonObject.getString("dateTime");
                    mDateTimeTextView.setText(dateTime);
                    mTitleTextView.setText(title);
                    mContentTextView.setText(content);
                } catch (JSONException jsone) {
                    Log.e(TAG, "Failed to parse custom content", jsone);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(getActivity());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
