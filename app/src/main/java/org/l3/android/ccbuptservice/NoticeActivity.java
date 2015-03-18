package org.l3.android.ccbuptservice;

import android.app.Fragment;
import android.util.Log;

/**
 * Created by Ihsan on 15/1/25.
 */
public class NoticeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        int noticeId = getIntent().getIntExtra(NoticeFragment.EXTRA_NOTICE_ID,-1);
        Log.d("NoticeActivity", "NoticeActivity onCreate" + getTaskId());
        return NoticeFragment.newInstance(noticeId);
    }
}
