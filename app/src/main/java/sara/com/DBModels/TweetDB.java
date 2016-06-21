package sara.com.DBModels;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import sara.com.Models.Tweet;
import sara.com.Utility.UtilityMethod;
import twitter4j.ResponseList;
import twitter4j.User;

/**
 * Created by sara on 6/20/2016.
 */
public class TweetDB {

    public static void InsertTweets(ArrayList<Tweet> tweets, Context con) {

        Tweet tweet;
        SQLiteDatabase db = UtilityMethod.ReadDatabase(con);
        String query = "insert or Replace into tweet(id,user_id,tweet) " +
                "values(?,?,?)";

        db.beginTransaction();
        try {

            for (int i = 0; i < tweets.size(); i++) {

                tweet = tweets.get(i);
                SQLiteStatement insertStmt = db.compileStatement(query);
                insertStmt.clearBindings();
                insertStmt.bindLong(1, tweet.getId());
                insertStmt.bindLong(2, tweet.getUser_id());
                insertStmt.bindString(3, tweet.getStatus());
                insertStmt.executeInsert();

                insertStmt.clearBindings();
            }

            Log.e("tweets", "inserted");
        } catch (Exception e) {
            Log.e("tweets db error", "" + e);
        }

        UtilityMethod.CloseDatabase(db);
    }

    public static String[] GetTweets(Context con, long user_id) {

        SQLiteDatabase db = UtilityMethod.ReadDatabase(con);
        String[] tweets = new String[0];

        try {

            String query = "select * from tweet where user_id=" + user_id;
            Cursor c = db.rawQuery(query, null);


            int i = 0;
            if (c.moveToFirst()) {
                tweets = new String[c.getCount()];
                do {

                    tweets[i] = c.getString(c.getColumnIndex("tweet"));
                    i++;

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.e("tweets db error", "" + e);
        }
        db.close();

        return tweets;
    }

}
