package org.l3.android.ccbuptservice;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.tencent.android.tpush.XGPushManager;

public class SettingsFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private ListPreference mSpecialty, mGrade;
    private String mOldSpecialty, mOldGrade;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.fragment_settings);
        mSpecialty = (ListPreference) findPreference("specialty");
        mOldSpecialty = mSpecialty.getValue();
        if (mOldSpecialty != null) {
            mSpecialty.setSummary(mOldSpecialty);
        }
        mSpecialty.setOnPreferenceChangeListener(this);

        mGrade = (ListPreference) findPreference("grade");
        mOldGrade = mGrade.getValue();
        if (mOldGrade != null) {
            mGrade.setSummary(mOldGrade);
        }
        mGrade.setOnPreferenceChangeListener(this);
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
        if (preference == mSpecialty) {
            String newSpecialty = String.valueOf(objValue);
            mSpecialty.setSummary(newSpecialty);
            if (mOldGrade!=null) {
                XGPushManager.setTag(getActivity().getApplicationContext(), mOldGrade+newSpecialty);
                if (mOldSpecialty != null) {
                    XGPushManager.deleteTag(getActivity().getApplicationContext(), mOldGrade+mOldSpecialty);
                }
            }
            mOldSpecialty = newSpecialty;
        }else if (preference==mGrade){
            String newGrade = String.valueOf(objValue);
            mGrade.setSummary(newGrade);
            if (mOldSpecialty!=null) {
                XGPushManager.setTag(getActivity().getApplicationContext(), newGrade+mOldSpecialty);
                if (mOldGrade != null) {
                    XGPushManager.deleteTag(getActivity().getApplicationContext(), mOldGrade+mOldSpecialty);
                }
            }
            mOldGrade = newGrade;
        }
        return true;
    }
}
