package sara.com.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import sara.com.DBModels.TweetDB;
import sara.com.DBModels.UserDB;
import sara.com.Models.Tweet;
import sara.com.eventtussample.R;
import sara.com.eventtussample.SecondActivity;
import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by sara on 6/17/2016.
 */
public class TwitterHelper {

    public static void SetupAuthentication(Context context) {


        TwitterAuthConfig authConfig = new TwitterAuthConfig
                (StaticAssets.TWITTER_KEY, StaticAssets.TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));

    }

    public static void GetUserData(final TwitterSession twitter_session, final Context context) {

        // get authentication info
        TwitterAuthToken authToken = twitter_session.getAuthToken();
        final String token = authToken.token;
        final String secret = authToken.secret;

        final SharedUserData userData = new SharedUserData(context);
        userData.SetToken(token);
        userData.SetSecret(secret);

        Twitter.getApiClient(twitter_session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {

                    @Override
                    public void failure(TwitterException e) {

                        UtilityMethod.SetToast(context, context.getString(R.string.error_data));
                    }

                    @Override
                    public void success(Result<User> userResult) {

                        User user = userResult.data;
                        userData.SaveUserData(user.name,
                                user.profileImageUrl.replace("_normal", "_bigger"), true,
                                user.id, user.screenName);

                        // move to second activity
                        Intent intent = new Intent(context, SecondActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }

                });

    }

    public void GetFollowers(Context context, long user_id) {

        long followerCursor = -1;
        IDs followerIds;
        try {
            do {
                twitter4j.Twitter twitter = GetTwitter(context);
                followerIds = twitter.getFollowersIDs(
                        user_id, followerCursor);
                ResponseList<twitter4j.User> followers = twitter.lookupUsers(followerIds.getIDs());

                // save followers in DB
                UserDB.InsertUser(followers, context);

            } while ((followerCursor = followerIds.getNextCursor()) != 0);
        } catch (Exception e) {

            UtilityMethod.SetLog("followers_error", e + "");
        }


    }

    public static twitter4j.Twitter GetTwitter(Context context) {

        SharedUserData userData = new SharedUserData(context);
        ConfigurationBuilder config =
                new ConfigurationBuilder()
                        .setOAuthConsumerKey(StaticAssets.TWITTER_KEY)
                        .setOAuthConsumerSecret(StaticAssets.TWITTER_SECRET)
                        .setOAuthAccessToken(userData.getToken())
                        .setOAuthAccessTokenSecret(userData.getSecret());

        return new TwitterFactory(config.build()).getInstance();

    }

    public static boolean getLastTweet(Context context, long user_id) {

        boolean check = true;
        twitter4j.Twitter twitter = TwitterHelper.GetTwitter(context);
        int numTweets = 10;
        ResponseList statuses = null;

        try {
            statuses = twitter.getUserTimeline(user_id);

        } catch (Exception e) {

            check = false;
            Log.e("tweet_error", e + "");
        }

        Tweet tweet;
        ArrayList<Tweet> lst_tweets = new ArrayList<>();
        if (statuses != null) {
            for (int i = 0; i < statuses.size(); i++) {

                tweet = new Tweet();
                Status status = (Status) statuses.get(i);
                if (i < numTweets) {

                    tweet.setId(status.getId());
                    tweet.setStatus(status.getText());
                    tweet.setUser_id(status.getUser().getId());
                    lst_tweets.add(tweet);
                } else
                    break;
            }
            TweetDB.InsertTweets(lst_tweets, context);
        }
        return check;
    }
}

