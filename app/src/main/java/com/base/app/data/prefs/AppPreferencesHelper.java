package com.base.app.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class AppPreferencesHelper implements PreferencesHelper {

    private static final long DEFAULT_VALUE_LONG = -1;
    private static final int DEFAULT_VALUE_INTEGER = -1;
    private static final int DEFAULT_VALUE_FLOAT = -1;

    public static final String PREF_KEY_USERNAME = "PREF_KEY_USERNAME";
    public static final String PREF_KEY_PASSWORD = "PREF_KEY_PASSWORD";
    public static final String PREF_KEY_TOKEN = "PREF_KEY_TOKEN";

    public static final String PREF_KEY_DESTINATION = "PREF_KEY_DESTINATION";
    public static final String PREF_KEY_DEPARTURE = "PREF_KEY_DEPARTURE";

    private final SharedPreferences mPrefs;

    public AppPreferencesHelper(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void save(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).apply();
    }

    @Override
    public void save(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
    }

    @Override
    public void save(String key, float value) {
        mPrefs.edit().putFloat(key, value).apply();
    }

    @Override
    public void save(String key, int value) {
        mPrefs.edit().putInt(key, value).apply();
    }

    @Override
    public void save(String key, long value) {
        mPrefs.edit().putLong(key, value).apply();
    }

    @Override
    public boolean getBoolean(String key) {
        return mPrefs.getBoolean(key, false);
    }

    @Override
    public String getString(String key) {
        return mPrefs.getString(key, null);
    }

    @Override
    public long getLong(String key) {
        return mPrefs.getLong(key, DEFAULT_VALUE_LONG);
    }

    @Override
    public int getInt(String key) {
        return mPrefs.getInt(key, DEFAULT_VALUE_INTEGER);
    }

    @Override
    public float getFloat(String key) {
        return mPrefs.getFloat(key, DEFAULT_VALUE_FLOAT);
    }

    @Override
    public void remove(String key) {
        mPrefs.edit().remove(key).apply();
    }
}
