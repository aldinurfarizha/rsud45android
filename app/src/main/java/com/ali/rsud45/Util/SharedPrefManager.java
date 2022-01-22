package com.ali.rsud45.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ali.rsud45.Auth.LoginPasien;
import com.ali.rsud45.Model.Credential;


public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "Aldinurfarizha";
    private static final String KEY_NAMA = "keynama";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_ROLE = "keyrole";
    private static final String KEY_ID = "keyid";
    private static final String KEY_OFF_ID = "keyoffid";
    private static final String KEY_OFF_NAME = "keyoffname";
    private static final String KEY_EXP= "key_exp";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }
    public void userLogin(Credential credential) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, credential.getId());
        editor.putInt(KEY_ROLE, credential.getRole());
        editor.putLong(KEY_EXP, credential.getExp());
        editor.putInt(KEY_OFF_ID, credential.getOff_id());
        editor.putString(KEY_USERNAME, credential.getUsername());
        editor.putString(KEY_NAMA, credential.getNama());
        editor.putString(KEY_OFF_NAME, credential.getOff_name());
        editor.apply();
    }
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }
    public Credential get() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Credential(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getInt(KEY_ROLE, 0),
                sharedPreferences.getInt(KEY_OFF_ID, 0),
                sharedPreferences.getLong(KEY_EXP, 0),
                sharedPreferences.getString(KEY_NAMA, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_OFF_NAME, null)
        );
    }
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginPasien.class));
    }
}