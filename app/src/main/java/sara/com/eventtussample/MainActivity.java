package sara.com.eventtussample;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import sara.com.Utility.SharedUserData;
import sara.com.Utility.TwitterHelper;
import sara.com.Utility.UtilityMethod;

public class MainActivity extends AppCompatActivity {

    private TwitterLoginButton btn_login;
    private TwitterSession twitter_session;
    private SharedUserData userData;
    // Test github

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userData = new SharedUserData(this);

        if (!userData.getLoginStatus() && !UtilityMethod.HaveNetworkConnection(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);

        } else {

            TwitterHelper.SetupAuthentication(this);
            setContentView(R.layout.activity_main);
            SetupTools();
            SetupCallBack();
        }

    }

    private void SetupTools() {

        btn_login = (TwitterLoginButton) findViewById(R.id.btn_twitter_login);

        if (userData.getLoginStatus()) {
            MoveToSecond();
        }
        // create db first time only
        if (!userData.getFirst())
            UtilityMethod.CreateDB(MainActivity.this);
    }

    private void SetupCallBack() {

        btn_login.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {


                twitter_session = result.data;

                // get and save userData
                TwitterHelper.GetUserData(twitter_session, MainActivity.this);
            }

            @Override
            public void failure(TwitterException exception) {

                UtilityMethod.SetToast(MainActivity.this, getString(R.string.twitter_error));

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            {
                // check error because of Internet
                UtilityMethod.HaveNetworkConnection(MainActivity.this);
                UtilityMethod.SetLog("result", "fail");
                UtilityMethod.clearApplicationData(this);
                finish();

            }
        } else
            btn_login.onActivityResult(requestCode, resultCode, data);

    }

    public void MoveToSecond() {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
        finish();
    }


}
