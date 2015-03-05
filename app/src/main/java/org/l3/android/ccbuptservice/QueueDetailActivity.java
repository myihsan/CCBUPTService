package org.l3.android.ccbuptservice;

import android.app.Fragment;

/**
 * Created by Ihsan on 15/2/5.
 */
public class QueueDetailActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        int queueId = getIntent().getIntExtra(QueueDetailFragment.EXTRA_QUEUE_ID, -1);
        return QueueDetailFragment.newInstance(queueId);
    }
}
