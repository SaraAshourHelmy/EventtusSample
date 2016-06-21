package sara.com.DBModels;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sara.com.Utility.UtilityMethod;
import twitter4j.ResponseList;
import twitter4j.User;

/**
 * Created by sara on 6/19/2016.
 */
public class UserDB {

    public static void InsertUser(ResponseList<User> users, Context con) {

        User user;
        SQLiteDatabase db = UtilityMethod.ReadDatabase(con);
        String query = "insert or Replace into user(id,user_name,bio,profile_img,bg_img) " +
                "values(?,?,?,?,?)";

        db.beginTransaction();
        try {

            for (int i = 0; i < users.size(); i++) {

                user = users.get(i);
                SQLiteStatement insertStmt = db.compileStatement(query);
                insertStmt.clearBindings();
                insertStmt.bindLong(1, user.getId());
                insertStmt.bindString(2, user.getName());
                insertStmt.bindString(3, user.getDescription());
                insertStmt.bindString(4, user.getProfileImageURL());
                insertStmt.bindString(5, user.getProfileBannerURL() + "");
                //.getProfileBackgroundImageURL()
                insertStmt.executeInsert();

                insertStmt.clearBindings();
            }

            UtilityMethod.SetLog("users", "inserted");

        } catch (Exception e) {
            UtilityMethod.SetLog("users db error", "" + e);
        }

        UtilityMethod.CloseDatabase(db);
    }

    public static ArrayList<sara.com.Models.User> GetUsers(Context con) {

        SQLiteDatabase db = UtilityMethod.ReadDatabase(con);
        ArrayList<sara.com.Models.User> users = new ArrayList<>();

        try {
            sara.com.Models.User user;
            String query = "select * from user";
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    user = new sara.com.Models.User();
                    user.setUserId(Long.valueOf(c.getString(c.getColumnIndex("id"))));
                    user.setUser_name(c.getString(c.getColumnIndex("user_name")));
                    user.setBio(c.getString(c.getColumnIndex("bio")));
                    user.setProfile_img(c.getString(c.getColumnIndex("profile_img")));
                    user.setBg_img(c.getString(c.getColumnIndex("bg_img")));

                    users.add(user);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.e("user db error", "" + e);
        }
        db.close();

        return users;
    }

}
