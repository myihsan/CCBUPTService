package com.l3.android.ccbuptservice;

import android.app.Fragment;

/**
 * Created by Ihsan on 15/1/25.
 */
public class NoticeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        int noticeId = getIntent().getIntExtra(NoticeFragment.EXTRA_NOTICE_ID,-1);

        return NoticeFragment.newInstance(noticeId);
    }
}
