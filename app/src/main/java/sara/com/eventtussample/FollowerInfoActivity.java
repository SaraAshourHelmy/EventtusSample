package sara.com.eventtussample;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;


import de.hdodenhof.circleimageview.CircleImageView;
import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;
import sara.com.Adapters.TweetsAdapter;
import sara.com.DBModels.TweetDB;
import sara.com.Models.User;
import sara.com.Utility.StaticAssets;
import sara.com.Utility.StickyAnimator;
import sara.com.Utility.TwitterHelper;
import sara.com.Utility.UtilityMethod;

public class FollowerInfoActivity extends AppCompatActivity {

    private ImageView img_bg;
    private CircleImageView img_user;
    private ListView lstV_tweets;
    private String[] tweets;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_info);

        // setup actionbar
        UtilityMethod.SetupActionBar(this);
        SetupTools();

        // check internet
        if (UtilityMethod.HaveNetworkConnection(this)) {
            new GetTweetClass().execute();
        } else
            SetupTweetList();
    }

    private void SetupTools() {

        // get user info
        if (getIntent().getSerializableExtra("user") != null) {
            user = (User) getIntent().getSerializableExtra("user");
        }

        img_user = (CircleImageView) findViewById(R.id.img_user);
        img_bg = (ImageView) findViewById(R.id.header_image);
        lstV_tweets = (ListView) findViewById(R.id.lstV_user_tweets);

        // load user img
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
                .minHeightHeaderDim(R.dimen.zero_height)
                .animator(new StickyAnimator())
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
                // user close permission to get his tweets
                UtilityMethod.SetToast(FollowerInfoActivity.this, getString(R.string.no_access)
                        + " " + user.getUser_name());
            else
                SetupTweetList();

        }
    }

    private void SetupTweetList() {

        tweets = TweetDB.GetTweets(this, user.getUserId());
        lstV_tweets.setAdapter
                (new TweetsAdapter(FollowerInfoActivity.this,
                        R.layout.adapter_tweet, tweets));
    }

}
