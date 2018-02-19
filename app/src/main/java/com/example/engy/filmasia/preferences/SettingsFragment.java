package com.example.engy.filmasia.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import com.example.engy.filmasia.R;

/**
 * Created by Engy on 2/17/2018.
 */

//This is the class for fragment of the settings to be used in SettingsActivity
public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener,Preference.OnPreferenceChangeListener {  //1.
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_fragment);

        //// To add summary to edit and list pref
        SharedPreferences sharedPreferences=getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen=getPreferenceScreen();
        int count=preferenceScreen.getPreferenceCount();

        for(int i=0;i<count;i++){
            Preference pref=preferenceScreen.getPreference(i);
            if(!(pref instanceof CheckBoxPreference)){
                setPrefSummary(pref,sharedPreferences.getString(pref.getKey(),""));
            }
        }

        ////To add validations to edit pref
        Preference preference=findPreference(getString(R.string.pref_size_key));
        preference.setOnPreferenceChangeListener(this);
    }
    private void setPrefSummary(Preference p,String value){
        if(p instanceof ListPreference){
            ListPreference listPreference=(ListPreference) p;
            int index=listPreference.findIndexOfValue(value);
            if(index>=0)
                listPreference.setSummary(((ListPreference) p).getEntries()[index]);
        }
        else if(p instanceof EditTextPreference){
            p.setSummary(value);
        }
    }

    @Override  //2.
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference p=findPreference(s); //s is the key
        if(p!=null &&!( p instanceof CheckBoxPreference)){
            setPrefSummary(p,sharedPreferences.getString(p.getKey(),""));
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast error = Toast.makeText(getContext(), "Please select a number between 16 and 24", Toast.LENGTH_SHORT);

        String sizeKey = getString(R.string.pref_size_key);
        if (preference.getKey().equals(sizeKey)) {
            String stringSize = ((String) (newValue)).trim();
            if (stringSize == null) stringSize = "1";
            try {
                float size = Float.parseFloat(stringSize);
                if (size > 24 || size < 16 ) {
                    error.show();
                    return false;
                }
            } catch (NumberFormatException nfe) {
                error.show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //3.
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //4.
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


}
