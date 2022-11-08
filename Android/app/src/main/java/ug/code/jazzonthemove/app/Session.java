package ug.code.jazzonthemove.app;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "jazz-on-the-move";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LOGIN_IS_ON = "loggedin";

    public Session(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(LOGIN_IS_ON, isLoggedIn);
        editor.commit();
    }

    public boolean loggedIn() {
        return pref.getBoolean(LOGIN_IS_ON, true);
    }


    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}