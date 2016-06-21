package sara.com.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import sara.com.eventtussample.R;

/**
 * Created by sara on 6/17/2016.
 */
public class SharedUserData {

    public SharedPreferences sharedUser;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedUserData(Context context) {
        this.context = context;
        sharedUser = context.getSharedPreferences(StaticAssets.SharedUserData, Context.MODE_PRIVATE);
        editor = sharedUser.edit();
    }

    public void SaveUserData(String userName, String profile_img, boolean login,
                             long id, String screen_name) {

        editor.putString("userName", userName);
        editor.putString("profileImg", profile_img);
        editor.putBoolean("login", login);
        editor.putLong("id", id);
        editor.putString("screen_name", screen_name);
        editor.commit();
    }

    public void SetFirst() {
        editor.putBoolean("First", true);
        editor.commit();
    }

    public void SetToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public void SetSecret(String secret) {
        editor.putString("secret", secret);
        editor.commit();
    }

    public boolean getFirst() {
        return sharedUser.getBoolean("First", false);
    }

    public String getToken() {
        return sharedUser.getString("token", "");
    }

    public String getSecret() {
        return sharedUser.getString("secret", "");
    }

    public long getUserId() {
        return sharedUser.getLong("id", 0);
    }

    public String getScreenName(Context context) {
        return sharedUser.getString("screen_name", context.getString(R.string.no_name));
    }

    public String getUserName() {
        return sharedUser.getString("userName", "");
    }

    public String getProfileImg() {
        return sharedUser.getString("profileImg", context.getString(R.string.no_img));
    }

    public boolean getLoginStatus() {
        return sharedUser.getBoolean("login", false);
    }
}
