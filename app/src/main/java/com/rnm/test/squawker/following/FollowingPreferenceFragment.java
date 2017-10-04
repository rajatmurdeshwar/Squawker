package com.rnm.test.squawker.following;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.rnm.test.squawker.R;

/**
 * Created by Mahe on 9/25/2017.
 */

public class FollowingPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    private final static String LOG_TAG = FollowingPreferenceFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Add visualizer preferences, defined in the XML file in res->xml->preferences_squawker
        addPreferencesFromResource(R.xml.following_squawker);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference != null && preference instanceof SwitchPreferenceCompat) {
            // Get the current state of the switch preference
            boolean isOn = sharedPreferences.getBoolean(key, false);
            if (isOn) {
                // The preference key matches the following key for the associated instructor in
                // FCM. For example, the key for Lyla is key_lyla (as seen in
                // following_squawker.xml). The topic for Lyla's messages is /topics/key_lyla

                // Subscribe
                FirebaseMessaging.getInstance().subscribeToTopic(key);
                Log.d(LOG_TAG, "Subscribing to " + key);
            } else {
                // Un-subscribe
                FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
                Log.d(LOG_TAG, "Un-subscribing to " + key);
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add the shared preference change listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the shared preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
