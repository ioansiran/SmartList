package com.akitektuo.smartlist.util;

import android.content.Context;
import android.content.SharedPreferences;

import static com.akitektuo.smartlist.util.Constant.COLOR_BLUE;
import static com.akitektuo.smartlist.util.Constant.CURRENCY_RON;
import static com.akitektuo.smartlist.util.Constant.KEY_AUTO_FILL;
import static com.akitektuo.smartlist.util.Constant.KEY_COLOR;
import static com.akitektuo.smartlist.util.Constant.KEY_CREATED;
import static com.akitektuo.smartlist.util.Constant.KEY_CURRENCY;
import static com.akitektuo.smartlist.util.Constant.KEY_INITIALIZE;
import static com.akitektuo.smartlist.util.Constant.KEY_RECOMMENDATIONS;
import static com.akitektuo.smartlist.util.Constant.KEY_SMART_PRICE;

/**
 * Created by AoD Akitektuo on 18-Mar-17.
 */

public class Preference {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Preference(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(KEY_INITIALIZE, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    private void savePreferences() {
        editor.commit();
    }

    public void setPreference(String key, boolean bool) {
        editor.putBoolean(key, bool);
        savePreferences();
    }

    public void setPreference(String key, int num) {
        editor.putInt(key, num);
        savePreferences();
    }

    public void setPreference(String key, String string) {
        editor.putString(key, string);
        savePreferences();
    }

    public boolean getPreferenceBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public int getPreferenceInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public String getPreferenceString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void setDefault() {
        setPreference(KEY_CREATED, true);
        setPreference(KEY_CURRENCY, CURRENCY_RON);
        setPreference(KEY_RECOMMENDATIONS, true);
        setPreference(KEY_AUTO_FILL, true);
        setPreference(KEY_SMART_PRICE, 100);
        setPreference(KEY_COLOR, COLOR_BLUE);
    }
}
