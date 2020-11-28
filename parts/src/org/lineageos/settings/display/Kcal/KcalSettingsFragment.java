/*
 * Copyright (C) 2020 ArrowOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.display;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragment;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreference;

import org.lineageos.settings.R;
import org.lineageos.settings.utils.FileUtils;
import org.lineageos.settings.display.KcalUtils;

public class KcalSettingsFragment extends PreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String TAG = "KcalSettings";

    private SwitchPreference mKcalSwitchPreference;
    private SeekBarPreference mRedColorSlider;
    private SeekBarPreference mGreenColorSlider;
    private SeekBarPreference mBlueColorSlider;
    private String mInitialStatus;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.kcal_settings);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        mKcalSwitchPreference = (SwitchPreference) findPreference("kcal_enable");

        // Check if the node exists and enable / disable the preference depending on the case
        if (FileUtils.fileExists(KcalUtils.KCAL_ENABLE_NODE)) {
            mKcalSwitchPreference.setEnabled(true);
            mKcalSwitchPreference.setOnPreferenceChangeListener(this);
            mInitialStatus = FileUtils.readOneLine(KcalUtils.KCAL_ENABLE_NODE);

            if (mInitialStatus.equals("1"))
                mKcalSwitchPreference.setChecked(true);
            else
                mKcalSwitchPreference.setChecked(false);

            mRedColorSlider = (SeekBarPreference) findPreference("red_slider");
            mRedColorSlider.setOnPreferenceChangeListener(this);
            mRedColorSlider.setSummary(String.valueOf(mRedColorSlider.getValue()));
            mRedColorSlider.setUpdatesContinuously(true);

            mGreenColorSlider = (SeekBarPreference) findPreference("green_slider");
            mGreenColorSlider.setOnPreferenceChangeListener(this);
            mGreenColorSlider.setSummary(String.valueOf(mGreenColorSlider.getValue()));
            mGreenColorSlider.setUpdatesContinuously(true);

            mBlueColorSlider = (SeekBarPreference) findPreference("blue_slider");
            mBlueColorSlider.setOnPreferenceChangeListener(this);
            mBlueColorSlider.setSummary(String.valueOf(mBlueColorSlider.getValue()));
            mBlueColorSlider.setUpdatesContinuously(true);
        } else {
            mKcalSwitchPreference.setEnabled(false);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals("kcal_enable")) {
            FileUtils.writeLine(KcalUtils.KCAL_ENABLE_NODE, (Boolean) newValue ? "1" : "0");
        } else if (preference.getKey().equals("red_slider")) {
             KcalUtils.writeConfigToNode(1, (Integer) newValue);
             preference.setSummary(String.valueOf(newValue));
        } else if (preference.getKey().equals("green_slider")) {
            KcalUtils.writeConfigToNode(2, (Integer) newValue);
            mGreenColorSlider.setSummary(String.valueOf(newValue));
        } else if (preference.getKey().equals("blue_slider")) {
            KcalUtils.writeConfigToNode(3, (Integer) newValue);
            mBlueColorSlider.setSummary(String.valueOf(newValue));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }
}
