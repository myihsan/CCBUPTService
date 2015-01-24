package com.l3.android.ccbuptservice;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

//import com.tencent.android.tpush.XGPushManager;

public class SettingsFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private ListPreference department;
    private String oldDepartment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.fragment_settings);
        department = (ListPreference) findPreference("department");
        oldDepartment = department.getValue();
        if (oldDepartment != null) {
            department.setSummary(oldDepartment);
        }
        department.setOnPreferenceChangeListener(this);
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

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == department) {
            String newDepartment = String.valueOf(objValue);
//			Toast.makeText(getActivity(), String.valueOf(objValue)+":"+oldDepartment,
//					Toast.LENGTH_SHORT).show();
            department.setSummary(newDepartment);
            if (oldDepartment != null) {
//				XGPushManager.deleteTag(getActivity().getApplicationContext(), oldDepartment);
            }
//			XGPushManager.setTag(getActivity().getApplicationContext(), newDepartment);
            oldDepartment = newDepartment;
        }
        return true;
    }
}
