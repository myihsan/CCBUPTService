package org.l3.android.ccbuptservice;

import android.app.Fragment;

/**
 * Created by Ihsan on 15/2/5.
 */
public class QueueListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new QueueListFragment();
    }
}
