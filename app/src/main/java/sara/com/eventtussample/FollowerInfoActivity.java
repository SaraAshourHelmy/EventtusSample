package sara.com.eventtussample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;
import sara.com.DBModels.TweetDB;
import sara.com.Models.Tweet;
import sara.com.Models.User;
import sara.com.Utility.SharedUserData;
import sara.com.Utility.StaticAssets;
import sara.com.Utility.TwitterHelper;
import sara.com.Utility.UtilityMethod;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class FollowerInfoActivity extends AppCompatActivity {

    private ImageView img_user, img_bg;
    private ListView lstV_tweets;
    private String[] tweets;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_info);

        SetupTools();
        if (UtilityMethod.HaveNetworkConnection(this)) {
            new GetTweetClass().execute();
        } else
            SetupTweetList();
    }

    private void SetupTools() {


        if (getIntent().getSerializableExtra("user") != null) {
            user = (User) getIntent().getSerializableExtra("user");
        }

        img_user = (ImageView) findViewById(R.id.img_user);
        img_bg = (ImageView) findViewById(R.id.header_image);
        lstV_tweets = (ListView) findViewById(R.id.lstV_user_tweets);

        if (user != null) {
            UrlImageViewHelper.setUrlDrawable
                    (img_user, user.getProfile_img(),
                            R.drawable.user_icon, StaticAssets.CacheTime);

            if (user.getBg_img() != null && user.getBg_img().length() > 0)
                UrlImageViewHelper.setUrlDrawable
                        (img_bg, user.getBg_img(),
                                R.drawable.bg, StaticAssets.CacheTime);
        }

        SetupStickyHeader();
    }

    private void SetupStickyHeader() {
        StikkyHeaderBuilder.stickTo(lstV_tweets)
                .setHeader(R.id.header, (ViewGroup) findViewById(R.id.container))
                .minHeightHeaderDim(R.dimen.min_height_header)
                .animator(new ParallaxStickyAnimator())
                .build();
    }

    public class GetTweetClass extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            return TwitterHelper.getLastTweet(FollowerInfoActivity.this,
                    user.getUserId());

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (!result)
                UtilityMethod.SetToast(FollowerInfoActivity.this, getString(R.string.no_access)
                        +" "+ user.getUser_name());
            else
                SetupTweetList();

        }
    }

    private void SetupTweetList() {

        tweets = TweetDB.GetTweets(this, user.getUserId());
        lstV_tweets.setAdapter
                (new ArrayAdapter<String>(FollowerInfoActivity.this,
                        android.R.layout.simple_list_item_1, tweets));
    }

    private class ParallaxStickyAnimator extends HeaderStikkyAnimator {
        @Override
        public AnimatorBuilder getAnimatorBuilder() {
            View mHeader_image = getHeader().findViewById(R.id.header_image);
            return AnimatorBuilder.create().applyVerticalParallax(mHeader_image);
        }
    }
}
