package org.l3.android.ccbuptservice;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Ihsan on 15/1/22.
 */
public class SettingsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {

        return new SettingsFragment();
    }
}
