package com.l3.android.ccbuptservice;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.tencent.android.tpush.XGPushManager;

public class SettingsFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private ListPreference specialty;
    private String oldSpecialty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.fragment_settings);
        specialty = (ListPreference) findPreference("specialty");
        oldSpecialty = specialty.getValue();
        if (oldSpecialty != null) {
            specialty.setSummary(oldSpecialty);
        }
        specialty.setOnPreferenceChangeListener(this);
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
        if (preference == specialty) {
            String newSpecialty = String.valueOf(objValue);
            specialty.setSummary(newSpecialty);
            if (oldSpecialty != null) {
				XGPushManager.deleteTag(getActivity().getApplicationContext(), oldSpecialty);
            }
			XGPushManager.setTag(getActivity().getApplicationContext(), newSpecialty);
            oldSpecialty = newSpecialty;
        }
        return true;
    }
}
